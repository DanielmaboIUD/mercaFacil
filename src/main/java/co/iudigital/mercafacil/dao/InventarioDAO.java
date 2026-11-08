package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.model.Inventario;
import co.iudigital.mercafacil.model.InventarioId;
import co.iudigital.mercafacil.model.InventarioListado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO implements DAO<Inventario, InventarioId> {

    private static final String COLUMNAS = "cod_almacen, cod_articulo, existencia, cantidad_maxima, cantidad_minima";
    private static final String INSERTAR = "INSERT INTO inventario (" + COLUMNAS + ") VALUES (?, ?, ?, ?, ?)";
    private static final String BUSCAR =
            "SELECT " + COLUMNAS + " FROM inventario WHERE cod_almacen = ? AND cod_articulo = ?";
    private static final String LISTAR =
            "SELECT " + COLUMNAS + " FROM inventario ORDER BY cod_almacen, cod_articulo";
    private static final String BLOQUEAR = BUSCAR + " FOR UPDATE";
    private static final String SUMAR_EXISTENCIA =
            "UPDATE inventario SET existencia = existencia + ? WHERE cod_almacen = ? AND cod_articulo = ? "
                    + "RETURNING " + COLUMNAS;
    private static final String CONTAR = "SELECT count(*) FROM inventario";
    private static final String LISTAR_PAGINA =
            "SELECT i.cod_almacen, i.cod_articulo, i.existencia, i.cantidad_maxima, i.cantidad_minima, "
                    + "al.nombre AS nombre_almacen, a.nombre AS nombre_articulo FROM inventario i "
                    + "JOIN almacen al ON al.cod_almacen = i.cod_almacen "
                    + "JOIN articulo a ON a.cod_articulo = i.cod_articulo "
                    + "ORDER BY i.cod_almacen, i.cod_articulo LIMIT ? OFFSET ?";
    private static final String ACTUALIZAR =
            "UPDATE inventario SET existencia = ?, cantidad_maxima = ?, cantidad_minima = ? "
                    + "WHERE cod_almacen = ? AND cod_articulo = ?";
    private static final String ELIMINAR = "DELETE FROM inventario WHERE cod_almacen = ? AND cod_articulo = ?";

    @Override
    public void insertar(Connection conexion, Inventario t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(INSERTAR)) {
            ps.setString(1, t.getCodAlmacen());
            ps.setString(2, t.getCodArticulo());
            ps.setInt(3, t.getExistencia());
            ps.setInt(4, t.getCantidadMaxima());
            ps.setInt(5, t.getCantidadMinima());
            ps.executeUpdate();
        }
    }

    @Override
    public Inventario buscarPorId(Connection conexion, InventarioId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BUSCAR)) {
            ps.setString(1, id.codAlmacen());
            ps.setString(2, id.codArticulo());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Inventario> listar(Connection conexion) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(LISTAR);
             ResultSet rs = ps.executeQuery()) {
            List<Inventario> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    /** Bloquea la fila hasta el fin de la transacción; devuelve null si no existe. */
    public Inventario bloquear(Connection conexion, InventarioId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(BLOQUEAR)) {
            ps.setString(1, id.codAlmacen());
            ps.setString(2, id.codArticulo());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /** Devuelve la fila actualizada, o null si no existe. */
    public Inventario sumarExistencia(Connection conexion, InventarioId id, int cantidad) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(SUMAR_EXISTENCIA)) {
            ps.setInt(1, cantidad);
            ps.setString(2, id.codAlmacen());
            ps.setString(3, id.codArticulo());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
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

    public List<InventarioListado> listarPagina(int desplazamiento, int tamano) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(LISTAR_PAGINA)) {
            ps.setInt(1, tamano);
            ps.setInt(2, desplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                List<InventarioListado> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new InventarioListado(rs.getString("cod_almacen"), rs.getString("cod_articulo"),
                            rs.getInt("existencia"), rs.getInt("cantidad_maxima"), rs.getInt("cantidad_minima"),
                            rs.getString("nombre_almacen"), rs.getString("nombre_articulo")));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    @Override
    public void actualizar(Connection conexion, Inventario t) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ACTUALIZAR)) {
            ps.setInt(1, t.getExistencia());
            ps.setInt(2, t.getCantidadMaxima());
            ps.setInt(3, t.getCantidadMinima());
            ps.setString(4, t.getCodAlmacen());
            ps.setString(5, t.getCodArticulo());
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    @Override
    public void eliminar(Connection conexion, InventarioId id) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(ELIMINAR)) {
            ps.setString(1, id.codAlmacen());
            ps.setString(2, id.codArticulo());
            DAO.exigirFilaAfectada(ps.executeUpdate());
        }
    }

    private Inventario mapear(ResultSet rs) throws SQLException {
        return new Inventario(rs.getString("cod_almacen"), rs.getString("cod_articulo"), rs.getInt("existencia"),
                rs.getInt("cantidad_maxima"), rs.getInt("cantidad_minima"));
    }
}
