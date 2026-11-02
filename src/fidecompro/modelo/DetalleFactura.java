package fidecompro.modelo;

import java.io.Serializable;

public class DetalleFactura implements Serializable {
    private static final long serialVersionUID = 1L;

    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public DetalleFactura(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        // Se guarda el precio del momento, por si después cambia en el inventario
        this.precioUnitario = producto.getPrecio();
    }

    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }
}
