package co.iudigital.mercafacil.model;

public class Grupo {

    private String codGrupo;
    private String descripcion;

    public Grupo() {
    }

    public Grupo(String codGrupo, String descripcion) {
        this.codGrupo = codGrupo;
        this.descripcion = descripcion;
    }

    public String getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(String codGrupo) {
        this.codGrupo = codGrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Grupo{codGrupo=" + codGrupo
                + ", descripcion=" + descripcion
                + "}";
    }
}
