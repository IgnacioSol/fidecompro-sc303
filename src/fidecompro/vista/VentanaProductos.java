package fidecompro.vista;

import fidecompro.datos.ProductoDAO;
import fidecompro.excepciones.DatoInvalidoException;
import fidecompro.excepciones.RegistroDuplicadoException;
import fidecompro.excepciones.RegistroEnUsoException;
import fidecompro.excepciones.RegistroNoEncontradoException;
import fidecompro.modelo.Producto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class VentanaProductos extends JFrame {
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtTipo;
    private JTextField txtPrecio;
    private JTextField txtExistencias;
    private JComboBox<String> cboFiltroTipo;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JTable tblProductos;
    private DefaultTableModel modelo;

    private ProductoDAO productoDAO = new ProductoDAO();
    // Evita que el combo filtre mientras se están cargando sus opciones
    private boolean cargandoTipos = false;

    public VentanaProductos() {
        setTitle("Fidecompro - Productos");
        setSize(660, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(20, 20, 80, 25);
        add(lblCodigo);
        txtCodigo = new JTextField();
        txtCodigo.setBounds(100, 20, 180, 25);
        add(txtCodigo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 55, 80, 25);
        add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(100, 55, 180, 25);
        add(txtNombre);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 90, 80, 25);
        add(lblTipo);
        txtTipo = new JTextField();
        txtTipo.setBounds(100, 90, 180, 25);
        add(txtTipo);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(330, 20, 90, 25);
        add(lblPrecio);
        txtPrecio = new JTextField();
        txtPrecio.setBounds(420, 20, 190, 25);
        add(txtPrecio);

        JLabel lblExistencias = new JLabel("Existencias:");
        lblExistencias.setBounds(330, 55, 90, 25);
        add(lblExistencias);
        txtExistencias = new JTextField();
        txtExistencias.setBounds(420, 55, 190, 25);
        add(txtExistencias);

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

        JLabel lblFiltro = new JLabel("Filtrar por tipo:");
        lblFiltro.setBounds(20, 180, 120, 25);
        add(lblFiltro);
        cboFiltroTipo = new JComboBox<String>();
        cboFiltroTipo.setBounds(140, 180, 180, 25);
        add(cboFiltroTipo);

        modelo = new DefaultTableModel(new Object[]{"Código", "Nombre", "Tipo", "Precio", "Existencias"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblProductos = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tblProductos);
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

        cboFiltroTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!cargandoTipos) {
                    llenarTabla();
                }
            }
        });

        tblProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarSeleccionado();
            }
        });

        cargarTipos();
        llenarTabla();
    }

    // Llena el combo con "Todos" y los tipos que existen en el inventario
    private void cargarTipos() {
        cargandoTipos = true;
        Object seleccionado = cboFiltroTipo.getSelectedItem();
        cboFiltroTipo.removeAllItems();
        cboFiltroTipo.addItem("Todos");
        try {
            for (String tipo : productoDAO.consultarTipos()) {
                cboFiltroTipo.addItem(tipo);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer los productos: " + ex.getMessage());
        }
        if (seleccionado != null) {
            cboFiltroTipo.setSelectedItem(seleccionado);
        }
        cargandoTipos = false;
    }

    private void llenarTabla() {
        try {
            ArrayList<Producto> productos;
            String tipo = (String) cboFiltroTipo.getSelectedItem();
            if (tipo == null || tipo.equals("Todos")) {
                productos = productoDAO.consultar();
            } else {
                productos = productoDAO.consultarPorTipo(tipo);
            }

            modelo.setRowCount(0);
            for (Producto p : productos) {
                modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getTipo(),
                    String.format("%.2f", p.getPrecio()), p.getExistencias()});
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer los productos: " + ex.getMessage());
        }
    }

    private Producto leerFormulario() throws DatoInvalidoException {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipo = txtTipo.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || tipo.isEmpty()) {
            throw new DatoInvalidoException("El código, el nombre y el tipo son obligatorios");
        }

        double precio;
        int existencias;
        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
            existencias = Integer.parseInt(txtExistencias.getText().trim());
        } catch (NumberFormatException ex) {
            throw new DatoInvalidoException("El precio y las existencias deben ser números (ej: 1850.00 y 120)");
        }

        if (precio <= 0) {
            throw new DatoInvalidoException("El precio debe ser mayor que cero");
        }
        if (existencias < 0) {
            throw new DatoInvalidoException("Las existencias no pueden ser negativas");
        }
        return new Producto(codigo, nombre, tipo, precio, existencias);
    }

    private void agregar() {
        try {
            Producto producto = leerFormulario();
            productoDAO.insertar(producto);
            JOptionPane.showMessageDialog(this, "Producto " + producto.getNombre() + " agregado");
            limpiar();
            cargarTipos();
            llenarTabla();
        } catch (DatoInvalidoException | RegistroDuplicadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void modificar() {
        if (txtCodigo.isEditable()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla");
            return;
        }
        try {
            Producto producto = leerFormulario();
            productoDAO.actualizar(producto);
            JOptionPane.showMessageDialog(this, "Producto " + producto.getNombre() + " modificado");
            limpiar();
            cargarTipos();
            llenarTabla();
        } catch (DatoInvalidoException | RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (txtCodigo.isEditable()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla");
            return;
        }
        String codigo = txtCodigo.getText();
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Eliminar el producto " + codigo + "?", "Eliminar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            productoDAO.eliminar(codigo);
            JOptionPane.showMessageDialog(this, "Producto eliminado");
            limpiar();
            cargarTipos();
            llenarTabla();
        } catch (RegistroNoEncontradoException | RegistroEnUsoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void cargarSeleccionado() {
        int fila = tblProductos.getSelectedRow();
        if (fila == -1) {
            return;
        }
        String codigo = modelo.getValueAt(fila, 0).toString();
        try {
            Producto p = productoDAO.buscar(codigo);
            txtCodigo.setText(p.getCodigo());
            txtNombre.setText(p.getNombre());
            txtTipo.setText(p.getTipo());
            txtPrecio.setText(String.valueOf(p.getPrecio()));
            txtExistencias.setText(String.valueOf(p.getExistencias()));
            txtCodigo.setEditable(false);
        } catch (RegistroNoEncontradoException | IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtTipo.setText("");
        txtPrecio.setText("");
        txtExistencias.setText("");
        txtCodigo.setEditable(true);
        tblProductos.clearSelection();
    }
}
