package co.iudigital.mercafacil.web;

import jakarta.servlet.http.HttpServletRequest;

/** Mensajes que sobreviven a un redirect; header.jspf los muestra una sola vez. */
final class Flash {

    private Flash() {
    }

    static void exito(HttpServletRequest request, String mensaje) {
        request.getSession().setAttribute("flashExito", mensaje);
    }

    static void error(HttpServletRequest request, String mensaje) {
        request.getSession().setAttribute("flashError", mensaje);
    }
}
