package pong_game;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {


	static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = 700;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    int score1;
    int score2;

    GamePanel() {
        newPaddles();
        newBall();
        score1 = 0;
        score2 = 0;
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        random = new Random();
        // Ball in der Mitte starten
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), (GAME_HEIGHT / 2) - (BALL_DIAMETER / 2), BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddles() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Wir zeichnen auf ein Off-Screen Image für bessere Performance (Double Buffering)
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        // Anti-Aliasing einschalten für glatte Kanten (WICHTIG für "Schönheit")
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Hintergrund dunkelgrau/fast schwarz
        g2d.setColor(new Color(25, 25, 25)); 
        g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        drawScore(g);
        drawCenterLine(g2d); // Gestrichelte Linie
        
        Toolkit.getDefaultToolkit().sync(); // Hilft bei Animationen unter Linux/Mac
    }
    
    // Eine schöne gestrichelte Linie in der Mitte
    private void drawCenterLine(Graphics2D g2d) {
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 50)); // Monospace Font sieht nach Retro-Gaming aus

        g.drawString(String.valueOf(score1), (GAME_WIDTH / 2) - 85, 50);
        g.drawString(String.valueOf(score2), (GAME_WIDTH / 2) + 50, 50);
        
        // Namen kleiner darunter
        g.setFont(new Font("Consolas", Font.PLAIN, 15));
        g.setColor(Color.GRAY);
        g.drawString("Obai", (GAME_WIDTH / 2) - 100, 70);
        g.drawString("CPU", (GAME_WIDTH / 2) + 65, 70);
    }

    public void move() {
        paddle1.move();
        
        // --- VERBESSERTE KI ---
        // Der Computer bewegt sich nicht sofort auf die y-Position (Zucken),
        // sondern gleitet mit einer Geschwindigkeit (speed) in Richtung Ball.
        // Wir geben dem Computer einen kleinen "Fehler", indem er etwas langsamer als der Ball ist (Speed 6 vs Ball 5+)
        int aiSpeed = 6;
        int paddleCenter = paddle2.y + (PADDLE_HEIGHT / 2);
        int ballCenter = ball.y + (BALL_DIAMETER / 2);

        if (paddleCenter < ballCenter - 10) {
            paddle2.setYDirection(aiSpeed);
        } else if (paddleCenter > ballCenter + 10) {
            paddle2.setYDirection(-aiSpeed);
        } else {
            paddle2.setYDirection(0);
        }
        paddle2.move();
        
        ball.move();
    }

    public void checkCollision() {
        // Ball stoppt an oberer/unterer Kante
        if (ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }

        // Ball prallt an Paddles ab
        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity); // Immer nach rechts
            ball.xVelocity++; // Ball wird schneller
            if (ball.yVelocity > 0) ball.yVelocity++; // Mehr Dynamik
            else ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if (ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // Schneller
            if (ball.yVelocity > 0) ball.yVelocity++;
            else ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity); // Immer nach links
            ball.setYDirection(ball.yVelocity);
        }

        // Paddles stoppen an Fensterkanten
        if (paddle1.y <= 0) paddle1.y = 0;
        if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        if (paddle2.y <= 0) paddle2.y = 0;
        if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;

        // Punkte vergeben und Reset
        if (ball.x <= 0) {
            score2++;
            newPaddles();
            newBall();
        }
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score1++;
            newPaddles();
            newBall();
        }
    }

    @Override
    public void run() {
        // Game Loop - begrenzt auf ca. 60 FPS für flüssige Bewegung
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
        }
    }
}