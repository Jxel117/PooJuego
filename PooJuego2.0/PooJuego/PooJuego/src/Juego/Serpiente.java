package Juego;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Serpiente extends JPanel implements Runnable {

    Color colorSerpiente = Color.blue;
    Color colorComida = Color.red;
    int tammax, tam, can, res;
    List<int[]> serpiente = new ArrayList<>();
    Comida comida;
    Marcador marcador;
    String direccion = "de";
    String direccionproxima = "de"; 
    boolean estado = true;

    Thread hilo;
    JButton reiniciarBoton;

    public Serpiente(int tammax, int can){
        this.tammax = tammax;
        this.can = can;
        this.tam = tammax / can;
        this.res = tammax % can;
        int[] a = {can / 2 - 1, can / 2 - 1};
        int[] b = {can / 2, can / 2 - 1};
        serpiente.add(a);
        serpiente.add(b);

        // Inicializar la comida
        crearComida();

        // Inicializar el marcador
        marcador = new Marcador(10, 20, Color.BLACK);

        reiniciarBoton = new JButton("Reiniciar");
        reiniciarBoton.setBounds(10, 30, 100, 30);
        reiniciarBoton.addActionListener(e -> reiniciarJuego());
        this.setLayout(null);
        this.add(reiniciarBoton);
        reiniciarBoton.setVisible(false);

        // Agregar listener de teclado
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                int key = evt.getKeyCode();
                switch (key) {
                    case java.awt.event.KeyEvent.VK_UP:
                        cambiardireccion("ar");
                        break;
                    case java.awt.event.KeyEvent.VK_DOWN:
                        cambiardireccion("ab");
                        break;
                    case java.awt.event.KeyEvent.VK_LEFT:
                        cambiardireccion("iz");
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        cambiardireccion("de");
                        break;
                }
            }
        });

        hilo = new Thread(this);
        hilo.start();
    }

    @Override
    public void paint(Graphics pintor){
        super.paint(pintor);
        pintor.setColor(colorSerpiente);

        for (int[] par : serpiente){
            pintor.fillRect(res / 2 + par[0] * tam, res / 2 + par[1] * tam, tam - 1, tam - 1);    
        }

        pintor.setColor(comida.getColor());
        pintor.fillRect(res / 2 + comida.getX() * tam, res / 2 + comida.getY() * tam, tam - 1, tam - 1);
        
        // Dibujar el marcador
        marcador.dibujar(pintor);
    }

    public void avanzar(){
        igualardir();
        int[] ultimo = serpiente.get(serpiente.size() - 1); 
        int agregarx = 0;
        int agregary = 0;
        switch(direccion){
            case "de": agregarx = 1; break;
            case "iz": agregarx = -1; break;
            case "ar": agregary = -1; break;
            case "ab": agregary = 1; break;
        }
        int[] nuevo = {Math.floorMod(ultimo[0] + agregarx, can), 
            Math.floorMod(ultimo[1] + agregary, can)};
        boolean existe = false;
        for(int i = 0; i < serpiente.size(); i++){
            if(nuevo[0] == serpiente.get(i)[0] && nuevo[1] == serpiente.get(i)[1]){
                existe = true;
                break;  
            }
        }
        if(existe){
            JOptionPane.showMessageDialog(this, "PERDISTE. Puntuación: " + marcador.getPuntuacion());
            reiniciarBoton.setVisible(true);
            parar();
        } else {
            if(nuevo[0] == comida.getX() && nuevo[1] == comida.getY()){
                serpiente.add(nuevo);
                crearComida();
                marcador.aumentarPuntuacion(); // Incrementar la puntuación
            } else {
                serpiente.add(nuevo);
                serpiente.remove(0);   
            }
        }
    }

    public void crearComida(){
        boolean existe = false;
        int a = (int)(Math.random() * can);
        int b = (int)(Math.random() * can);
        for (int[] par : serpiente){
            if(par[0] == a && par[1] == b){
                existe = true;
                crearComida();
                break;
            }
        }
        if(!existe){
            comida = new Comida(a, b, colorComida);
        }
    }

    public void cambiardireccion(String dir){
        if((this.direccion.equals("de") || this.direccion.equals("iz")) && (dir.equals("ar") || dir.equals("ab"))){
            this.direccionproxima = dir;
        }
        if((this.direccion.equals("ar") || this.direccion.equals("ab")) && (dir.equals("iz") || dir.equals("de"))){
            this.direccionproxima = dir;
        }
    }

    public void igualardir(){
        this.direccion = this.direccionproxima;
    }

    public void reiniciarJuego() {
        // Detener el hilo actual
        parar();
        try {
            hilo.join();  // Esperar a que el hilo termine
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Reiniciar variables del juego
        serpiente.clear();
        int[] a = {can / 2 - 1, can / 2 - 1};
        int[] b = {can / 2, can / 2 - 1};
        serpiente.add(a);
        serpiente.add(b);
        crearComida();
        marcador.resetearPuntuacion(); // Resetear la puntuación
        direccion = "de";
        direccionproxima = "de";
        reiniciarBoton.setVisible(false);

        // Iniciar un nuevo hilo
        estado = true;
        hilo = new Thread(this);
        hilo.start();

        // Enfocar el panel para que reciba eventos del teclado
        setFocusable(true);
        requestFocusInWindow();
    }

    public void parar() {
        this.estado = false;
    }

    @Override
    public void run() {
        while (estado){
            avanzar();
            repaint();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Serpiente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
