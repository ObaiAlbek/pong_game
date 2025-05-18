package pong_game;

import java.awt.*;
import java.util.Random;

public class Ball extends Rectangle {

	int yBewegung;
	int xBewegung;
	int speed = 4;
	Random random = new Random();

	Ball(int x, int y, int width, int height) {
		super(x, y, width, height);
		xBewegung = random.nextInt(10) - 5;
		if (xBewegung == 0)
			xBewegung++;
		xBewegung(xBewegung * speed);

	}

	public void draw(Graphics g) {
		g.setColor(Color.green);
		g.fillOval(x, y, width, height);

	}

	public void xBewegung(int x) {
		xBewegung = x;
	}

	public void yBewegung(int y) {
		yBewegung = y;
	}

	public void move() {
		x += xBewegung;
		y += yBewegung;

	}

}
