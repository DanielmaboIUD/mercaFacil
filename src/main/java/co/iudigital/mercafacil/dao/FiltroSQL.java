package co.iudigital.mercafacil.dao;

final class FiltroSQL {

    private static final String SIN_TILDES = "'áéíóúü', 'aeiouu'";

    private FiltroSQL() {
    }

    /** strpos en lugar de LIKE para que % y _ se busquen como texto; un filtro vacío coincide con todo. */
    static String contiene(String... columnas) {
        return " WHERE strpos(translate(lower(concat_ws(' ', " + String.join(", ", columnas) + ")), " + SIN_TILDES
                + "), translate(lower(?), " + SIN_TILDES + ")) > 0";
    }
}
