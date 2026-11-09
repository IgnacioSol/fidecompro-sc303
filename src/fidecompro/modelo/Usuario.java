package fidecompro.modelo;

public class Usuario extends Persona {
    private static final long serialVersionUID = 1L;

    private String nombreUsuario;
    private String contrasena;

    public Usuario(String nombreUsuario, String contrasena, String nombreCompleto) {
        super(nombreCompleto);
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    public boolean validarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreCompleto() {
        return getNombre();
    }

    @Override
    public String getIdentificacion() {
        return nombreUsuario;
    }

    @Override
    public String getDatosFactura() {
        return "Atendido por: " + getNombre() + " (" + nombreUsuario + ")";
    }
}
