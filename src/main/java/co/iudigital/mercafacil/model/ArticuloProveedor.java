package co.iudigital.mercafacil.model;

import java.math.BigDecimal;

public class ArticuloProveedor {

    private String codArticulo;
    private String nit;
    private BigDecimal precioCompra;

    public ArticuloProveedor() {
    }

    public ArticuloProveedor(String codArticulo, String nit, BigDecimal precioCompra) {
        this.codArticulo = codArticulo;
        this.nit = nit;
        this.precioCompra = precioCompra;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    @Override
    public String toString() {
        return "ArticuloProveedor{codArticulo=" + codArticulo
                + ", nit=" + nit
                + ", precioCompra=" + precioCompra
                + "}";
    }
}
