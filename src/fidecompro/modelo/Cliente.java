package fidecompro.modelo;

public class Cliente extends Persona implements Comparable<Cliente> {
    private static final long serialVersionUID = 1L;

    private String cedula;
    private String telefono;
    private String correo;
    private String direccion;

    public Cliente(String cedula, String nombre, String telefono, String correo, String direccion) {
        super(nombre);
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String getIdentificacion() {
        return cedula;
    }

    @Override
    public String getDatosFactura() {
        return "Cliente: " + getNombre() + "\n"
                + "Cédula: " + cedula + "\n"
                + "Teléfono: " + telefono + "\n"
                + "Correo: " + correo + "\n"
                + "Dirección: " + direccion;
    }

    // Para ordenar la lista de clientes por nombre
    @Override
    public int compareTo(Cliente otro) {
        return getNombre().compareToIgnoreCase(otro.getNombre());
    }

    @Override
    public String toString() {
        return getNombre() + " (" + cedula + ")";
    }
}
