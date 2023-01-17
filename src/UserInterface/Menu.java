package UserInterface;

import javax.swing.JFrame;

import Notifications.NotificationCenter;
import Schiffeversenken.GameExitStatus;
import Schiffeversenken.GameType;
import Schiffeversenken.Main;
import UserInterface.MenuPanels.*;

/**
 * Das Fenster des Menüs.
 */
public class Menu extends JFrame {
	private static final long serialVersionUID = 1L;

	public GameWindow gameWindow;

	/**
	 * Erstellt das Fenster des Menüs.
	 */
	public Menu() {
		super("Schiffeversenken");
		showMenu();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setMinimumSize(getSize());
		this.setSize(900, 650);
		this.setLocationRelativeTo(null);
		showFrame();
	}

	/**
	 * Versteckt das Fenster.
	 */
	public void hideFrame() {
		this.setVisible(false);
	}

	/**
	 * Zeigt das Fenster.
	 */
	public void showFrame() {
		this.setVisible(true);
	}

	/**
	 * Zeigt das Hauptmenü in diesem Fenster an.
	 */
	public void showMenu() {
		NotificationCenter.removeAllObservers(getContentPane());
		MainPanel menu = new MainPanel(this);
		this.setContentPane(menu);
		this.validate();
	}

	/**
	 * Zeigt die Einstellungen in diesem Fenster an.
	 */
	public void showSettings() {
		NotificationCenter.removeAllObservers(getContentPane());
		SettingsPanel settings = new SettingsPanel(this);
		this.setContentPane(settings);
		this.validate();
	}

	/**
	 * Zeigt die Spielanleitung.
	 */
	public void showTutorial() {
		NotificationCenter.removeAllObservers(getContentPane());
		TutorialPanel tutorial = new TutorialPanel(this);
		this.setContentPane(tutorial);
		this.validate();
	}

	/**
	 * Zeigt die Inputs für's Singleplayer game.
	 *
	 * @param type Art des Spiels
	 */
	public void showCreateSingleplayerGame(GameType type) {
		NotificationCenter.removeAllObservers(getContentPane());
		CreateSingleplayerGamePanel newGame = new CreateSingleplayerGamePanel(this, type);
		this.setContentPane(newGame);
		this.validate();
	}

	/**
	 * Zeigt die Paramter zum Spielstart an.
	 *
	 * @param type Art des Spiels
	 */
	public void showCreateNetworkGame(GameType type) {
		NotificationCenter.removeAllObservers(getContentPane());
		CreateNetworkGamePanel newGame = new CreateNetworkGamePanel(this, type);
		this.setContentPane(newGame);
		this.validate();
	}

	/**
	 * Zeigt die Inputs zum Beitreten eins Spiels an.
	 *
	 * @param type
	 */
	public void showNetworkGame(GameType type) {
		NotificationCenter.removeAllObservers(getContentPane());
		NetworkGamePanel joinGame = new NetworkGamePanel(this, type);
		this.setContentPane(joinGame);
		this.validate();
	}

	/**
	 * Öffnet das Fenster zum Spielen.
	 */
	public void openGameWindow() {
		this.hideFrame();
		gameWindow = new GameWindow();
	}

	/**
	 * Schliest das Fenster zum Spielen.
	 */
	public void closeGameWindow() {
		if (gameWindow != null) {
			NotificationCenter.removeAllObservers(gameWindow);
			gameWindow.dispose();
			gameWindow = null;
		}

		this.showMenu();
		this.showFrame();
	}

	/**
	 * Erstellt ein Spiel.
	 *
	 * @param pitchSize Größe des Spielfelds
	 * @param type      Art des Spiels
	 */
	public void createGame(int pitchSize, GameType type) {
		new Thread(new Runnable() {
			public void run() {
				Main.createGame(pitchSize, type);
			}
		}, "StartConnection").start();
	}

	/**
	 * Verlässt ein Spiel.
	 *
	 * @param status Grund für das Verlassen
	 */
	public void exitGame(GameExitStatus status) {
		Main.currentGame.exit(this, status);
		this.closeGameWindow();
	}
}
