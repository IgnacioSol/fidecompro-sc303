package fidecompro.datos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Serializa y deserializa las listas en archivos .dat
// (en el avance final esto se cambia por la base de datos)
public class Archivo {

    public static void guardar(String ruta, Object objeto) throws IOException {
        ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta));
        try {
            salida.writeObject(objeto);
        } finally {
            salida.close();
        }
    }

    // Devuelve null si el archivo todavía no existe
    public static Object leer(String ruta) throws IOException {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return null;
        }

        ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo));
        try {
            return entrada.readObject();
        } catch (ClassNotFoundException ex) {
            throw new IOException("El archivo " + ruta + " no tiene el formato esperado");
        } finally {
            entrada.close();
        }
    }
}
