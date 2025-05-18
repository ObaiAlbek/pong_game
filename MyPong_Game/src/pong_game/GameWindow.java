package pong_game;
import javax.swing.*;

public class GameWindow extends JFrame {

	GamePanle game;

	GameWindow() {
		game = new GamePanle();
		this.add(game);
		this.setTitle("Pong Game");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
