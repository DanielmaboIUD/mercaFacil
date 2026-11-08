package co.iudigital.mercafacil.model;

/** Inventario con los nombres de su almacén y artículo, para los listados. */
public class InventarioListado extends Inventario {

    private final String nombreAlmacen;
    private final String nombreArticulo;

    public InventarioListado(String codAlmacen, String codArticulo, int existencia, int cantidadMaxima,
                             int cantidadMinima, String nombreAlmacen, String nombreArticulo) {
        super(codAlmacen, codArticulo, existencia, cantidadMaxima, cantidadMinima);
        this.nombreAlmacen = nombreAlmacen;
        this.nombreArticulo = nombreArticulo;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }
}
