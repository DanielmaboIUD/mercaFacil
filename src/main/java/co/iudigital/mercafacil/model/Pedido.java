package co.iudigital.mercafacil.model;

import java.time.LocalDate;

public class Pedido {

    private String numPedido;
    private LocalDate fecha;
    private String nit;
    private String codAlmacen;

    public Pedido() {
    }

    public Pedido(String numPedido, LocalDate fecha, String nit, String codAlmacen) {
        this.numPedido = numPedido;
        this.fecha = fecha;
        this.nit = nit;
        this.codAlmacen = codAlmacen;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    @Override
    public String toString() {
        return "Pedido{numPedido=" + numPedido
                + ", fecha=" + fecha
                + ", nit=" + nit
                + ", codAlmacen=" + codAlmacen
                + "}";
    }
}
