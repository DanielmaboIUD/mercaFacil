package co.iudigital.mercafacil.web;

public final class Pagina {

    private static final int TAMANO = 20;

    private final int numero;
    private final int totalRegistros;
    private final int totalPaginas;

    private Pagina(int numero, int totalRegistros, int totalPaginas) {
        this.numero = numero;
        this.totalRegistros = totalRegistros;
        this.totalPaginas = totalPaginas;
    }

    /** Ajusta la página pedida al rango válido; p. ej. al borrar la última fila de la última página. */
    public static Pagina de(String parametro, int totalRegistros) {
        int totalPaginas = Math.max(1, (totalRegistros + TAMANO - 1) / TAMANO);
        return new Pagina(Math.min(leer(parametro), totalPaginas), totalRegistros, totalPaginas);
    }

    public static int leer(String parametro) {
        try {
            return Math.max(1, Integer.parseInt(parametro));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public int getNumero() {
        return numero;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public int getTamano() {
        return TAMANO;
    }

    public int getOffset() {
        return (numero - 1) * TAMANO;
    }

    public boolean isHayAnterior() {
        return numero > 1;
    }

    public boolean isHaySiguiente() {
        return numero < totalPaginas;
    }

    public int getAnterior() {
        return numero - 1;
    }

    public int getSiguiente() {
        return numero + 1;
    }
}
