package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO implements DAO<Proveedor, String> {

    private static final String INSERTAR =
            "INSERT INTO proveedor (nit, nombre, direccion, telefono) VALUES (?, ?, ?, ?)";
    private static final String BUSCAR =
            "SELECT nit, nombre, direccion, telefono FROM proveedor WHERE nit = ?";
    private static final String LISTAR =
            "SELECT nit, nombre, direccion, telefono FROM proveedor ORDER BY nit";
    private static final String FILTRO = FiltroSQL.contiene("nit", "nombre", "direccion", "telefono");
    private static final String CONTAR = "SELECT count(*) FROM proveedor" + FILTRO;
    private static final String LISTAR_PAGINA =
            "SELECT nit, nombre, direccion, telefono FROM proveedor" + FILTRO + " ORDER BY nit LIMIT ? OFFSET ?";
    private static final String ACTUALIZAR =
            "UPDATE proveedor SET nombre = ?, direccion = ?, telefono = ? WHERE nit = ?";
    private static final String ELIMINAR = "DELETE FROM proveedor WHERE nit = ?";

    @Override
    public void insertar(Connection conexion, Proveedor t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getNit());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getDireccion());
            ps.setString(4, t.getTelefono());
            ps.executeUpdate();
        }
    }

    @Override
    public Proveedor buscarPorId(Connection conexion, String id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Proveedor> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<Proveedor> lista = new ArrayList<>();
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

    public List<Proveedor> listarPagina(String filtro, int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setString(1, filtro);
            ps.setInt(2, tamano);
            ps.setInt(3, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<Proveedor> lista = new ArrayList<>();
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
    public void actualizar(Connection conexion, Proveedor t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDireccion());
            ps.setString(3, t.getTelefono());
            ps.setString(4, t.getNit());
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

    private Proveedor mapear(ResultSet rs) throws SQLException {
        return new Proveedor(rs.getString("nit"), rs.getString("nombre"),
                rs.getString("direccion"), rs.getString("telefono"));
    }
}