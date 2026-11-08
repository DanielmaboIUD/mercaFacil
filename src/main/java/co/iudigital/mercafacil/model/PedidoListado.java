package co.iudigital.mercafacil.model;

import java.time.LocalDate;

/** Pedido con los nombres de su proveedor y almacén, para los listados. */
public class PedidoListado extends Pedido {

    private final String nombreProveedor;
    private final String nombreAlmacen;

    public PedidoListado(String numPedido, LocalDate fecha, String nit, String codAlmacen,
                         String nombreProveedor, String nombreAlmacen) {
        super(numPedido, fecha, nit, codAlmacen);
        this.nombreProveedor = nombreProveedor;
        this.nombreAlmacen = nombreAlmacen;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }
}
