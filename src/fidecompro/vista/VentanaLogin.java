package fidecompro.vista;

import fidecompro.datos.UsuarioDAO;
import fidecompro.excepciones.CredencialesInvalidasException;
import fidecompro.modelo.Usuario;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VentanaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JButton btnSalir;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public VentanaLogin() {
        setTitle("Fidecompro - Iniciar sesión");
        setSize(420, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        JLabel lblTitulo = new JLabel("FIDECOMPRO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setBounds(0, 20, 420, 30);
        add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Sistema de facturación", SwingConstants.CENTER);
        lblSubtitulo.setBounds(0, 52, 420, 20);
        add(lblSubtitulo);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(60, 95, 100, 25);
        add(lblUsuario);
        txtUsuario = new JTextField();
        txtUsuario.setBounds(160, 95, 190, 25);
        add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(60, 130, 100, 25);
        add(lblContrasena);
        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(160, 130, 190, 25);
        add(txtContrasena);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(105, 180, 100, 30);
        add(btnIngresar);

        btnSalir = new JButton("Salir");
        btnSalir.setBounds(215, 180, 100, 30);
        add(btnSalir);

        // Para que Enter funcione como el botón Ingresar
        getRootPane().setDefaultButton(btnIngresar);

        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escriba su usuario y su contraseña");
            return;
        }

        try {
            Usuario encontrado = usuarioDAO.validarIngreso(usuario, contrasena);
            new VentanaMenu(encontrado).setVisible(true);
            dispose();
        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            txtContrasena.setText("");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer los usuarios: " + ex.getMessage());
        }
    }
}
