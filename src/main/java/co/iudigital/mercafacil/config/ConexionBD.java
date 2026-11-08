package co.iudigital.mercafacil.config;

import co.iudigital.mercafacil.exception.DAOException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {

    private static final String ARCHIVO = "db.properties";

    private ConexionBD() {
    }

    public static Connection obtenerConexion() {
        Properties propiedades = cargarPropiedades();
        try {
            // Dentro de Tomcat, DriverManager no siempre detecta los drivers de WEB-INF/lib.
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(
                    propiedades.getProperty("db.url"),
                    propiedades.getProperty("db.usuario"),
                    propiedades.getProperty("db.contrasena"));
        } catch (ClassNotFoundException e) {
            throw new DAOException("No se encontró el driver de PostgreSQL.", e);
        } catch (SQLException e) {
            throw new DAOException(mensajePara(e), e);
        }
    }

    private static Properties cargarPropiedades() {
        try (InputStream entrada = ConexionBD.class.getClassLoader().getResourceAsStream(ARCHIVO)) {
            if (entrada == null) {
                throw new DAOException("No se encontró " + ARCHIVO + " en el classpath.");
            }
            Properties propiedades = new Properties();
            propiedades.load(entrada);
            return propiedades;
        } catch (IOException e) {
            throw new DAOException("No se pudo leer " + ARCHIVO + ".", e);
        }
    }

    private static String mensajePara(SQLException e) {
        String estado = e.getSQLState() == null ? "" : e.getSQLState();
        return switch (estado) {
            // El driver reporta 08004 cuando la contraseña está vacía.
            case "28P01", "28000", "08004" -> "Usuario o contraseña de la base de datos incorrectos.";
            case "3D000" -> "La base de datos configurada no existe.";
            case "08001", "08006" -> "No se pudo conectar con el servidor de PostgreSQL.";
            default -> "Error al conectar con la base de datos.";
        };
    }
}
