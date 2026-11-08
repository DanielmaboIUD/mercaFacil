package co.iudigital.mercafacil.model;

import java.math.BigDecimal;

/** Línea de compra o venta con el nombre del artículo. */
public class DetalleListado {

    private final String codArticulo;
    private final String nombreArticulo;
    private final int cantidad;
    private final BigDecimal valorUnitario;

    public DetalleListado(String codArticulo, String nombreArticulo, int cantidad, BigDecimal valorUnitario) {
        this.codArticulo = codArticulo;
        this.nombreArticulo = nombreArticulo;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public BigDecimal getSubtotal() {
        return valorUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
