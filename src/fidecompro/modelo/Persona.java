package fidecompro.modelo;

import java.io.Serializable;

// Clase padre de Usuario y Cliente: los dos son personas con un nombre
public abstract class Persona implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;

    public Persona(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Cada tipo de persona se identifica distinto (usuario o cédula)
    public abstract String getIdentificacion();

    // Lo que se imprime de esta persona en el archivo de la factura
    public abstract String getDatosFactura();

    @Override
    public String toString() {
        return getIdentificacion() + " - " + nombre;
    }
}
