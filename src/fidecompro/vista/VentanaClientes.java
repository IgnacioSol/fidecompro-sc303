package fidecompro.vista;

import fidecompro.datos.ClienteDAO;
import fidecompro.excepciones.DatoInvalidoException;
import fidecompro.excepciones.RegistroDuplicadoException;
import fidecompro.excepciones.RegistroEnUsoException;
import fidecompro.excepciones.RegistroNoEncontradoException;
import fidecompro.modelo.Cliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class VentanaClientes extends JFrame {
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtBuscar;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;
    private JButton btnVerTodos;
    private JTable tblClientes;
    private DefaultTableModel modelo;

    private ClienteDAO clienteDAO = new ClienteDAO();

    public VentanaClientes() {
        setTitle("Fidecompro - Clientes");
        setSize(660, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(20, 20, 80, 25);
        add(lblCedula);
        txtCedula = new JTextField();
        txtCedula.setBounds(100, 20, 180, 25);
        add(txtCedula);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 55, 80, 25);
        add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(100, 55, 180, 25);
        add(txtNombre);

        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setBounds(20, 90, 80, 25);
        add(lblDireccion);
        txtDireccion = new JTextField();
        txtDireccion.setBounds(100, 90, 180, 25);
        add(txtDireccion);

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(330, 20, 80, 25);
        add(lblTelefono);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(420, 20, 190, 25);
        add(txtTelefono);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(330, 55, 80, 25);
        add(lblCorreo);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(420, 55, 190, 25);
        add(txtCorreo);

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(20, 135, 100, 30);
        add(btnAgregar);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(130, 135, 100, 30);
        add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(240, 135, 100, 30);
        add(btnEliminar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(350, 135, 100, 30);
        add(btnLimpiar);

        JLabel lblBuscar = new JLabel("Buscar por cédula:");
        lblBuscar.setBounds(20, 180, 130, 25);
        add(lblBuscar);
        txtBuscar = new JTextField();
        txtBuscar.setBounds(150, 180, 150, 25);
        add(txtBuscar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(310, 178, 90, 30);
        add(btnBuscar);

        btnVerTodos = new JButton("Ver todos");
        btnVerTodos.setBounds(410, 178, 100, 30);
        add(btnVerTodos);

        // Tabla de solo lectura: los cambios se hacen con los campos de arriba
        modelo = new DefaultTableModel(new Object[]{"Cédula", "Nombre", "Teléfono", "Correo"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblClientes = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tblClientes);
        scroll.setBounds(20, 220, 610, 200);
        add(scroll);

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregar();
            }
        });

        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificar();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });

        btnVerTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtBuscar.setText("");
                llenarTabla();
            }
        });

        tblClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarSeleccionado();
            }
        });

        llenarTabla();
    }

    private void llenarTabla() {
        try {
            mostrarEnTabla(clienteDAO.consultar());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer los clientes: " + ex.getMessage());
        }
    }

    private void mostrarEnTabla(ArrayList<Cliente> clientes) {
        modelo.setRowCount(0);
        for (Cliente c : clientes) {
            modelo.addRow(new Object[]{c.getCedula(), c.getNombre(), c.getTelefono(), c.getCorreo()});
        }
    }

    // Lee los campos y arma el cliente, validando lo básico
    private Cliente leerFormulario() throws DatoInvalidoException {
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();

        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
            throw new DatoInvalidoException("La cédula, el nombre y el teléfono son obligatorios");
        }
        if (!correo.isEmpty() && !correo.contains("@")) {
            throw new DatoInvalidoException("El correo no es válido");
        }
        return new Cliente(cedula, nombre, telefono, correo, direccion);
    }

    private void agregar() {
        try {
            Cliente cliente = leerFormulario();
            clienteDAO.insertar(cliente);
            JOptionPane.showMessageDialog(this, "Cliente " + cliente.getNombre() + " agregado");
            limpiar();
            llenarTabla();
        } catch (DatoInvalidoException | RegistroDuplicadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void modificar() {
        // Si la cédula se puede editar es porque no se ha escogido nadie de la tabla
        if (txtCedula.isEditable()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla");
            return;
        }
        try {
            Cliente cliente = leerFormulario();
            clienteDAO.actualizar(cliente);
            JOptionPane.showMessageDialog(this, "Cliente " + cliente.getNombre() + " modificado");
            limpiar();
            llenarTabla();
        } catch (DatoInvalidoException | RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (txtCedula.isEditable()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla");
            return;
        }
        String cedula = txtCedula.getText();
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Eliminar al cliente " + cedula + "?", "Eliminar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            clienteDAO.eliminar(cedula);
            JOptionPane.showMessageDialog(this, "Cliente eliminado");
            limpiar();
            llenarTabla();
        } catch (RegistroNoEncontradoException | RegistroEnUsoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void buscar() {
        String cedula = txtBuscar.getText().trim();
        if (cedula.isEmpty()) {
            llenarTabla();
            return;
        }
        try {
            ArrayList<Cliente> resultado = new ArrayList<Cliente>();
            resultado.add(clienteDAO.buscar(cedula));
            mostrarEnTabla(resultado);
        } catch (RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer los clientes: " + ex.getMessage());
        }
    }

    private void cargarSeleccionado() {
        int fila = tblClientes.getSelectedRow();
        if (fila == -1) {
            return;
        }
        String cedula = modelo.getValueAt(fila, 0).toString();
        try {
            Cliente c = clienteDAO.buscar(cedula);
            txtCedula.setText(c.getCedula());
            txtNombre.setText(c.getNombre());
            txtTelefono.setText(c.getTelefono());
            txtCorreo.setText(c.getCorreo());
            txtDireccion.setText(c.getDireccion());
            // La cédula no se cambia porque es la que identifica al cliente
            txtCedula.setEditable(false);
        } catch (RegistroNoEncontradoException | IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void limpiar() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtCedula.setEditable(true);
        tblClientes.clearSelection();
    }
}
