package co.iudigital.mercafacil.model;

public class DetallePedido {

    private String numPedido;
    private String codArticulo;
    private int cantidad;

    public DetallePedido() {
    }

    public DetallePedido(String numPedido, String codArticulo, int cantidad) {
        this.numPedido = numPedido;
        this.codArticulo = codArticulo;
        this.cantidad = cantidad;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
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

    @Override
    public String toString() {
        return "DetallePedido{numPedido=" + numPedido
                + ", codArticulo=" + codArticulo
                + ", cantidad=" + cantidad
                + "}";
    }
}
