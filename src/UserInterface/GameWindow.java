package UserInterface;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import Schiffeversenken.Main;

/*
 * TODO: Wenn das Spiel beendet wird, dann muss das Fenster wieder geschlossen werden!!
 */

public class GameWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6451215725250748331L;

	public GameWindow() {
		super("Schiffeversenken");
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent e) {
				Main.currentGame.exit();
				Main.menu = new Menu();
				Main.gameWindow = null;
			}

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
		
		setup();
		showFrame();
	}
	
	private void setup() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setMinimumSize(getSize());
		setSize(1500, 750);
		setLocationRelativeTo(null);
	}
	
	public void hideFrame() {
		this.setVisible(false);
	}
	
	public void showFrame() {
		this.setVisible(true);
	}
	
	
	

	
}
