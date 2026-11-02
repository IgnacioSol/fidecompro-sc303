package fidecompro.excepciones;

// Cuando se busca un cliente o producto que no existe
public class RegistroNoEncontradoException extends Exception {

    public RegistroNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
