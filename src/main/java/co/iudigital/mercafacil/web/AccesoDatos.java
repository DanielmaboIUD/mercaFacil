package co.iudigital.mercafacil.web;

/** Texto de la etiqueta «Acceso a datos» que header.jspf muestra en cada pantalla. */
final class AccesoDatos {

    static final String ATRIBUTO = "accesoDatos";

    private AccesoDatos() {
    }

    // Se deduce del DAO en uso (convención: los DAO de JPA se llaman *JpaDAO), así la etiqueta cambia sola al volver a JDBC.
    static String de(Object dao) {
        return dao.getClass().getSimpleName().endsWith("JpaDAO") ? "JPA (Hibernate)" : "JDBC";
    }
}
