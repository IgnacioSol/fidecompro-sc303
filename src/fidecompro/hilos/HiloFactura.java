package fidecompro.hilos;

import fidecompro.modelo.Factura;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Hilo que escribe el archivo de la factura mientras la ventana sigue funcionando
public class HiloFactura extends Thread {
    private Factura factura;
    private JFrame ventana;

    public HiloFactura(Factura factura, JFrame ventana) {
        this.factura = factura;
        this.ventana = ventana;
    }

    @Override
    public void run() {
        String mensaje;
        try {
            factura.generarArchivo();
            mensaje = "Factura No. " + factura.getNumero() + " generada.\nArchivo: " + factura.getNombreArchivo();
        } catch (IOException ex) {
            mensaje = "La factura se guardó, pero no se pudo crear el archivo: " + ex.getMessage();
        }

        // Los mensajes en pantalla se muestran desde el hilo de Swing
        final String texto = mensaje;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(ventana, texto);
            }
        });
    }
}
