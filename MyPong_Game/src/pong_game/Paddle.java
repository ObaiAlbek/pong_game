package pong_game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {

    int id;
    int yVelocity;
    int speed = 10;

    Paddle(int x, int y, int width, int height, int id) {
        super(x, y, width, height);
        this.id = id;
    }

    public void keyPressed(KeyEvent e) {
        if (id == 1) {
            if (e.getKeyCode() == KeyEvent.VK_W) {
                setYDirection(-speed);
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                setYDirection(speed);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (id == 1) {
            if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) {
                setYDirection(0);
            }
        }
    }

    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    public void move() {
        y = y + yVelocity;
    }

    public void draw(Graphics g) {
        // Wir nutzen Graphics2D für glatte Kanten und runde Ecken
        Graphics2D g2d = (Graphics2D) g;
        
        if (id == 1)
            g2d.setColor(new Color(66, 135, 245)); // Modernes Blau
        else
            g2d.setColor(new Color(245, 66, 66)); // Modernes Rot
        
        // fillRoundRect macht die Ecken etwas runder und schöner
        g2d.fillRoundRect(x, y, width, height, 10, 10);
    }
}