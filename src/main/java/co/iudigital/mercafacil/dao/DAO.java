package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.exception.DAOException;

import java.sql.Connection;
import java.sql.SQLException;

public interface DAO<T, K> extends DAOConsulta<T, K> {

    void actualizar(Connection conexion, T t) throws SQLException;

    void eliminar(Connection conexion, K id) throws SQLException;

    default void actualizar(T t) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            actualizar(conexion, t);
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    default void eliminar(K id) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            eliminar(conexion, id);
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, true);
        }
    }

    static void exigirFilaAfectada(int filas) {
        if (filas == 0) {
            throw new DAOException("El registro ya no existe.");
        }
    }
}
