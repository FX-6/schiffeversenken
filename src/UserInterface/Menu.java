package UserInterface;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
	
	private JPanel menu;
	
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
		if (menu == null) {	
			menu = new MainPanel(this);		
		}
		
		setContentPane(menu);
		validate();
	}
	
	
	// Zeigt die Einstellungen in diesem Fenster an
	public void showSettings() {
		SettingsPanel settings = new SettingsPanel(this);
		setContentPane(settings);
		validate();
	}
	
	
	// Zeigt die Paramter zum Spielstart an
	public void showCreateNetworkGame(GameType type) {
		CreateNetworkGamePanel newGame = new CreateNetworkGamePanel(this, type);
		setContentPane(newGame);
		validate();
	}
	
	
	public void showNetworkGame(GameType type) {
		NetworkGamePanel joinGame = new NetworkGamePanel(this, type);
		setContentPane(joinGame);
		validate();
	}
	
	
	
	
	
	
	
	
	
	public void createGame(int pitchSize, GameType type, String hostAddress) {
		Main.hostAddress = hostAddress;
		Main.createGame(pitchSize, type);
	}
	
	public void openGameWindow() {
		hideFrame();
		Main.gameWindow = new GameWindow();
		Main.menu = null;
	}
	
}
