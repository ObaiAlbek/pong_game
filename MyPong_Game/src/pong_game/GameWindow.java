package pong_game;

import java.awt.Color;
import javax.swing.JFrame;

public class GameWindow extends JFrame {

    GamePanel panel;

    GameWindow() {
        panel = new GamePanel();
        this.add(panel);
        this.setTitle("Neon Pong");
        this.setResizable(false);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null); // Zentriert das Fenster
        this.setVisible(true);
    }
}