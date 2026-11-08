package co.iudigital.mercafacil.model;

public class Inventario {

    private String codAlmacen;
    private String codArticulo;
    private int existencia;
    private int cantidadMaxima;
    private int cantidadMinima;

    public Inventario() {
    }

    public Inventario(String codAlmacen, String codArticulo, int existencia, int cantidadMaxima, int cantidadMinima) {
        this.codAlmacen = codAlmacen;
        this.codArticulo = codArticulo;
        this.existencia = existencia;
        this.cantidadMaxima = cantidadMaxima;
        this.cantidadMinima = cantidadMinima;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public int getCantidadMaxima() {
        return cantidadMaxima;
    }

    public void setCantidadMaxima(int cantidadMaxima) {
        this.cantidadMaxima = cantidadMaxima;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    @Override
    public String toString() {
        return "Inventario{codAlmacen=" + codAlmacen
                + ", codArticulo=" + codArticulo
                + ", existencia=" + existencia
                + ", cantidadMaxima=" + cantidadMaxima
                + ", cantidadMinima=" + cantidadMinima
                + "}";
    }
}
