package co.iudigital.mercafacil.model;

public class TipoPago {

    private String codTipoPago;
    private String descripcion;

    public TipoPago() {
    }

    public TipoPago(String codTipoPago, String descripcion) {
        this.codTipoPago = codTipoPago;
        this.descripcion = descripcion;
    }

    public String getCodTipoPago() {
        return codTipoPago;
    }

    public void setCodTipoPago(String codTipoPago) {
        this.codTipoPago = codTipoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoPago{codTipoPago=" + codTipoPago
                + ", descripcion=" + descripcion
                + "}";
    }
}
