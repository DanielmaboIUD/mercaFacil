package co.iudigital.mercafacil.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Venta {

    private String numFactura;
    private LocalDate fecha;
    private BigDecimal valorTotal;
    private String codTipoPago;
    private String cedula;
    private String codAlmacen;

    public Venta() {
    }

    public Venta(String numFactura, LocalDate fecha, BigDecimal valorTotal, String codTipoPago, String cedula, String codAlmacen) {
        this.numFactura = numFactura;
        this.fecha = fecha;
        this.valorTotal = valorTotal;
        this.codTipoPago = codTipoPago;
        this.cedula = cedula;
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    @Override
    public String toString() {
        return "Venta{numFactura=" + numFactura
                + ", fecha=" + fecha
                + ", valorTotal=" + valorTotal
                + ", codTipoPago=" + codTipoPago
                + ", cedula=" + cedula
                + ", codAlmacen=" + codAlmacen
                + "}";
    }
}
