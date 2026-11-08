package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.Venta;
import co.iudigital.mercafacil.model.MovimientoListado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO implements DAOConsulta<Venta, String> {

    private static final String COLUMNAS = "num_factura, fecha, valor_total, cod_tipo_pago, cedula, cod_almacen";
    private static final String INSERTAR = "INSERT INTO venta (" + COLUMNAS + ") VALUES (?, ?, ?, ?, ?, ?)";
    private static final String BUSCAR = "SELECT " + COLUMNAS + " FROM venta WHERE num_factura = ?";
    private static final String LISTAR = "SELECT " + COLUMNAS + " FROM venta ORDER BY fecha DESC, num_factura DESC";
    private static final String CONTAR = "SELECT count(*) FROM venta";
    private static final String BASE_LISTADO =
            "SELECT m.num_factura, m.fecha, m.valor_total, tp.descripcion AS tipo_pago, "
                    + "m.cedula AS id_tercero, t.nombre AS nombre_tercero, al.nombre AS nombre_almacen "
                    + "FROM venta m "
                    + "JOIN tipo_pago tp ON tp.cod_tipo_pago = m.cod_tipo_pago "
                    + "JOIN cliente t ON t.cedula = m.cedula "
                    + "JOIN almacen al ON al.cod_almacen = m.cod_almacen ";
    private static final String LISTAR_PAGINA =
            BASE_LISTADO + "ORDER BY m.fecha DESC, m.num_factura DESC LIMIT ? OFFSET ?";
    private static final String BUSCAR_LISTADO = BASE_LISTADO + "WHERE m.num_factura = ?";

    @Override
    public void insertar(Connection conexion, Venta t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getNumFactura());
            ps.setObject(2, t.getFecha());
            ps.setBigDecimal(3, t.getValorTotal());
            ps.setString(4, t.getCodTipoPago());
            ps.setString(5, t.getCedula());
            ps.setString(6, t.getCodAlmacen());
            ps.executeUpdate();
        }
    }

    @Override
    public Venta buscarPorId(Connection conexion, String id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Venta> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<Venta> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public int contar() {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(CONTAR);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    public List<MovimientoListado> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<MovimientoListado> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapearListado(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    public MovimientoListado buscarListado(String numFactura) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(BUSCAR_LISTADO)) {
            ps.setString(1, numFactura);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearListado(rs) : null;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    private Venta mapear(ResultSet rs) throws SQLException {
        return new Venta(rs.getString("num_factura"), rs.getObject("fecha", LocalDate.class),
                rs.getBigDecimal("valor_total"), rs.getString("cod_tipo_pago"), rs.getString("cedula"),
                rs.getString("cod_almacen"));
    }

    private MovimientoListado mapearListado(ResultSet rs) throws SQLException {
        return new MovimientoListado(rs.getString("num_factura"), rs.getObject("fecha", LocalDate.class),
                rs.getBigDecimal("valor_total"), rs.getString("tipo_pago"), rs.getString("id_tercero"),
                rs.getString("nombre_tercero"), rs.getString("nombre_almacen"));
    }
}
