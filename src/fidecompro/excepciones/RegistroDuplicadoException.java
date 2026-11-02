package fidecompro.excepciones;

// Cuando se intenta agregar un cliente o producto que ya existe
public class RegistroDuplicadoException extends Exception {

    public RegistroDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
