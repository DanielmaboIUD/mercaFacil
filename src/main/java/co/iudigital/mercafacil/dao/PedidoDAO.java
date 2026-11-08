package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.Pedido;
import co.iudigital.mercafacil.model.PedidoListado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements DAO<Pedido, String> {

    private static final String COLUMNAS = "num_pedido, fecha, nit, cod_almacen";
    private static final String INSERTAR = "INSERT INTO pedido (" + COLUMNAS + ") VALUES (?, ?, ?, ?)";
    private static final String BUSCAR = "SELECT " + COLUMNAS + " FROM pedido WHERE num_pedido = ?";
    private static final String LISTAR = "SELECT " + COLUMNAS + " FROM pedido ORDER BY fecha DESC, num_pedido DESC";
    private static final String CONTAR = "SELECT count(*) FROM pedido";
    private static final String BASE_LISTADO =
            "SELECT m.num_pedido, m.fecha, m.nit, m.cod_almacen, "
                    + "p.nombre AS nombre_proveedor, al.nombre AS nombre_almacen "
                    + "FROM pedido m "
                    + "JOIN proveedor p ON p.nit = m.nit "
                    + "JOIN almacen al ON al.cod_almacen = m.cod_almacen ";
    private static final String LISTAR_PAGINA =
            BASE_LISTADO + "ORDER BY m.fecha DESC, m.num_pedido DESC LIMIT ? OFFSET ?";
    private static final String BUSCAR_LISTADO = BASE_LISTADO + "WHERE m.num_pedido = ?";
    private static final String ACTUALIZAR = "UPDATE pedido SET fecha = ?, nit = ?, cod_almacen = ? WHERE num_pedido = ?";
    private static final String ELIMINAR = "DELETE FROM pedido WHERE num_pedido = ?";

    @Override
    public void insertar(Connection conexion, Pedido t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getNumPedido());
            ps.setObject(2, t.getFecha());
            ps.setString(3, t.getNit());
            ps.setString(4, t.getCodAlmacen());
            ps.executeUpdate();
        }
    }

    @Override
    public Pedido buscarPorId(Connection conexion, String id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Pedido> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<Pedido> lista = new ArrayList<>();
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

    public List<PedidoListado> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<PedidoListado> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapearListado(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    public PedidoListado buscarListado(String numPedido) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(BUSCAR_LISTADO)) {
            ps.setString(1, numPedido);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearListado(rs) : null;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    @Override
    public void actualizar(Connection conexion, Pedido t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setObject(1, t.getFecha());
            ps.setString(2, t.getNit());
            ps.setString(3, t.getCodAlmacen());
            ps.setString(4, t.getNumPedido());
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    @Override
    public void eliminar(Connection conexion, String id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ELIMINAR)) {
            ps.setString(1, id);
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        return new Pedido(rs.getString("num_pedido"), rs.getObject("fecha", LocalDate.class),
                rs.getString("nit"), rs.getString("cod_almacen"));
    }

    private PedidoListado mapearListado(ResultSet rs) throws SQLException {
        return new PedidoListado(rs.getString("num_pedido"), rs.getObject("fecha", LocalDate.class),
                rs.getString("nit"), rs.getString("cod_almacen"), rs.getString("nombre_proveedor"),
                rs.getString("nombre_almacen"));
    }
}
