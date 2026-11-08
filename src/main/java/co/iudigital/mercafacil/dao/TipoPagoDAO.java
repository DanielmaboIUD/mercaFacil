package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.TipoPago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoPagoDAO implements DAO<TipoPago, String> {

    private static final String INSERTAR = "INSERT INTO tipo_pago (cod_tipo_pago, descripcion) VALUES (?, ?)";
    private static final String BUSCAR = "SELECT cod_tipo_pago, descripcion FROM tipo_pago WHERE cod_tipo_pago = ?";
    private static final String LISTAR = "SELECT cod_tipo_pago, descripcion FROM tipo_pago ORDER BY cod_tipo_pago";
    private static final String CONTAR = "SELECT count(*) FROM tipo_pago";
    private static final String LISTAR_PAGINA = LISTAR + " LIMIT ? OFFSET ?";
    private static final String ACTUALIZAR = "UPDATE tipo_pago SET descripcion = ? WHERE cod_tipo_pago = ?";
    private static final String ELIMINAR = "DELETE FROM tipo_pago WHERE cod_tipo_pago = ?";

    @Override
    public void insertar(Connection conexion, TipoPago t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getCodTipoPago());
            ps.setString(2, t.getDescripcion());
            ps.executeUpdate();
        }
    }

    @Override
    public TipoPago buscarPorId(Connection conexion, String id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<TipoPago> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<TipoPago> lista = new ArrayList<>();
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

    public List<TipoPago> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<TipoPago> lista = new ArrayList<>();
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
    public void actualizar(Connection conexion, TipoPago t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setString(1, t.getDescripcion());
            ps.setString(2, t.getCodTipoPago());
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

    private TipoPago mapear(ResultSet rs) throws SQLException {
        return new TipoPago(rs.getString("cod_tipo_pago"), rs.getString("descripcion"));
    }
}