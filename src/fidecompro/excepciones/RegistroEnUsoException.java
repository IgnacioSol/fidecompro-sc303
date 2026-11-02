package fidecompro.excepciones;

// Cuando se quiere eliminar algo que ya aparece en una factura
public class RegistroEnUsoException extends Exception {

    public RegistroEnUsoException(String mensaje) {
        super(mensaje);
    }
}
