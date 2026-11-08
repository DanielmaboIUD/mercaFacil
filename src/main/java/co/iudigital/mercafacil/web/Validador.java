package co.iudigital.mercafacil.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

final class Validador {

    private Validador() {
    }

    /** Devuelve el mensaje de error, o null si el valor es válido. */
    static String texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isEmpty()) {
            return "El campo " + campo + " es obligatorio.";
        }
        if (valor.length() > maximo) {
            return "El campo " + campo + " admite máximo " + maximo + " caracteres.";
        }
        return null;
    }

    static String decimal(String valor, String campo, int digitosEnteros, int decimales) {
        if (valor == null || valor.isEmpty()) {
            return "El campo " + campo + " es obligatorio.";
        }
        BigDecimal numero = aDecimal(valor);
        if (numero == null) {
            return "El campo " + campo + " debe ser un número válido.";
        }
        BigDecimal normalizado = numero.stripTrailingZeros();
        if (normalizado.precision() - normalizado.scale() > digitosEnteros || normalizado.scale() > decimales) {
            return "El campo " + campo + " admite máximo " + digitosEnteros + " enteros y "
                    + decimales + " decimales.";
        }
        return null;
    }

    static String entero(String valor, String campo) {
        if (valor == null || valor.isEmpty()) {
            return "El campo " + campo + " es obligatorio.";
        }
        try {
            Integer.parseInt(valor);
            return null;
        } catch (NumberFormatException e) {
            return "El campo " + campo + " debe ser un número entero.";
        }
    }

    static String fecha(String valor, String campo) {
        if (valor == null || valor.isEmpty()) {
            return "El campo " + campo + " es obligatorio.";
        }
        return aFecha(valor) == null ? "El campo " + campo + " no es una fecha válida." : null;
    }

    /** Devuelve null si el texto no es una fecha; fecha() informa el error. */
    static LocalDate aFecha(String valor) {
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /** Devuelve null si el texto no es numérico; decimal() informa el error. */
    static BigDecimal aDecimal(String valor) {
        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Devuelve 0 si el texto no es entero; entero() informa el error. */
    static int aEntero(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    static String primerError(String... errores) {
        for (String error : errores) {
            if (error != null) {
                return error;
            }
        }
        return null;
    }
}
