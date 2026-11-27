package co.iudigital.mercafacil.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Las anotaciones solo las lee JPA; el DAO JDBC y los JSP no se ven afectados.
@Entity
@Table(name = "cliente")
public class Cliente {

    // Sin @GeneratedValue: la cédula la digita el usuario.
    @Id
    @Column(name = "cedula", length = 12)
    private String cedula;

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 120)
    private String direccion;

    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    public Cliente() {
    }

    public Cliente(String cedula, String nombre, String direccion, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Cliente{cedula=" + cedula
                + ", nombre=" + nombre
                + ", direccion=" + direccion
                + ", telefono=" + telefono
                + "}";
    }
}
