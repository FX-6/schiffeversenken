package UserInterface.MenuPanels;

import java.awt.GridBagConstraints;
import java.awt.event.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import Notifications.Notification;
import Notifications.NotificationCenter;
import Schiffeversenken.*;
import UserInterface.Menu;
import UserInterface.UIComponents.*;

/**
 * Die UI um ein online Spiel zu erstellen.
 */
public class CreateNetworkGamePanel extends BackgroundPanel implements Notification {
	/**
	 * Wird zur serialization genutzt.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Der Button zum Starten des Spiels.
	 */
	private JButton startGameButton;

	/**
	 * Das Menü, der Parent.
	 */
	Menu parent;

	/**
	 * Erstellt die UI für ein online Spiel.
	 *
	 * @param parent Der Parent der UI
	 * @param type   Die Art des Spiels
	 */
	public CreateNetworkGamePanel(Menu parent, GameType type) {
		NotificationCenter.addObserver("ClientConnected", this);
		NotificationCenter.addObserver("GameLoaded", this);

		this.parent = parent;

		// fill with content
		WrapperPanel wrapperPanel = new WrapperPanel();

		// IP Address heading
		JLabel ipAddressLabel = new HeaderLabel("L\u00e4dt...", true);
		try {
			ipAddressLabel.setText("IP: " + Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			ipAddressLabel.setText("IP nicht gefunden");
			e1.printStackTrace();
		}
		GridBagConstraints ipAddressLabelConstraints = wrapperPanel.defaultConstraints;
		ipAddressLabelConstraints.gridy = 0;
		wrapperPanel.add(ipAddressLabel, ipAddressLabelConstraints);

		// field size input
		InputPanel sizeInputPanel = new InputPanel("Spielfeldgr\u00f6\u00dfe", false);
		InputTextField sizeInput = new InputTextField();
		sizeInputPanel.add(sizeInput);
		GridBagConstraints sizeInputPanelConstraints = doubleFirstConstraints;
		sizeInputPanelConstraints.gridy = 1;
		wrapperPanel.add(sizeInputPanel, sizeInputPanelConstraints);

		// auto ship button
		JButton autoShipButton = new InputButton("Bev\u00f6lkern", false);
		GridBagConstraints autoShipButtonConstraints = doubleSecondConstraints;
		autoShipButtonConstraints.gridy = 1;
		wrapperPanel.add(autoShipButton, autoShipButtonConstraints);

		// ship size 2 input
		InputPanel ship2InputPanel = new InputPanel("2er Schiffe", false);
		InputTextField ship2Input = new InputTextField();
		ship2InputPanel.add(ship2Input);
		GridBagConstraints ship2InputPanelConstraints = doubleFirstConstraints;
		ship2InputPanelConstraints.gridy = 2;
		wrapperPanel.add(ship2InputPanel, ship2InputPanelConstraints);

		// ship size 3 input
		InputPanel ship3InputPanel = new InputPanel("3er Schiffe", false);
		InputTextField ship3Input = new InputTextField();
		ship3InputPanel.add(ship3Input);
		GridBagConstraints ship3InputPanelConstraints = doubleSecondConstraints;
		ship3InputPanelConstraints.gridy = 2;
		wrapperPanel.add(ship3InputPanel, ship3InputPanelConstraints);

		// ship size 4 input
		InputPanel ship4InputPanel = new InputPanel("4er Schiffe", false);
		InputTextField ship4Input = new InputTextField();
		ship4InputPanel.add(ship4Input);
		GridBagConstraints ship4InputPanelConstraints = doubleFirstConstraints;
		ship4InputPanelConstraints.gridy = 3;
		wrapperPanel.add(ship4InputPanel, ship4InputPanelConstraints);

		// ship size 5 input
		InputPanel ship5InputPanel = new InputPanel("5er Schiffe", false);
		InputTextField ship5Input = new InputTextField();
		ship5InputPanel.add(ship5Input);
		GridBagConstraints ship5InputPanelConstraints = doubleSecondConstraints;
		ship5InputPanelConstraints.gridy = 3;
		wrapperPanel.add(ship5InputPanel, ship5InputPanelConstraints);

		// auto fill ships
		autoShipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] ships = new int[4];
				Arrays.fill(ships, 0);

				if (SettingsHandler.validateSizeInput(sizeInput, sizeInputPanel)) {
					ships = Game.getRandomShipConfiguration(sizeInput.getIntValue());

					ship2Input.setValue(ships[0]);
					ship3Input.setValue(ships[1]);
					ship4Input.setValue(ships[2]);
					ship5Input.setValue(ships[3]);
				}
			}
		});

		// load game input
		String[] savedGames = SettingsHandler.getSavedGames();
		String[] loadGameOptions = new String[1 + savedGames.length];
		loadGameOptions[0] = "Nichts laden";
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm '@' dd.MM.");
		for (int i = 0; i < savedGames.length; i++) {
			int extensionIndex = savedGames[i].lastIndexOf(".");
			int seperatorIndex = savedGames[i].indexOf("#");
			date = new Date(Long.valueOf(savedGames[i].substring(0, seperatorIndex)));

			loadGameOptions[i + 1] = (i + 1) + ". " + savedGames[i].substring(seperatorIndex + 1, extensionIndex) + " ("
					+ sdf.format(date) + ")";
		}
		InputPanel loadGameInputPanel = new InputPanel("Spiel laden", true);
		JComboBox<String> loadGameInput = new InputComboBox<String>(loadGameOptions);
		loadGameInputPanel.add(loadGameInput);
		GridBagConstraints loadGameInputPanelConstraints = defaultConstraints;
		loadGameInputPanelConstraints.gridy = 4;
		wrapperPanel.add(loadGameInputPanel, loadGameInputPanelConstraints);

		// Start game button
		startGameButton = new InputButton("Spiel starten", true);
		startGameButton.setEnabled(false);
		startGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedLoadGame = (String) loadGameInput.getSelectedItem();

				if (selectedLoadGame.equalsIgnoreCase("Nichts laden")) {
					InputTextField[] inputTextFields = { sizeInput, ship2Input, ship3Input, ship4Input, ship5Input };
					InputPanel[] inputPanels = { sizeInputPanel, ship2InputPanel, ship3InputPanel, ship4InputPanel,
							ship5InputPanel };

					if (SettingsHandler.validateGameInput(inputTextFields, inputPanels)) {
						Main.currentGame.setPitchSize(sizeInput.getIntValue());

						int[] ships = new int[4];
						ships[0] = ship2Input.getIntValue();
						ships[1] = ship3Input.getIntValue();
						ships[2] = ship4Input.getIntValue();
						ships[3] = ship5Input.getIntValue();
						Main.currentGame.setShips(ships);
						Main.currentGame.transmittSizeAndShips();

						parent.openGameWindow();
					}
				} else {
					startGameButton.setEnabled(false);
					String savedGameID = savedGames[Integer
							.parseInt(selectedLoadGame.substring(0, selectedLoadGame.indexOf("."))) - 1];
					if (!Main.currentGame.load(savedGameID, Main.currentGame.getPlayer1())) {
						loadGameInputPanel.setError("Fehler beim Laden");
						startGameButton.setEnabled(true);
					}
				}
			}
		});
		GridBagConstraints startGameButtonConstraints = defaultConstraints;
		startGameButtonConstraints.gridy = 5;
		wrapperPanel.add(startGameButton, startGameButtonConstraints);

		// wrapperPanel
		wrapperPanel.setLocation(170, 113);
		this.add(wrapperPanel);

		// Menu Button
		JButton menuButton = new MenuButton();
		menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				parent.exitGame(GameExitStatus.GAME_DISCARDED);
				System.out.println(event.getActionCommand());
				parent.showMenu();
			}
		});
		this.add(menuButton);

		// recenter wrapperPanel
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				wrapperPanel.setLocation(e.getComponent().getWidth() / 2 - wrapperPanel.getWidth() / 2,
						e.getComponent().getHeight() / 2 - wrapperPanel.getHeight() / 2);
			}
		});

		parent.createGame(4, type);
	}

	public void processNotification(String type, Object object) {
		if (type.equals("ClientConnected")) {
			startGameButton.setEnabled(true);
		}

		else if (type.equals("GameLoaded")) {
			parent.openGameWindow();
		}
	}
}
