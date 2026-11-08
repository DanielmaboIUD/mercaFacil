package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.Almacen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlmacenDAO implements DAO<Almacen, String> {

    private static final String INSERTAR =
            "INSERT INTO almacen (cod_almacen, nombre, direccion, nombre_gerente) VALUES (?, ?, ?, ?)";
    private static final String BUSCAR =
            "SELECT cod_almacen, nombre, direccion, nombre_gerente FROM almacen WHERE cod_almacen = ?";
    private static final String LISTAR =
            "SELECT cod_almacen, nombre, direccion, nombre_gerente FROM almacen ORDER BY cod_almacen";
    private static final String FILTRO = FiltroSQL.contiene("cod_almacen", "nombre", "direccion", "nombre_gerente");
    private static final String CONTAR = "SELECT count(*) FROM almacen" + FILTRO;
    private static final String LISTAR_PAGINA =
            "SELECT cod_almacen, nombre, direccion, nombre_gerente FROM almacen" + FILTRO + " ORDER BY cod_almacen LIMIT ? OFFSET ?";
    private static final String ACTUALIZAR =
            "UPDATE almacen SET nombre = ?, direccion = ?, nombre_gerente = ? WHERE cod_almacen = ?";
    private static final String ELIMINAR = "DELETE FROM almacen WHERE cod_almacen = ?";

    @Override
    public void insertar(Connection conexion, Almacen t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getCodAlmacen());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getDireccion());
            ps.setString(4, t.getNombreGerente());
            ps.executeUpdate();
        }
    }

    @Override
    public Almacen buscarPorId(Connection conexion, String id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Almacen> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<Almacen> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public int contar(String filtro) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(CONTAR)) {
            ps.setString(1, filtro);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    public List<Almacen> listarPagina(String filtro, int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setString(1, filtro);
            ps.setInt(2, tamano);
            ps.setInt(3, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<Almacen> lista = new ArrayList<>();
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
    public void actualizar(Connection conexion, Almacen t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDireccion());
            ps.setString(3, t.getNombreGerente());
            ps.setString(4, t.getCodAlmacen());
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

    private Almacen mapear(ResultSet rs) throws SQLException {
        return new Almacen(rs.getString("cod_almacen"), rs.getString("nombre"),
                rs.getString("direccion"), rs.getString("nombre_gerente"));
    }
}