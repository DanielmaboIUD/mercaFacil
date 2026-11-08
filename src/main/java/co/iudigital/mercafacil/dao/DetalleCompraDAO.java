package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.DetalleCompra;
import co.iudigital.mercafacil.model.DetalleCompraId;
import co.iudigital.mercafacil.model.DetalleListado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleCompraDAO implements DAOConsulta<DetalleCompra, DetalleCompraId> {

    private static final String COLUMNAS = "num_factura, cod_articulo, cantidad, valor_unitario";
    private static final String INSERTAR = "INSERT INTO detalle_compra (" + COLUMNAS + ") VALUES (?, ?, ?, ?)";
    private static final String BUSCAR =
            "SELECT " + COLUMNAS + " FROM detalle_compra WHERE num_factura = ? AND cod_articulo = ?";
    private static final String LISTAR =
            "SELECT " + COLUMNAS + " FROM detalle_compra ORDER BY num_factura, cod_articulo";
    private static final String LISTAR_POR_FACTURA =
            "SELECT d.cod_articulo, a.nombre AS nombre_articulo, d.cantidad, d.valor_unitario "
                    + "FROM detalle_compra d JOIN articulo a ON a.cod_articulo = d.cod_articulo "
                    + "WHERE d.num_factura = ? ORDER BY d.cod_articulo";

    @Override
    public void insertar(Connection conexion, DetalleCompra t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getNumFactura());
            ps.setString(2, t.getCodArticulo());
            ps.setInt(3, t.getCantidad());
            ps.setBigDecimal(4, t.getValorUnitario());
            ps.executeUpdate();
        }
    }

    @Override
    public DetalleCompra buscarPorId(Connection conexion, DetalleCompraId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id.numFactura());
            ps.setString(2, id.codArticulo());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<DetalleCompra> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<DetalleCompra> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public List<DetalleListado> listarPorFactura(String numFactura) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_POR_FACTURA)) {
            ps.setString(1, numFactura);
            try (ResultSet rs = ps.executeQuery()) {
                List<DetalleListado> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new DetalleListado(rs.getString("cod_articulo"), rs.getString("nombre_articulo"),
                            rs.getInt("cantidad"), rs.getBigDecimal("valor_unitario")));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    private DetalleCompra mapear(ResultSet rs) throws SQLException {
        return new DetalleCompra(rs.getString("num_factura"), rs.getString("cod_articulo"), rs.getInt("cantidad"),
                rs.getBigDecimal("valor_unitario"));
    }
}
