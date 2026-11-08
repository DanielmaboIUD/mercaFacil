package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements DAO<Cliente, String> {

    private static final String INSERTAR =
            "INSERT INTO cliente (cedula, nombre, direccion, telefono) VALUES (?, ?, ?, ?)";
    private static final String BUSCAR =
            "SELECT cedula, nombre, direccion, telefono FROM cliente WHERE cedula = ?";
    private static final String LISTAR =
            "SELECT cedula, nombre, direccion, telefono FROM cliente ORDER BY cedula";
    private static final String CONTAR = "SELECT count(*) FROM cliente";
    private static final String LISTAR_PAGINA = LISTAR + " LIMIT ? OFFSET ?";
    private static final String ACTUALIZAR =
            "UPDATE cliente SET nombre = ?, direccion = ?, telefono = ? WHERE cedula = ?";
    private static final String ELIMINAR = "DELETE FROM cliente WHERE cedula = ?";

    @Override
    public void insertar(Connection conexion, Cliente t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getCedula());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getDireccion());
            ps.setString(4, t.getTelefono());
            ps.executeUpdate();
        }
    }

    @Override
    public Cliente buscarPorId(Connection conexion, String id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Cliente> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<Cliente> lista = new ArrayList<>();
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

    public List<Cliente> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<Cliente> lista = new ArrayList<>();
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
    public void actualizar(Connection conexion, Cliente t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDireccion());
            ps.setString(3, t.getTelefono());
            ps.setString(4, t.getCedula());
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

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(rs.getString("cedula"), rs.getString("nombre"),
                rs.getString("direccion"), rs.getString("telefono"));
    }
}