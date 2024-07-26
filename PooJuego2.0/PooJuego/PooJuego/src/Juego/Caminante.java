package Juego;

import java.util.logging.Logger;
import java.util.logging.Level;

public class Caminante implements Runnable {
    
    PanelSerpiente panel;
    boolean estado = true;
    
    public Caminante(PanelSerpiente panel){
        this.panel = panel;
    }
    
    @Override
    public void run() {
        while (estado){
            panel.avanzar();
            panel.repaint();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Caminante.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void parar() {
        this.estado = false;
    }
}
