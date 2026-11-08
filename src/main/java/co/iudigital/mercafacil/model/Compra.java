package co.iudigital.mercafacil.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Compra {

    private String numFactura;
    private LocalDate fecha;
    private BigDecimal valorTotal;
    private String codTipoPago;
    private String nit;
    private String codAlmacen;

    public Compra() {
    }

    public Compra(String numFactura, LocalDate fecha, BigDecimal valorTotal, String codTipoPago, String nit, String codAlmacen) {
        this.numFactura = numFactura;
        this.fecha = fecha;
        this.valorTotal = valorTotal;
        this.codTipoPago = codTipoPago;
        this.nit = nit;
        this.codAlmacen = codAlmacen;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getCodTipoPago() {
        return codTipoPago;
    }

    public void setCodTipoPago(String codTipoPago) {
        this.codTipoPago = codTipoPago;
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
        return "Compra{numFactura=" + numFactura
                + ", fecha=" + fecha
                + ", valorTotal=" + valorTotal
                + ", codTipoPago=" + codTipoPago
                + ", nit=" + nit
                + ", codAlmacen=" + codAlmacen
                + "}";
    }
}
