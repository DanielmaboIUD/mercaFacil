package co.iudigital.mercafacil.web;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/** Palabra clave de búsqueda de los listados. */
final class Filtro {

    private static final String PARAMETRO = "q";
    private static final int LARGO_MAXIMO = 100;

    private Filtro() {
    }

    static String leer(HttpServletRequest request) {
        String valor = request.getParameter(PARAMETRO);
        if (valor == null) {
            return "";
        }
        String limpio = valor.trim();
        return limpio.length() > LARGO_MAXIMO ? limpio.substring(0, LARGO_MAXIMO) : limpio;
    }

    /** Fragmento de URL para conservar la búsqueda tras un redirect. */
    static String sufijo(HttpServletRequest request) {
        String filtro = leer(request);
        return filtro.isEmpty() ? "" : "&" + PARAMETRO + "=" + URLEncoder.encode(filtro, StandardCharsets.UTF_8);
    }
}
