
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HP-I3
 */

package Juego;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame ventana = new JFrame("Juego de la Culebrita");
        Serpiente panelSerpiente = new Serpiente(600, 20);
        
        ventana.add(panelSerpiente);
        ventana.setSize(620, 640);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
        
        ventana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                int key = evt.getKeyCode();
                switch (key) {
                    case java.awt.event.KeyEvent.VK_UP:
                        panelSerpiente.cambiardireccion("ar");
                        break;
                    case java.awt.event.KeyEvent.VK_DOWN:
                        panelSerpiente.cambiardireccion("ab");
                        break;
                    case java.awt.event.KeyEvent.VK_LEFT:
                        panelSerpiente.cambiardireccion("iz");
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        panelSerpiente.cambiardireccion("de");
                        break;
                }
            }
        });
    }
}

