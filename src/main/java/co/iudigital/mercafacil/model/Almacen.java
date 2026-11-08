package co.iudigital.mercafacil.model;

public class Almacen {

    private String codAlmacen;
    private String nombre;
    private String direccion;
    private String nombreGerente;

    public Almacen() {
    }

    public Almacen(String codAlmacen, String nombre, String direccion, String nombreGerente) {
        this.codAlmacen = codAlmacen;
        this.nombre = nombre;
        this.direccion = direccion;
        this.nombreGerente = nombreGerente;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreGerente() {
        return nombreGerente;
    }

    public void setNombreGerente(String nombreGerente) {
        this.nombreGerente = nombreGerente;
    }

    @Override
    public String toString() {
        return "Almacen{codAlmacen=" + codAlmacen
                + ", nombre=" + nombre
                + ", direccion=" + direccion
                + ", nombreGerente=" + nombreGerente
                + "}";
    }
}
