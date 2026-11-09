package fidecompro.datos;

import fidecompro.modelo.DetalleFactura;
import fidecompro.modelo.Factura;
import java.io.IOException;
import java.util.ArrayList;

public class FacturaDAO {
    private static final String RUTA = "facturas.dat";

    @SuppressWarnings("unchecked")
    public ArrayList<Factura> consultar() throws IOException {
        ArrayList<Factura> facturas = (ArrayList<Factura>) Archivo.leer(RUTA);
        if (facturas == null) {
            facturas = new ArrayList<Factura>();
        }
        return facturas;
    }

    public int siguienteNumero() throws IOException {
        int mayor = 0;
        for (Factura f : consultar()) {
            if (f.getNumero() > mayor) {
                mayor = f.getNumero();
            }
        }
        return mayor + 1;
    }

    // Le asigna el número a la factura y la guarda
    public void insertar(Factura factura) throws IOException {
        ArrayList<Factura> facturas = consultar();
        factura.setNumero(siguienteNumero());
        facturas.add(factura);
        Archivo.guardar(RUTA, facturas);
    }

    public boolean clienteTieneFacturas(String cedula) throws IOException {
        for (Factura f : consultar()) {
            if (f.getCliente().getCedula().equals(cedula)) {
                return true;
            }
        }
        return false;
    }

    public boolean productoEnFacturas(String codigo) throws IOException {
        for (Factura f : consultar()) {
            for (DetalleFactura d : f.getDetalles()) {
                if (d.getProducto().getCodigo().equalsIgnoreCase(codigo)) {
                    return true;
                }
            }
        }
        return false;
    }
}
