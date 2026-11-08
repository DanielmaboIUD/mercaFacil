package co.iudigital.mercafacil.model;

public class Articulo {

    private String codArticulo;
    private String nombre;
    private String codUnidad;
    private String codGrupo;

    public Articulo() {
    }

    public Articulo(String codArticulo, String nombre, String codUnidad, String codGrupo) {
        this.codArticulo = codArticulo;
        this.nombre = nombre;
        this.codUnidad = codUnidad;
        this.codGrupo = codGrupo;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodUnidad() {
        return codUnidad;
    }

    public void setCodUnidad(String codUnidad) {
        this.codUnidad = codUnidad;
    }

    public String getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(String codGrupo) {
        this.codGrupo = codGrupo;
    }

    @Override
    public String toString() {
        return "Articulo{codArticulo=" + codArticulo
                + ", nombre=" + nombre
                + ", codUnidad=" + codUnidad
                + ", codGrupo=" + codGrupo
                + "}";
    }
}
