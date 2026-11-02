package fidecompro.modelo;

import fidecompro.excepciones.ExistenciasInsuficientesException;
import java.io.Serializable;

public class Producto implements Serializable, Comparable<Producto> {
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nombre;
    private String tipo;
    private double precio;
    private int existencias;

    public Producto(String codigo, String nombre, String tipo, double precio, int existencias) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.existencias = existencias;
    }

    public boolean hayExistencias(int cantidad) {
        return existencias >= cantidad;
    }

    public void restarExistencias(int cantidad) throws ExistenciasInsuficientesException {
        if (!hayExistencias(cantidad)) {
            throw new ExistenciasInsuficientesException("Solo quedan " + existencias + " unidades de " + nombre);
        }
        existencias = existencias - cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    // Para ordenar los productos por código
    @Override
    public int compareTo(Producto otro) {
        return codigo.compareTo(otro.getCodigo());
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
