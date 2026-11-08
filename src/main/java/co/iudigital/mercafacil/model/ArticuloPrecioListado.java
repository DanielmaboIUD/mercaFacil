package co.iudigital.mercafacil.model;

import java.math.BigDecimal;

/** Registro con los nombres de sus llaves foráneas, para los listados. */
public class ArticuloPrecioListado extends ArticuloPrecio {

    private final String nombreArticulo;
    private final String nombreSegundo;

    public ArticuloPrecioListado(String codArticulo, String codTipoPrecio, BigDecimal valor,
                      String nombreArticulo, String nombreSegundo) {
        super(codArticulo, codTipoPrecio, valor);
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
