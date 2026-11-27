package co.iudigital.mercafacil.config;

import co.iudigital.mercafacil.exception.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public final class JpaUtil {

    private static final String UNIDAD = "mercaFacilPU";

    private static EntityManagerFactory emf;

    private JpaUtil() {
    }

    // Si falla (BD apagada, contraseña errada), se reintenta en la siguiente petición.
    public static synchronized void iniciar() {
        if (emf != null) {
            return;
        }
        Properties propiedades = ConexionBD.propiedades();
        Map<String, String> credenciales = Map.of(
                "jakarta.persistence.jdbc.url", propiedades.getProperty("db.url"),
                "jakarta.persistence.jdbc.user", propiedades.getProperty("db.usuario"),
                "jakarta.persistence.jdbc.password", propiedades.getProperty("db.contrasena"));
        try {
            emf = Persistence.createEntityManagerFactory(UNIDAD, credenciales);
        } catch (RuntimeException e) {
            throw new DAOException(mensajePara(e), e);
        }
    }

    public static EntityManager crearEntityManager() {
        iniciar();
        return emf.createEntityManager();
    }

    public static synchronized void cerrar() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }

    // Reutiliza los mensajes de ConexionBD si la causa es un error de conexión.
    private static String mensajePara(Throwable e) {
        for (Throwable causa = e; causa != null; causa = causa.getCause()) {
            if (causa instanceof SQLException sql) {
                return ConexionBD.mensajePara(sql);
            }
        }
        return "No se pudo iniciar JPA.";
    }
}
