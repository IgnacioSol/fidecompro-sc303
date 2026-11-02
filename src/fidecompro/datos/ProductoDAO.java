package fidecompro.datos;

import fidecompro.excepciones.ExistenciasInsuficientesException;
import fidecompro.excepciones.RegistroDuplicadoException;
import fidecompro.excepciones.RegistroEnUsoException;
import fidecompro.excepciones.RegistroNoEncontradoException;
import fidecompro.modelo.DetalleFactura;
import fidecompro.modelo.Producto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

public class ProductoDAO {
    private static final String RUTA = "productos.dat";

    @SuppressWarnings("unchecked")
    public ArrayList<Producto> consultar() throws IOException {
        ArrayList<Producto> productos = (ArrayList<Producto>) Archivo.leer(RUTA);
        if (productos == null) {
            productos = new ArrayList<Producto>();
        }
        Collections.sort(productos);
        return productos;
    }

    public ArrayList<Producto> consultarPorTipo(String tipo) throws IOException {
        ArrayList<Producto> filtrados = new ArrayList<Producto>();
        for (Producto p : consultar()) {
            if (p.getTipo().equalsIgnoreCase(tipo)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    // TreeSet no deja repetidos y los ordena solo: sirve para el combo de tipos
    public TreeSet<String> consultarTipos() throws IOException {
        TreeSet<String> tipos = new TreeSet<String>();
        for (Producto p : consultar()) {
            tipos.add(p.getTipo());
        }
        return tipos;
    }

    public Producto buscar(String codigo) throws IOException, RegistroNoEncontradoException {
        for (Producto p : consultar()) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        throw new RegistroNoEncontradoException("No existe un producto con el código " + codigo);
    }

    public void insertar(Producto producto) throws IOException, RegistroDuplicadoException {
        ArrayList<Producto> productos = consultar();
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                throw new RegistroDuplicadoException("Ya existe un producto con el código " + producto.getCodigo());
            }
        }
        productos.add(producto);
        Archivo.guardar(RUTA, productos);
    }

    public void actualizar(Producto producto) throws IOException, RegistroNoEncontradoException {
        ArrayList<Producto> productos = consultar();
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                productos.set(i, producto);
                Archivo.guardar(RUTA, productos);
                return;
            }
        }
        throw new RegistroNoEncontradoException("No existe un producto con el código " + producto.getCodigo());
    }

    // Solo se elimina si el producto no aparece en ninguna factura
    public void eliminar(String codigo) throws IOException, RegistroNoEncontradoException, RegistroEnUsoException {
        FacturaDAO facturaDAO = new FacturaDAO();
        if (facturaDAO.productoEnFacturas(codigo)) {
            throw new RegistroEnUsoException("El producto " + codigo + " aparece en una factura y no se puede eliminar");
        }

        ArrayList<Producto> productos = consultar();
        Producto encontrado = null;
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                encontrado = p;
            }
        }
        if (encontrado == null) {
            throw new RegistroNoEncontradoException("No existe un producto con el código " + codigo);
        }
        productos.remove(encontrado);
        Archivo.guardar(RUTA, productos);
    }

    // Descuenta del inventario lo vendido en una factura.
    // Primero revisa todas las líneas y solo si alcanza para todas, resta y guarda.
    public void descontarExistencias(ArrayList<DetalleFactura> detalles)
            throws IOException, RegistroNoEncontradoException, ExistenciasInsuficientesException {
        ArrayList<Producto> productos = consultar();
        HashMap<String, Producto> porCodigo = new HashMap<String, Producto>();
        for (Producto p : productos) {
            porCodigo.put(p.getCodigo(), p);
        }

        for (DetalleFactura d : detalles) {
            Producto p = porCodigo.get(d.getProducto().getCodigo());
            if (p == null) {
                throw new RegistroNoEncontradoException("El producto " + d.getProducto().getCodigo() + " ya no existe");
            }
            if (!p.hayExistencias(d.getCantidad())) {
                throw new ExistenciasInsuficientesException("Solo quedan " + p.getExistencias() + " unidades de " + p.getNombre());
            }
        }

        for (DetalleFactura d : detalles) {
            porCodigo.get(d.getProducto().getCodigo()).restarExistencias(d.getCantidad());
        }
        Archivo.guardar(RUTA, productos);
    }
}
