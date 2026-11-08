package co.iudigital.mercafacil.model;

import java.math.BigDecimal;

/** Registro con los nombres de sus llaves foráneas, para los listados. */
public class ArticuloProveedorListado extends ArticuloProveedor {

    private final String nombreArticulo;
    private final String nombreSegundo;

    public ArticuloProveedorListado(String codArticulo, String nit, BigDecimal precioCompra,
                      String nombreArticulo, String nombreSegundo) {
        super(codArticulo, nit, precioCompra);
        this.nombreArticulo = nombreArticulo;
        this.nombreSegundo = nombreSegundo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public String getNombreSegundo() {
        return nombreSegundo;
    }
}
