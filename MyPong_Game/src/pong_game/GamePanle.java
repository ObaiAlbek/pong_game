package pong_game;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanle extends JPanel implements Runnable {

	final static int WIDTH_WINDOW = 1000;
	final static int HEIGH_WINDOW = 700;
	Dimension fensterSize = new Dimension(WIDTH_WINDOW, HEIGH_WINDOW);

	final static int WIDTH_RECT = 20;
	final static int HEIGHT_RECT = 100;

	final static int WIDTH_BALL = 20;
	final static int HEIGHT_BALL = 20;

	Schläger spieler1, spieler2;
	Ball ball;
	Random random;
	Thread game;
	int scoreSpieler1 = 0;
	int scoreSpieler2 = 0;
	int speed = 2;

	GamePanle() {
		zeichneRects();
		this.setFocusable(true);
		this.setPreferredSize(fensterSize);
		this.addKeyListener(new AL());
		random = new Random();
		game = new Thread(this);
		game.start();

	}

	public void zeichneRects() {
		spieler1 = new Schläger(0, (HEIGH_WINDOW / 2) - (HEIGHT_RECT / 2), WIDTH_RECT, HEIGHT_RECT, 1);
		spieler2 = new Schläger(WIDTH_WINDOW - WIDTH_RECT, (HEIGH_WINDOW / 2) - (HEIGHT_RECT / 2), WIDTH_RECT,
				HEIGHT_RECT, 2);
		ball = new Ball(WIDTH_WINDOW / 2, 100, WIDTH_BALL, HEIGHT_BALL);
	}

	public void paint(Graphics g) {
		super.paint(g);

		// Screen farbe
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH_WINDOW, HEIGH_WINDOW);
		g.setColor(Color.GRAY);
		g.fillRect(WIDTH_WINDOW / 2, 0, 5, HEIGH_WINDOW);

		spieler1.draw(g);
		spieler2.draw(g);
		ball.draw(g);

		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.setColor(Color.WHITE);
		g.drawString(scoreSpieler1 + "", WIDTH_WINDOW / 2 - 50, 35);

		g.setColor(Color.WHITE);
		g.drawString(scoreSpieler2 + "", WIDTH_WINDOW / 2 + 50, 35);

		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.YELLOW);
		g.drawString("Obai", WIDTH_WINDOW / 2 - 120, 35);

		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.YELLOW);
		g.drawString("ComputerGegener", WIDTH_WINDOW / 2 + 100, 35);

	}

	public void mov() {

		spieler1.move();
		spieler2.movegegener();
		ball.move();

	}

	public void checkRänder() {
		if (spieler1.y <= 0)
			spieler1.y = 0;

		if (spieler1.y >= HEIGH_WINDOW - HEIGHT_RECT)
			spieler1.y = HEIGH_WINDOW - HEIGHT_RECT;

		if (ball.x > WIDTH_WINDOW / 2 && ball.y < spieler2.y)
			spieler2.setBewegungGegener(-10);

		else if (ball.x > WIDTH_WINDOW / 2 && ball.y > spieler2.y)
			spieler2.setBewegungGegener(10);

		else
			spieler2.setBewegungGegener(0);

		if (spieler2.y <= 0)
			spieler2.y = 0;

		if (spieler2.y >= HEIGH_WINDOW - HEIGHT_RECT)
			spieler2.y = HEIGH_WINDOW - HEIGHT_RECT;

		if (ball.intersects(spieler1)) {
			int bewegexRandom = random.nextInt(8) + 4;
			ball.xBewegung(bewegexRandom);
			int bewegeyRandom = random.nextInt(5) - 2;
			ball.yBewegung(bewegeyRandom);
		}
		if (ball.intersects(spieler2)) {

			int bewegexRandom = random.nextInt(8) + 4;
			ball.xBewegung(-bewegexRandom);
			int bewegeyRandom = random.nextInt(5) - 2;
			ball.yBewegung(bewegeyRandom);

		}
		if (ball.x <= 0) {
			scoreSpieler2++;
			zeichneRects();
		}

		if (ball.x >= WIDTH_WINDOW) {
			scoreSpieler1++;
			zeichneRects();
		}

		if (ball.y >= HEIGH_WINDOW - HEIGHT_BALL)
			ball.yBewegung = -ball.yBewegung;

		if (ball.y <= 0)
			ball.yBewegung = -ball.yBewegung;

	}

	@Override
	public void run() {
		try {
			while (true) {

				mov();
				checkRänder();
				repaint();
				Thread.sleep(5);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public class AL extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			spieler1.keyPressed(e);
			spieler2.keyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			spieler1.keyReleased(e);
			spieler2.keyReleased(e);
		}
	}

}
