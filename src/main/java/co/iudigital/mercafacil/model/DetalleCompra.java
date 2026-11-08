package co.iudigital.mercafacil.model;

import java.math.BigDecimal;

public class DetalleCompra {

    private String numFactura;
    private String codArticulo;
    private int cantidad;
    private BigDecimal valorUnitario;

    public DetalleCompra() {
    }

    public DetalleCompra(String numFactura, String codArticulo, int cantidad, BigDecimal valorUnitario) {
        this.numFactura = numFactura;
        this.codArticulo = codArticulo;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    @Override
    public String toString() {
        return "DetalleCompra{numFactura=" + numFactura
                + ", codArticulo=" + codArticulo
                + ", cantidad=" + cantidad
                + ", valorUnitario=" + valorUnitario
                + "}";
    }
}
