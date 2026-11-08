package co.iudigital.mercafacil.model;

import java.math.BigDecimal;

public class ArticuloPrecio {

    private String codArticulo;
    private String codTipoPrecio;
    private BigDecimal valor;

    public ArticuloPrecio() {
    }

    public ArticuloPrecio(String codArticulo, String codTipoPrecio, BigDecimal valor) {
        this.codArticulo = codArticulo;
        this.codTipoPrecio = codTipoPrecio;
        this.valor = valor;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getCodTipoPrecio() {
        return codTipoPrecio;
    }

    public void setCodTipoPrecio(String codTipoPrecio) {
        this.codTipoPrecio = codTipoPrecio;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "ArticuloPrecio{codArticulo=" + codArticulo
                + ", codTipoPrecio=" + codTipoPrecio
                + ", valor=" + valor
                + "}";
    }
}
