package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.Articulo;
import co.iudigital.mercafacil.model.ArticuloListado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAO implements DAO<Articulo, String> {

    private static final String INSERTAR =
            "INSERT INTO articulo (cod_articulo, nombre, cod_unidad, cod_grupo) VALUES (?, ?, ?, ?)";
    private static final String BUSCAR =
            "SELECT cod_articulo, nombre, cod_unidad, cod_grupo FROM articulo WHERE cod_articulo = ?";
    private static final String LISTAR =
            "SELECT cod_articulo, nombre, cod_unidad, cod_grupo FROM articulo ORDER BY cod_articulo";
    private static final String CONTAR = "SELECT count(*) FROM articulo";
    private static final String LISTAR_PAGINA =
            "SELECT a.cod_articulo, a.nombre, a.cod_unidad, a.cod_grupo, "
                    + "u.descripcion AS descripcion_unidad, g.descripcion AS descripcion_grupo "
                    + "FROM articulo a "
                    + "JOIN unidad_medida u ON u.cod_unidad = a.cod_unidad "
                    + "JOIN grupo g ON g.cod_grupo = a.cod_grupo "
                    + "ORDER BY a.cod_articulo LIMIT ? OFFSET ?";
    private static final String ACTUALIZAR =
            "UPDATE articulo SET nombre = ?, cod_unidad = ?, cod_grupo = ? WHERE cod_articulo = ?";
    private static final String ELIMINAR = "DELETE FROM articulo WHERE cod_articulo = ?";

    @Override
    public void insertar(Connection conexion, Articulo t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getCodArticulo());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getCodUnidad());
            ps.setString(4, t.getCodGrupo());
            ps.executeUpdate();
        }
    }

    @Override
    public Articulo buscarPorId(Connection conexion, String id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Articulo> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<Articulo> lista = new ArrayList<>();
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

    public List<ArticuloListado> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<ArticuloListado> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new ArticuloListado(
                            rs.getString("cod_articulo"),
                            rs.getString("nombre"),
                            rs.getString("cod_unidad"),
                            rs.getString("cod_grupo"),
                            rs.getString("descripcion_unidad"),
                            rs.getString("descripcion_grupo")));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    @Override
    public void actualizar(Connection conexion, Articulo t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getCodUnidad());
            ps.setString(3, t.getCodGrupo());
            ps.setString(4, t.getCodArticulo());
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

    private Articulo mapear(ResultSet rs) throws SQLException {
        return new Articulo(rs.getString("cod_articulo"), rs.getString("nombre"),
                rs.getString("cod_unidad"), rs.getString("cod_grupo"));
    }
}
