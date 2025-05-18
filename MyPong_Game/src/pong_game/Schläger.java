package pong_game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Schläger extends Rectangle {
	
	int id;
	int yBewegung;
	
	int gegnerBewegungy;
	int speed = 10;
	
	Schläger(int x, int y, int width, int height, int id){
		super(x,y,width,height);
		this.id = id;
	}
	
	public void move() {
		y += yBewegung;
	}
	
	public void movegegener() {
		y += gegnerBewegungy;
	}
	
	
	public void keyPressed(KeyEvent e) {
		if (id == 1) {
			if (e.getKeyCode() == KeyEvent.VK_W) 
				setBewegung(-speed);
			
			if (e.getKeyCode() == KeyEvent.VK_S)
				setBewegung(speed);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (id == 1) {
			if (e.getKeyCode() == KeyEvent.VK_W ||e.getKeyCode() == KeyEvent.VK_S)
				setBewegung(0);
		}
	}
	
	public void setBewegung(int bewegung) {
		yBewegung = bewegung;
	}
	
	public void setBewegungGegener(int bewegung) {
		gegnerBewegungy = bewegung;
	}
	
	public void draw(Graphics g) {
		if (id == 1)
			g.setColor(Color.RED);
		else 
			g.setColor(Color.BLUE);
		
		g.fillRect(x,y,width,height);
	}
	
}
