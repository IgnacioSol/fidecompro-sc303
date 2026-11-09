package fidecompro.datos;

import fidecompro.excepciones.CredencialesInvalidasException;
import fidecompro.modelo.Usuario;
import java.io.IOException;
import java.util.ArrayList;

public class UsuarioDAO {
    private static final String RUTA = "usuarios.dat";

    @SuppressWarnings("unchecked")
    public ArrayList<Usuario> consultar() throws IOException {
        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) Archivo.leer(RUTA);
        if (usuarios == null) {
            // Primera vez que corre el programa: se crean los usuarios de prueba
            usuarios = new ArrayList<Usuario>();
            usuarios.add(new Usuario("admin", "admin123", "Administrador"));
            usuarios.add(new Usuario("cajero1", "cajero123", "Cajero de turno"));
            Archivo.guardar(RUTA, usuarios);
        }
        return usuarios;
    }

    public Usuario validarIngreso(String nombreUsuario, String contrasena) throws IOException, CredencialesInvalidasException {
        for (Usuario u : consultar()) {
            if (u.getNombreUsuario().equals(nombreUsuario) && u.validarContrasena(contrasena)) {
                return u;
            }
        }
        throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
    }
}
