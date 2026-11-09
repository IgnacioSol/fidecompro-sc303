package fidecompro.modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final double IVA = 0.13;
    public static final String CARPETA = "facturas";

    private int numero;
    private LocalDate fecha;
    private Cliente cliente;
    private Usuario cajero;
    private ArrayList<DetalleFactura> detalles;

    public Factura(Usuario cajero) {
        this.cajero = cajero;
        this.fecha = LocalDate.now();
        this.detalles = new ArrayList<DetalleFactura>();
    }

    // Si el producto ya está en el detalle, solo se le suma la cantidad
    public void agregarDetalle(DetalleFactura nuevo) {
        for (DetalleFactura d : detalles) {
            if (d.getProducto().getCodigo().equals(nuevo.getProducto().getCodigo())) {
                d.setCantidad(d.getCantidad() + nuevo.getCantidad());
                return;
            }
        }
        detalles.add(nuevo);
    }

    public void quitarDetalle(int indice) {
        detalles.remove(indice);
    }

    // Cuántas unidades de un producto lleva ya la factura
    public int getCantidadDe(String codigo) {
        for (DetalleFactura d : detalles) {
            if (d.getProducto().getCodigo().equals(codigo)) {
                return d.getCantidad();
            }
        }
        return 0;
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetalleFactura d : detalles) {
            subtotal = subtotal + d.calcularSubtotal();
        }
        return subtotal;
    }

    public double calcularImpuesto() {
        return calcularSubtotal() * IVA;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularImpuesto();
    }

    public String getNombreArchivo() {
        return CARPETA + File.separator + "factura-" + String.format("%04d", numero) + ".txt";
    }

    // Crea la "factura física": un .txt con el desglose de pago
    public void generarArchivo() throws IOException {
        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        PrintWriter escritor = new PrintWriter(new FileWriter(getNombreArchivo()));
        String linea = "------------------------------------------------------------------";

        escritor.println("FIDECOMPRO - Venta al por mayor");
        escritor.println("Factura No. " + String.format("%04d", numero));
        escritor.println("Fecha: " + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        escritor.println(linea);

        // Cliente y cajero se tratan como Persona: cada uno imprime sus propios datos
        Persona[] personas = {cliente, cajero};
        for (Persona p : personas) {
            escritor.println(p.getDatosFactura());
        }
        escritor.println(linea);

        escritor.println(String.format("%-8s %-22s %6s %12s %13s", "Código", "Producto", "Cant.", "Precio", "Subtotal"));
        for (DetalleFactura d : detalles) {
            escritor.println(String.format("%-8s %-22s %6d %12.2f %13.2f",
                    d.getProducto().getCodigo(),
                    d.getProducto().getNombre(),
                    d.getCantidad(),
                    d.getPrecioUnitario(),
                    d.calcularSubtotal()));
        }
        escritor.println(linea);
        escritor.println(String.format("%51s %13.2f", "Subtotal:", calcularSubtotal()));
        escritor.println(String.format("%51s %13.2f", "IVA (13%):", calcularImpuesto()));
        escritor.println(String.format("%51s %13.2f", "TOTAL:", calcularTotal()));
        escritor.println();
        escritor.println("Gracias por su compra");
        escritor.close();
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getCajero() {
        return cajero;
    }

    public ArrayList<DetalleFactura> getDetalles() {
        return detalles;
    }
}
