package Juego;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Kmylho
 */
public class Juego extends Thread {

    private JLabel var1;
    private TableroJuego TJ;

    public Juego(JLabel var1, TableroJuego TJ) {
        this.var1 = var1;
        this.TJ = TJ;
    }

    public void Run() {
        int jugador1 = 0, jugador2 = 0, jugador3 = 0;
        while (true) {
            try {
                sleep((int) (Math.random() * 1000));
            } catch (InterruptedException ex) {
                Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            }
            jugador1 = TJ.getPrimerCarro().getLocation().x;
            jugador2 = TJ.getSegundoCarro().getLocation().x;
            jugador3 = TJ.getTercerCarro().getLocation().x;
            if (jugador1 < TJ.getMeta().getLocation().x - 10 && jugador2 < TJ.getMeta().getLocation().x - 10 && jugador3 < TJ.getMeta().getLocation().x - 10) {
                var1.setLocation(var1.getLocation().x + 10, var1.getLocation().y);
                TJ.repaint();
            } else {
                break;
            }
        }
        if (var1.getLocation().x >= TJ.getMeta().getLocation().x - 10) {
            if (jugador1 > jugador2 && jugador1 > jugador3) {
                JOptionPane.showMessageDialog(null, "Gano 1");
            } else if (jugador2 > jugador1 && jugador2 > jugador3) {
                JOptionPane.showMessageDialog(null, "Gano 2");
            } else if (jugador3 > jugador1 && jugador3 > jugador2) {
                JOptionPane.showMessageDialog(null, "Gano 3");
            } else {

                JOptionPane.showMessageDialog(null, "Empate");
            }

        }
    }
}
