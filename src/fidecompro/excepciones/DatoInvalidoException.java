package fidecompro.excepciones;

// Campos vacíos o con datos mal escritos en los formularios
public class DatoInvalidoException extends Exception {

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
