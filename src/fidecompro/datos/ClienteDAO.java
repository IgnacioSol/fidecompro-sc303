package fidecompro.datos;

import fidecompro.excepciones.RegistroDuplicadoException;
import fidecompro.excepciones.RegistroEnUsoException;
import fidecompro.excepciones.RegistroNoEncontradoException;
import fidecompro.modelo.Cliente;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ClienteDAO {
    private static final String RUTA = "clientes.dat";

    @SuppressWarnings("unchecked")
    public ArrayList<Cliente> consultar() throws IOException {
        ArrayList<Cliente> clientes = (ArrayList<Cliente>) Archivo.leer(RUTA);
        if (clientes == null) {
            clientes = new ArrayList<Cliente>();
        }
        Collections.sort(clientes);
        return clientes;
    }

    public Cliente buscar(String cedula) throws IOException, RegistroNoEncontradoException {
        for (Cliente c : consultar()) {
            if (c.getCedula().equals(cedula)) {
                return c;
            }
        }
        throw new RegistroNoEncontradoException("No existe un cliente con la cédula " + cedula);
    }

    public void insertar(Cliente cliente) throws IOException, RegistroDuplicadoException {
        ArrayList<Cliente> clientes = consultar();
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cliente.getCedula())) {
                throw new RegistroDuplicadoException("Ya existe un cliente con la cédula " + cliente.getCedula());
            }
        }
        clientes.add(cliente);
        Archivo.guardar(RUTA, clientes);
    }

    public void actualizar(Cliente cliente) throws IOException, RegistroNoEncontradoException {
        ArrayList<Cliente> clientes = consultar();
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getCedula().equals(cliente.getCedula())) {
                clientes.set(i, cliente);
                Archivo.guardar(RUTA, clientes);
                return;
            }
        }
        throw new RegistroNoEncontradoException("No existe un cliente con la cédula " + cliente.getCedula());
    }

    // Solo se elimina si el cliente no tiene facturas, para no perder el historial
    public void eliminar(String cedula) throws IOException, RegistroNoEncontradoException, RegistroEnUsoException {
        FacturaDAO facturaDAO = new FacturaDAO();
        if (facturaDAO.clienteTieneFacturas(cedula)) {
            throw new RegistroEnUsoException("El cliente " + cedula + " tiene facturas registradas y no se puede eliminar");
        }

        ArrayList<Cliente> clientes = consultar();
        Cliente encontrado = null;
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                encontrado = c;
            }
        }
        if (encontrado == null) {
            throw new RegistroNoEncontradoException("No existe un cliente con la cédula " + cedula);
        }
        clientes.remove(encontrado);
        Archivo.guardar(RUTA, clientes);
    }
}
