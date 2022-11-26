package UserInterface;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Notifications.NotificationCenter;
import Schiffeversenken.GameExitStatus;
import Schiffeversenken.GameType;
import Schiffeversenken.Main;
import UserInterface.MenuPanels.CreateNetworkGamePanel;
import UserInterface.MenuPanels.MainPanel;
import UserInterface.MenuPanels.NetworkGamePanel;
import UserInterface.MenuPanels.SettingsPanel;

public class Menu extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2989383731928612512L;
		
	public GameWindow gameWindow;
	
	public Menu() {
		super("Schiffeversenken");
		showMenu();
		setup();
		showFrame();
	}
	
	public void hideFrame() {
		this.setVisible(false);
	}
	
	public void showFrame() {
		this.setVisible(true);
	}
	
	
	
	
	private void setup() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setMinimumSize(getSize());
		setSize(700, 400);
		setLocationRelativeTo(null);
	}
	
	// Zeigt das Hauptmenü in diesem Fenster an
	public void showMenu() {
		NotificationCenter.removeAllObservers(getContentPane());
		MainPanel menu = new MainPanel(this);		
		
		setContentPane(menu);
		validate();
	}
	
	
	// Zeigt die Einstellungen in diesem Fenster an
	public void showSettings() {
		NotificationCenter.removeAllObservers(getContentPane());
		SettingsPanel settings = new SettingsPanel(this);
		setContentPane(settings);
		validate();
	}
	
	
	// Zeigt die Paramter zum Spielstart an
	public void showCreateNetworkGame(GameType type) {
		NotificationCenter.removeAllObservers(getContentPane());
		CreateNetworkGamePanel newGame = new CreateNetworkGamePanel(this, type);
		setContentPane(newGame);
		validate();
	}
	
	
	public void showNetworkGame(GameType type) {
		NotificationCenter.removeAllObservers(getContentPane());
		NetworkGamePanel joinGame = new NetworkGamePanel(this, type);
		setContentPane(joinGame);
		validate();
	}
	
	
	public void openGameWindow() {
		this.hideFrame();
		gameWindow = new GameWindow();
	}
	
	public void closeGameWindow() {
		if (gameWindow != null) {
			NotificationCenter.removeAllObservers(gameWindow);
			gameWindow.dispose();
			gameWindow = null;
		}
		
		this.showMenu();
		this.showFrame();
	}
	
	public void createGame(int pitchSize, GameType type) {
		new Thread(new Runnable() {
			public void run() {
				Main.createGame(pitchSize, type);
			}
		}, "StartConnection").start();
	}
	
	public void exitGame(GameExitStatus status) {
		Main.currentGame.exit(this, status);
		closeGameWindow();
	}
	
	
	
	
	
	
	
}
