package co.iudigital.mercafacil.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Encabezado de compra o venta con los nombres de sus llaves foráneas. */
public class MovimientoListado {

    private final String numFactura;
    private final LocalDate fecha;
    private final BigDecimal valorTotal;
    private final String tipoPago;
    private final String idTercero;
    private final String nombreTercero;
    private final String nombreAlmacen;

    public MovimientoListado(String numFactura, LocalDate fecha, BigDecimal valorTotal, String tipoPago,
                             String idTercero, String nombreTercero, String nombreAlmacen) {
        this.numFactura = numFactura;
        this.fecha = fecha;
        this.valorTotal = valorTotal;
        this.tipoPago = tipoPago;
        this.idTercero = idTercero;
        this.nombreTercero = nombreTercero;
        this.nombreAlmacen = nombreAlmacen;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public String getIdTercero() {
        return idTercero;
    }

    public String getNombreTercero() {
        return nombreTercero;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }
}
