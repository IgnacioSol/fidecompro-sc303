package fidecompro.vista;

import fidecompro.modelo.Usuario;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class VentanaMenu extends JFrame {
    private Usuario usuario;

    private JLabel lblBienvenida;
    private JButton btnClientes;
    private JButton btnProductos;
    private JButton btnFacturacion;
    private JButton btnCerrarSesion;

    public VentanaMenu(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Fidecompro - Menú principal");
        setSize(420, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        lblBienvenida = new JLabel("Bienvenido, " + usuario.getNombreUsuario());
        lblBienvenida.setBounds(20, 15, 300, 25);
        add(lblBienvenida);

        btnClientes = new JButton("Clientes");
        btnClientes.setBounds(115, 55, 180, 35);
        add(btnClientes);

        btnProductos = new JButton("Productos");
        btnProductos.setBounds(115, 105, 180, 35);
        add(btnProductos);

        btnFacturacion = new JButton("Facturación");
        btnFacturacion.setBounds(115, 155, 180, 35);
        add(btnFacturacion);

        btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setBounds(115, 205, 180, 35);
        add(btnCerrarSesion);

        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaClientes().setVisible(true);
            }
        });



        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea cerrar la sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        // Se cierran todas las ventanas abiertas (clientes, productos, facturas) y se vuelve al login
        for (Window ventana : Window.getWindows()) {
            ventana.dispose();
        }
        new VentanaLogin().setVisible(true);
    }
}
