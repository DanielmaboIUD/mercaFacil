package co.iudigital.mercafacil.config.jpa;

import co.iudigital.mercafacil.exception.DAOException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

// Cerrar el EMF al detener la app evita conexiones colgadas al redesplegar.
@WebListener
public class JpaListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        try {
            JpaUtil.iniciar();
        } catch (DAOException e) {
            // La app arranca igual; el error se mostrará al usar JPA.
            evento.getServletContext().log("JPA no se pudo iniciar: " + e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        JpaUtil.cerrar();
    }
}
