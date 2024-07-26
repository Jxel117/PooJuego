package Juego;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelSerpiente extends JPanel {

    Color colorSerpiente = Color.blue;
    Color colorcomida = Color.red;
    int tammax, tam, can, res;
    List<int[]> serpiente = new ArrayList<>();
    int[] comida = new int[2];
    String direccion = "de";
    String direccionproxima = "de"; 
    int puntuacion = 0; // Variable de puntuación
    
    Thread hilo;
    Caminante camino;
    JButton reiniciarBoton;

    public PanelSerpiente(int tammax, int can){
        this.tammax = tammax;
        this.can = can;
        this.tam = tammax / can;
        this.res = tammax % can;
        int[] a = {can / 2 - 1, can / 2 - 1};
        int[] b = {can / 2, can / 2 - 1};
        serpiente.add(a);
        serpiente.add(b);
        crearcomida();

        camino = new Caminante(this);
        hilo = new Thread(camino);
        hilo.start();

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
    }

    @Override
    public void paint(Graphics pintor){
        super.paint(pintor);
        pintor.setColor(colorSerpiente);

        for (int[] par : serpiente){
            pintor.fillRect(res / 2 + par[0] * tam, res / 2 + par[1] * tam, tam - 1, tam - 1);    
        }

        pintor.setColor(colorcomida);
        pintor.fillRect(res / 2 + comida[0] * tam, res / 2 + comida[1] * tam, tam - 1, tam - 1);
        
        // Mostrar la puntuación
        pintor.setColor(Color.BLACK);
        pintor.drawString("Puntuación: " + puntuacion, 10, 20);
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
            JOptionPane.showMessageDialog(this, "PERDISTE. Puntuación: " + puntuacion);
            reiniciarBoton.setVisible(true);
            camino.parar();
        } else {
            if(nuevo[0] == comida[0] && nuevo[1] == comida[1]){
                serpiente.add(nuevo);
                crearcomida();
                puntuacion++; // Incrementar la puntuación
            } else {
                serpiente.add(nuevo);
                serpiente.remove(0);   
            }
        }
    }

    public void crearcomida(){
        boolean existe = false;
        int a = (int)(Math.random() * can);
        int b = (int)(Math.random() * can);
        for (int[] par : serpiente){
            if(par[0] == a && par[1] == b){
                existe = true;
                crearcomida();
                break;
            }
        }
        if(!existe){
            this.comida[0] = a;
            this.comida[1] = b;
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
        camino.parar();
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
        crearcomida();
        puntuacion = 0;
        direccion = "de";
        direccionproxima = "de";
        reiniciarBoton.setVisible(false);

        // Iniciar un nuevo hilo
        camino = new Caminante(this);
        hilo = new Thread(camino);
        hilo.start();

        // Enfocar el panel para que reciba eventos del teclado
        setFocusable(true);
        requestFocusInWindow();
    }
}



           
    

