package co.iudigital.mercafacil.model;

public class UnidadMedida {

    private String codUnidad;
    private String descripcion;

    public UnidadMedida() {
    }

    public UnidadMedida(String codUnidad, String descripcion) {
        this.codUnidad = codUnidad;
        this.descripcion = descripcion;
    }

    public String getCodUnidad() {
        return codUnidad;
    }

    public void setCodUnidad(String codUnidad) {
        this.codUnidad = codUnidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "UnidadMedida{codUnidad=" + codUnidad
                + ", descripcion=" + descripcion
                + "}";
    }
}
