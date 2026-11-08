package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.DetallePedido;
import co.iudigital.mercafacil.model.DetallePedidoId;
import co.iudigital.mercafacil.model.DetallePedidoListado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDAO implements DAO<DetallePedido, DetallePedidoId> {

    private static final String COLUMNAS = "num_pedido, cod_articulo, cantidad";
    private static final String INSERTAR = "INSERT INTO detalle_pedido (" + COLUMNAS + ") VALUES (?, ?, ?)";
    private static final String BUSCAR =
            "SELECT " + COLUMNAS + " FROM detalle_pedido WHERE num_pedido = ? AND cod_articulo = ?";
    private static final String LISTAR =
            "SELECT " + COLUMNAS + " FROM detalle_pedido ORDER BY num_pedido, cod_articulo";
    private static final String LISTAR_POR_PEDIDO =
            "SELECT d.num_pedido, d.cod_articulo, d.cantidad, a.nombre AS nombre_articulo "
                    + "FROM detalle_pedido d JOIN articulo a ON a.cod_articulo = d.cod_articulo "
                    + "WHERE d.num_pedido = ? ORDER BY d.cod_articulo";
    private static final String ACTUALIZAR =
            "UPDATE detalle_pedido SET cantidad = ? WHERE num_pedido = ? AND cod_articulo = ?";
    private static final String ELIMINAR = "DELETE FROM detalle_pedido WHERE num_pedido = ? AND cod_articulo = ?";
    private static final String ELIMINAR_POR_PEDIDO = "DELETE FROM detalle_pedido WHERE num_pedido = ?";

    @Override
    public void insertar(Connection conexion, DetallePedido t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getNumPedido());
            ps.setString(2, t.getCodArticulo());
            ps.setInt(3, t.getCantidad());
            ps.executeUpdate();
        }
    }

    @Override
    public DetallePedido buscarPorId(Connection conexion, DetallePedidoId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id.numPedido());
            ps.setString(2, id.codArticulo());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<DetallePedido> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<DetallePedido> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public List<DetallePedidoListado> listarPorPedido(String numPedido) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_POR_PEDIDO)) {
            ps.setString(1, numPedido);
            try (ResultSet rs = ps.executeQuery()) {
                List<DetallePedidoListado> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new DetallePedidoListado(rs.getString("num_pedido"), rs.getString("cod_articulo"),
                            rs.getInt("cantidad"), rs.getString("nombre_articulo")));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    @Override
    public void actualizar(Connection conexion, DetallePedido t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setInt(1, t.getCantidad());
            ps.setString(2, t.getNumPedido());
            ps.setString(3, t.getCodArticulo());
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    @Override
    public void eliminar(Connection conexion, DetallePedidoId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ELIMINAR)) {
            ps.setString(1, id.numPedido());
            ps.setString(2, id.codArticulo());
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    public void eliminarPorPedido(Connection conexion, String numPedido) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ELIMINAR_POR_PEDIDO)) {
            ps.setString(1, numPedido);
            ps.executeUpdate();
        }
    }

    private DetallePedido mapear(ResultSet rs) throws SQLException {
        return new DetallePedido(rs.getString("num_pedido"), rs.getString("cod_articulo"), rs.getInt("cantidad"));
    }
}
