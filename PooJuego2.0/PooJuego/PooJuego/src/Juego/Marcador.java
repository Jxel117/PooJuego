package Juego;

import java.awt.Color;
import java.awt.Graphics;

public class Marcador {
    private int puntuacion;
    private Color color;
    private int x, y;

    public Marcador(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.puntuacion = 0;
    }

    public void aumentarPuntuacion() {
        this.puntuacion++;
    }

    public void resetearPuntuacion() {
        this.puntuacion = 0;
    }

    public int getPuntuacion() {
        return this.puntuacion;
    }

    public void dibujar(Graphics g) {
        g.setColor(color);
        g.drawString("Puntuación: " + puntuacion, x, y);
    }
}
