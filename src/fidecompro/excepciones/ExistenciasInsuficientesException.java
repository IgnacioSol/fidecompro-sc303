package fidecompro.excepciones;

// Cuando se quieren vender más unidades de las que hay
public class ExistenciasInsuficientesException extends Exception {

    public ExistenciasInsuficientesException(String mensaje) {
        super(mensaje);
    }
}
