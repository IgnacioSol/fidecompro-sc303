package fidecompro.excepciones;

// Usuario o contraseña incorrectos en el inicio de sesión
public class CredencialesInvalidasException extends Exception {

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
