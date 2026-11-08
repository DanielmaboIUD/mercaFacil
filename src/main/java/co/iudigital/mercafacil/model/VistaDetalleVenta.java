package co.iudigital.mercafacil.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Fila de la vista v_detalle_ventas (solo lectura). */
public class VistaDetalleVenta {

    private final String numFactura;
    private final LocalDate fecha;
    private final String tipoPago;
    private final String cedula;
    private final String cliente;
    private final String almacen;
    private final String codArticulo;
    private final String articulo;
    private final String grupo;
    private final String unidad;
    private final int cantidad;
    private final BigDecimal valorUnitario;
    private final BigDecimal subtotal;

    public VistaDetalleVenta(String numFactura, LocalDate fecha, String tipoPago, String cedula, String cliente,
                             String almacen, String codArticulo, String articulo, String grupo, String unidad,
                             int cantidad, BigDecimal valorUnitario, BigDecimal subtotal) {
        this.numFactura = numFactura;
        this.fecha = fecha;
        this.tipoPago = tipoPago;
        this.cedula = cedula;
        this.cliente = cliente;
        this.almacen = almacen;
        this.codArticulo = codArticulo;
        this.articulo = articulo;
        this.grupo = grupo;
        this.unidad = unidad;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.subtotal = subtotal;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public String getCedula() {
        return cedula;
    }

    public String getCliente() {
        return cliente;
    }

    public String getAlmacen() {
        return almacen;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public String getArticulo() {
        return articulo;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getUnidad() {
        return unidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
