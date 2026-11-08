package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.ArticuloPrecio;
import co.iudigital.mercafacil.model.ArticuloPrecioListado;
import co.iudigital.mercafacil.model.ArticuloPrecioId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticuloPrecioDAO implements DAO<ArticuloPrecio, ArticuloPrecioId> {

    private static final String INSERTAR =
            "INSERT INTO articulo_precio (cod_articulo, cod_tipo_precio, valor) VALUES (?, ?, ?)";
    private static final String BUSCAR =
            "SELECT cod_articulo, cod_tipo_precio, valor FROM articulo_precio WHERE cod_articulo = ? AND cod_tipo_precio = ?";
    private static final String LISTAR =
            "SELECT cod_articulo, cod_tipo_precio, valor FROM articulo_precio ORDER BY cod_articulo, cod_tipo_precio";
    private static final String CONTAR = "SELECT count(*) FROM articulo_precio";
    private static final String LISTAR_PAGINA =
            "SELECT t.cod_articulo, t.cod_tipo_precio, t.valor, a.nombre AS nombre_articulo, "
                    + "s.descripcion AS nombre_segundo FROM articulo_precio t "
                    + "JOIN articulo a ON a.cod_articulo = t.cod_articulo "
                    + "JOIN tipo_precio s ON s.cod_tipo_precio = t.cod_tipo_precio "
                    + "ORDER BY t.cod_articulo, t.cod_tipo_precio LIMIT ? OFFSET ?";
    private static final String LISTAR_POR_TIPO =
            "SELECT t.cod_articulo, t.cod_tipo_precio, t.valor, a.nombre AS nombre_articulo, "
                    + "s.descripcion AS nombre_segundo FROM articulo_precio t "
                    + "JOIN articulo a ON a.cod_articulo = t.cod_articulo "
                    + "JOIN tipo_precio s ON s.cod_tipo_precio = t.cod_tipo_precio "
                    + "WHERE t.cod_tipo_precio = ? ORDER BY t.cod_articulo";
    private static final String ACTUALIZAR =
            "UPDATE articulo_precio SET valor = ? WHERE cod_articulo = ? AND cod_tipo_precio = ?";
    private static final String ELIMINAR = "DELETE FROM articulo_precio WHERE cod_articulo = ? AND cod_tipo_precio = ?";

    @Override
    public void insertar(Connection conexion, ArticuloPrecio t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getCodArticulo());
            ps.setString(2, t.getCodTipoPrecio());
            ps.setBigDecimal(3, t.getValor());
            ps.executeUpdate();
        }
    }

    @Override
    public ArticuloPrecio buscarPorId(Connection conexion, ArticuloPrecioId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id.codArticulo());
            ps.setString(2, id.codTipoPrecio());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<ArticuloPrecio> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<ArticuloPrecio> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public List<ArticuloPrecioListado> listarPorTipo(String tipoPrecio) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_POR_TIPO)) {
            ps.setString(1, tipoPrecio);
            try (ResultSet rs = ps.executeQuery()) {
                List<ArticuloPrecioListado> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new ArticuloPrecioListado(rs.getString("cod_articulo"), rs.getString("cod_tipo_precio"),
                            rs.getBigDecimal("valor"), rs.getString("nombre_articulo"),
                            rs.getString("nombre_segundo")));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
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

    public List<ArticuloPrecioListado> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<ArticuloPrecioListado> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new ArticuloPrecioListado(rs.getString("cod_articulo"), rs.getString("cod_tipo_precio"),
                            rs.getBigDecimal("valor"), rs.getString("nombre_articulo"),
                            rs.getString("nombre_segundo")));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    @Override
    public void actualizar(Connection conexion, ArticuloPrecio t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setBigDecimal(1, t.getValor());
            ps.setString(2, t.getCodArticulo());
            ps.setString(3, t.getCodTipoPrecio());
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    @Override
    public void eliminar(Connection conexion, ArticuloPrecioId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ELIMINAR)) {
            ps.setString(1, id.codArticulo());
            ps.setString(2, id.codTipoPrecio());
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    private ArticuloPrecio mapear(ResultSet rs) throws SQLException {
        return new ArticuloPrecio(rs.getString("cod_articulo"), rs.getString("cod_tipo_precio"), rs.getBigDecimal("valor"));
    }
}
