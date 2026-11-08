package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Crear y consultar. Las versiones con Connection permiten a los services agrupar operaciones
 * en una transacción; las versiones sin Connection abren la suya y traducen los errores.
 */
public interface DAOConsulta<T, K> {

    void insertar(Connection conexion, T t) throws SQLException;

    T buscarPorId(Connection conexion, K id) throws SQLException;

    List<T> listar(Connection conexion) throws SQLException;

    default void insertar(T t) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            insertar(conexion, t);
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    default T buscarPorId(K id) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            return buscarPorId(conexion, id);
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    default List<T> listar() {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            return listar(conexion);
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }
}
