package fidecompro.vista;

import fidecompro.datos.ClienteDAO;
import fidecompro.datos.FacturaDAO;
import fidecompro.datos.ProductoDAO;
import fidecompro.excepciones.DatoInvalidoException;
import fidecompro.excepciones.ExistenciasInsuficientesException;
import fidecompro.excepciones.RegistroNoEncontradoException;
import fidecompro.modelo.Cliente;
import fidecompro.modelo.DetalleFactura;
import fidecompro.modelo.Factura;
import fidecompro.modelo.Producto;
import fidecompro.modelo.Usuario;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class VentanaFacturacion extends JFrame {
    private Usuario cajero;
    private Factura factura;

    private JTextField txtCliente;
    private JTextField txtProducto;
    private JTextField txtCantidad;
    private JLabel lblNombreCliente;
    private JLabel lblFecha;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;
    private JButton btnBuscar;
    private JButton btnAgregar;
    private JButton btnGenerar;
    private JButton btnQuitar;
    private JButton btnCancelar;
    private JTable tblDetalle;
    private DefaultTableModel modelo;

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private FacturaDAO facturaDAO = new FacturaDAO();

    public VentanaFacturacion(Usuario cajero) {
        this.cajero = cajero;
        this.factura = new Factura(cajero);

        setTitle("Fidecompro - Nueva factura");
        setSize(760, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setBounds(20, 20, 80, 25);
        add(lblCliente);
        txtCliente = new JTextField();
        txtCliente.setBounds(100, 20, 150, 25);
        add(txtCliente);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(260, 18, 90, 30);
        add(btnBuscar);

        lblNombreCliente = new JLabel("");
        lblNombreCliente.setBounds(365, 20, 210, 25);
        add(lblNombreCliente);

        lblFecha = new JLabel("Fecha: " + factura.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblFecha.setBounds(580, 20, 150, 25);
        add(lblFecha);

        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setBounds(20, 60, 80, 25);
        add(lblProducto);
        txtProducto = new JTextField();
        txtProducto.setBounds(100, 60, 150, 25);
        add(txtProducto);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(265, 60, 70, 25);
        add(lblCantidad);
        txtCantidad = new JTextField();
        txtCantidad.setBounds(335, 60, 60, 25);
        add(txtCantidad);

        btnAgregar = new JButton("Agregar al detalle");
        btnAgregar.setBounds(430, 58, 160, 30);
        add(btnAgregar);

        modelo = new DefaultTableModel(new Object[]{"Código", "Producto", "Cant.", "Precio", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tblDetalle = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tblDetalle);
        scroll.setBounds(20, 105, 710, 240);
        add(scroll);

        JLabel lblSubtotalTitulo = new JLabel("Subtotal:", SwingConstants.RIGHT);
        lblSubtotalTitulo.setBounds(470, 360, 120, 25);
        add(lblSubtotalTitulo);
        lblSubtotal = new JLabel("0.00", SwingConstants.RIGHT);
        lblSubtotal.setBounds(600, 360, 130, 25);
        add(lblSubtotal);

        JLabel lblIvaTitulo = new JLabel("IVA (13%):", SwingConstants.RIGHT);
        lblIvaTitulo.setBounds(470, 385, 120, 25);
        add(lblIvaTitulo);
        lblIva = new JLabel("0.00", SwingConstants.RIGHT);
        lblIva.setBounds(600, 385, 130, 25);
        add(lblIva);

        JLabel lblTotalTitulo = new JLabel("Total:", SwingConstants.RIGHT);
        lblTotalTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotalTitulo.setBounds(470, 415, 120, 25);
        add(lblTotalTitulo);
        lblTotal = new JLabel("0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotal.setBounds(600, 415, 130, 25);
        add(lblTotal);

        btnGenerar = new JButton("Generar factura");
        btnGenerar.setBounds(20, 465, 150, 32);
        add(btnGenerar);

        btnQuitar = new JButton("Quitar línea");
        btnQuitar.setBounds(180, 465, 120, 32);
        add(btnQuitar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(310, 465, 110, 32);
        add(btnCancelar);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarDetalle();
            }
        });

        btnQuitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitarLinea();
            }
        });

        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarFactura();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    private void buscarCliente() {
        String cedula = txtCliente.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escriba la cédula del cliente");
            return;
        }
        try {
            Cliente cliente = clienteDAO.buscar(cedula);
            factura.setCliente(cliente);
            lblNombreCliente.setText(cliente.getNombre());
        } catch (RegistroNoEncontradoException ex) {
            factura.setCliente(null);
            lblNombreCliente.setText("");
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer los clientes: " + ex.getMessage());
        }
    }

    private void agregarDetalle() {
        try {
            String codigo = txtProducto.getText().trim();
            if (codigo.isEmpty()) {
                throw new DatoInvalidoException("Escriba el código del producto");
            }
            int cantidad;
            try {
                cantidad = Integer.parseInt(txtCantidad.getText().trim());
            } catch (NumberFormatException ex) {
                throw new DatoInvalidoException("La cantidad debe ser un número entero");
            }
            if (cantidad <= 0) {
                throw new DatoInvalidoException("La cantidad debe ser mayor que cero");
            }

            Producto producto = productoDAO.buscar(codigo);
            // Se cuenta lo que ya lleva la factura de ese mismo producto
            int totalPedido = factura.getCantidadDe(producto.getCodigo()) + cantidad;
            if (!producto.hayExistencias(totalPedido)) {
                throw new ExistenciasInsuficientesException("Solo quedan " + producto.getExistencias() + " unidades de " + producto.getNombre());
            }

            factura.agregarDetalle(new DetalleFactura(producto, cantidad));
            actualizarDetalle();
            txtProducto.setText("");
            txtCantidad.setText("");
            txtProducto.requestFocus();
        } catch (DatoInvalidoException | RegistroNoEncontradoException | ExistenciasInsuficientesException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer los productos: " + ex.getMessage());
        }
    }

    private void quitarLinea() {
        int fila = tblDetalle.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione la línea que desea quitar");
            return;
        }
        factura.quitarDetalle(fila);
        actualizarDetalle();
    }

    // Vuelve a pintar la tabla y los totales con lo que tiene la factura
    private void actualizarDetalle() {
        modelo.setRowCount(0);
        for (DetalleFactura d : factura.getDetalles()) {
            modelo.addRow(new Object[]{
                d.getProducto().getCodigo(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                String.format("%.2f", d.getPrecioUnitario()),
                String.format("%.2f", d.calcularSubtotal())});
        }
        lblSubtotal.setText(String.format("%.2f", factura.calcularSubtotal()));
        lblIva.setText(String.format("%.2f", factura.calcularImpuesto()));
        lblTotal.setText(String.format("%.2f", factura.calcularTotal()));
    }

    private void generarFactura() {
        if (factura.getCliente() == null) {
            JOptionPane.showMessageDialog(this, "Primero busque el cliente");
            return;
        }
        if (factura.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La factura no tiene productos");
            return;
        }

        try {
            // Primero el inventario: si no alcanza, no se guarda nada
            productoDAO.descontarExistencias(factura.getDetalles());
            facturaDAO.insertar(factura);

            factura.generarArchivo();
            JOptionPane.showMessageDialog(this, "Factura No. " + factura.getNumero() + " generada.\nArchivo: " + factura.getNombreArchivo());

            nuevaFactura();
        } catch (ExistenciasInsuficientesException | RegistroNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la factura: " + ex.getMessage());
        }
    }

    // Deja la ventana lista para la siguiente venta
    private void nuevaFactura() {
        factura = new Factura(cajero);
        txtCliente.setText("");
        txtProducto.setText("");
        txtCantidad.setText("");
        lblNombreCliente.setText("");
        actualizarDetalle();
    }

    private void cancelar() {
        if (!factura.getDetalles().isEmpty()) {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Cancelar la factura? Se pierden las líneas agregadas", "Cancelar", JOptionPane.YES_NO_OPTION);
            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }
        }
        dispose();
    }
}
