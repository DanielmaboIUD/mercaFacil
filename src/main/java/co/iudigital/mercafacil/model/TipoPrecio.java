package co.iudigital.mercafacil.model;

public class TipoPrecio {

    private String codTipoPrecio;
    private String descripcion;

    public TipoPrecio() {
    }

    public TipoPrecio(String codTipoPrecio, String descripcion) {
        this.codTipoPrecio = codTipoPrecio;
        this.descripcion = descripcion;
    }

    public String getCodTipoPrecio() {
        return codTipoPrecio;
    }

    public void setCodTipoPrecio(String codTipoPrecio) {
        this.codTipoPrecio = codTipoPrecio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoPrecio{codTipoPrecio=" + codTipoPrecio
                + ", descripcion=" + descripcion
                + "}";
    }
}
