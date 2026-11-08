package co.iudigital.mercafacil.model;

/** Artículo con las descripciones de su unidad y grupo, para los listados. */
public class ArticuloListado extends Articulo {

    private final String descripcionUnidad;
    private final String descripcionGrupo;

    public ArticuloListado(String codArticulo, String nombre, String codUnidad, String codGrupo,
                           String descripcionUnidad, String descripcionGrupo) {
        super(codArticulo, nombre, codUnidad, codGrupo);
        this.descripcionUnidad = descripcionUnidad;
        this.descripcionGrupo = descripcionGrupo;
    }

    public String getDescripcionUnidad() {
        return descripcionUnidad;
    }

    public String getDescripcionGrupo() {
        return descripcionGrupo;
    }
}
