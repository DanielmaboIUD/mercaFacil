package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.Grupo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO implements DAO<Grupo, String> {

    private static final String INSERTAR = "INSERT INTO grupo (cod_grupo, descripcion) VALUES (?, ?)";
    private static final String BUSCAR = "SELECT cod_grupo, descripcion FROM grupo WHERE cod_grupo = ?";
    private static final String LISTAR = "SELECT cod_grupo, descripcion FROM grupo ORDER BY cod_grupo";
    private static final String CONTAR = "SELECT count(*) FROM grupo";
    private static final String LISTAR_PAGINA = LISTAR + " LIMIT ? OFFSET ?";
    private static final String ACTUALIZAR = "UPDATE grupo SET descripcion = ? WHERE cod_grupo = ?";
    private static final String ELIMINAR = "DELETE FROM grupo WHERE cod_grupo = ?";

    @Override
    public void insertar(Connection conexion, Grupo t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getCodGrupo());
            ps.setString(2, t.getDescripcion());
            ps.executeUpdate();
        }
    }

    @Override
    public Grupo buscarPorId(Connection conexion, String id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Grupo> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<Grupo> lista = new ArrayList<>();
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

    public List<Grupo> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<Grupo> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    @Override
    public void actualizar(Connection conexion, Grupo t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setString(1, t.getDescripcion());
            ps.setString(2, t.getCodGrupo());
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

    private Grupo mapear(ResultSet rs) throws SQLException {
        return new Grupo(rs.getString("cod_grupo"), rs.getString("descripcion"));
    }
}