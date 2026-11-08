package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.ArticuloProveedor;
import co.iudigital.mercafacil.model.ArticuloProveedorListado;
import co.iudigital.mercafacil.model.ArticuloProveedorId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticuloProveedorDAO implements DAO<ArticuloProveedor, ArticuloProveedorId> {

    private static final String INSERTAR =
            "INSERT INTO articulo_proveedor (cod_articulo, nit, precio_compra) VALUES (?, ?, ?)";
    private static final String BUSCAR =
            "SELECT cod_articulo, nit, precio_compra FROM articulo_proveedor WHERE cod_articulo = ? AND nit = ?";
    private static final String LISTAR =
            "SELECT cod_articulo, nit, precio_compra FROM articulo_proveedor ORDER BY cod_articulo, nit";
    private static final String CONTAR = "SELECT count(*) FROM articulo_proveedor";
    private static final String LISTAR_PAGINA =
            "SELECT t.cod_articulo, t.nit, t.precio_compra, a.nombre AS nombre_articulo, "
                    + "s.nombre AS nombre_segundo FROM articulo_proveedor t "
                    + "JOIN articulo a ON a.cod_articulo = t.cod_articulo "
                    + "JOIN proveedor s ON s.nit = t.nit "
                    + "ORDER BY t.cod_articulo, t.nit LIMIT ? OFFSET ?";
    private static final String ACTUALIZAR =
            "UPDATE articulo_proveedor SET precio_compra = ? WHERE cod_articulo = ? AND nit = ?";
    private static final String ELIMINAR = "DELETE FROM articulo_proveedor WHERE cod_articulo = ? AND nit = ?";

    @Override
    public void insertar(Connection conexion, ArticuloProveedor t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getCodArticulo());
            ps.setString(2, t.getNit());
            ps.setBigDecimal(3, t.getPrecioCompra());
            ps.executeUpdate();
        }
    }

    @Override
    public ArticuloProveedor buscarPorId(Connection conexion, ArticuloProveedorId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id.codArticulo());
            ps.setString(2, id.nit());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<ArticuloProveedor> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<ArticuloProveedor> lista = new ArrayList<>();
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

    public List<ArticuloProveedorListado> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<ArticuloProveedorListado> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new ArticuloProveedorListado(rs.getString("cod_articulo"), rs.getString("nit"),
                            rs.getBigDecimal("precio_compra"), rs.getString("nombre_articulo"),
                            rs.getString("nombre_segundo")));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    @Override
    public void actualizar(Connection conexion, ArticuloProveedor t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setBigDecimal(1, t.getPrecioCompra());
            ps.setString(2, t.getCodArticulo());
            ps.setString(3, t.getNit());
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    @Override
    public void eliminar(Connection conexion, ArticuloProveedorId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ELIMINAR)) {
            ps.setString(1, id.codArticulo());
            ps.setString(2, id.nit());
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    private ArticuloProveedor mapear(ResultSet rs) throws SQLException {
        return new ArticuloProveedor(rs.getString("cod_articulo"), rs.getString("nit"), rs.getBigDecimal("precio_compra"));
    }
}
