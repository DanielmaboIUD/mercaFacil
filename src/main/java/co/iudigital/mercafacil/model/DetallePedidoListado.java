package co.iudigital.mercafacil.model;

/** Línea de pedido con el nombre del artículo. */
public class DetallePedidoListado extends DetallePedido {

    private final String nombreArticulo;

    public DetallePedidoListado(String numPedido, String codArticulo, int cantidad, String nombreArticulo) {
        super(numPedido, codArticulo, cantidad);
        this.nombreArticulo = nombreArticulo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }
}
