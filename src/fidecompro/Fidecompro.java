package fidecompro;

import fidecompro.vista.VentanaLogin;

public class Fidecompro {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaLogin().setVisible(true);
            }
        });
    }
}
