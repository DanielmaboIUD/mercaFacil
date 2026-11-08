package co.iudigital.mercafacil.web;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/** Fila del formulario tal como se digitó, para poder mostrarla de nuevo si hay errores. */
public class FilaDetalle {

    private final String codArticulo;
    private final String cantidad;
    private final String valorUnitario;

    public FilaDetalle(String codArticulo, String cantidad, String valorUnitario) {
        this.codArticulo = codArticulo;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
    }

    static List<FilaDetalle> leer(HttpServletRequest request) {
        String[] articulos = request.getParameterValues("codArticulo");
        String[] cantidades = request.getParameterValues("cantidad");
        String[] valores = request.getParameterValues("valorUnitario");
        int total = Math.max(largo(articulos), Math.max(largo(cantidades), largo(valores)));
        List<FilaDetalle> filas = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            FilaDetalle fila = new FilaDetalle(valor(articulos, i), valor(cantidades, i), valor(valores, i));
            if (!fila.estaVacia()) {
                filas.add(fila);
            }
        }
        return filas;
    }

    private boolean estaVacia() {
        return codArticulo.isEmpty() && cantidad.isEmpty() && valorUnitario.isEmpty();
    }

    private static int largo(String[] valores) {
        return valores == null ? 0 : valores.length;
    }

    private static String valor(String[] valores, int indice) {
        return valores == null || indice >= valores.length ? "" : valores[indice].trim();
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getValorUnitario() {
        return valorUnitario;
    }
}
