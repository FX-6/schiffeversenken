package UserInterface;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.*;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import Notifications.Notification;
import Notifications.NotificationCenter;
import Schiffeversenken.*;
import UserInterface.UIComponents.*;

/*
 * TODO: Wenn das Spiel beendet wird, dann muss das Fenster wieder geschlossen werden!!
 */

/**
 * Das Fenster des Spiels.
 */
public class GameWindow extends JFrame implements Notification {
	private static final long serialVersionUID = 1L;

	private boolean gameOver = false;
	private boolean inMatch = false;
	private boolean viewingSelf = true;

	private WrapperPanel gameOverPanel = new WrapperPanel();
	private JLabel gameOverLabel = new HeaderLabel("Game Over", true);
	private GameMapPanel gameMap = new GameMapPanel();
	private WrapperPanel errorPanel = new WrapperPanel();
	private JLabel errorLabel = new HeaderLabel("", true);
	private JButton savePanelButton = new InputButton("Speichern", false);
	private WrapperPanel passTurnWarningPanel = new WrapperPanel();
	private JButton addShips2Button = new InputButton("Größe 2: 0/0", true);
	private JButton addShips3Button = new InputButton("Größe 3: 0/0", true);
	private JButton addShips4Button = new InputButton("Größe 4: 0/0", true);
	private JButton addShips5Button = new InputButton("Größe 5: 0/0", true);
	private GameMenuPanel gameMenu = new GameMenuPanel();
	private JLabel currentPlayerLabel = new HeaderLabel("Du", false);
	private JButton changePlayerButton = new InputButton("Ändern", false);
	private JLabel remainingShipsSize2Label = new HeaderLabel("Größe 2: 0/0", false);
	private JLabel remainingShipsSize3Label = new HeaderLabel("Größe 3: 0/0", false);
	private JLabel remainingShipsSize4Label = new HeaderLabel("Größe 4: 0/0", false);
	private JLabel remainingShipsSize5Label = new HeaderLabel("Größe 5: 0/0", false);
	private JButton saveButton = new InputButton("Speichern", true);
	private JButton shootButton = new InputButton("SCHIEßEN", true); // ẞ (groß) oder ß (klein)?

	/**
	 * Erstellt das Fenster für's Spiel.
	 */
	public GameWindow() {
		super("Schiffeversenken");

		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					Main.menuWindow.exitGame(GameExitStatus.GAME_DISCARDED);
				} catch (NullPointerException ex) {
				}
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}
		});

		this.setup();
		this.showFrame();
	}

	/**
	 * Setup befehle.
	 */
	private void setup() {
		NotificationCenter.addObserver("WinPlayer1", this);
		NotificationCenter.addObserver("WinPlayer2", this);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setMinimumSize(getSize());
		this.setSize(1500, 750);
		this.setLocationRelativeTo(null);

		// no layout for absolute Positioning
		this.setLayout(null);

		// gameOver Label
		GridBagConstraints gameOverLabelConstraints = gameOverPanel.defaultConstraints;
		gameOverLabelConstraints.gridy = 0;
		gameOverPanel.add(gameOverLabel, gameOverLabelConstraints);
		gameOverPanel.setLocation(this.getWidth() / 2 - gameOverPanel.getWidth() / 2,
				this.getHeight() - gameOverPanel.getHeight() - 59);
		gameOverPanel.setVisible(false);
		this.add(gameOverPanel);

		// error Label
		errorLabel.setForeground(Color.decode(SettingsHandler.getSettingString("color.error")));
		GridBagConstraints errorLabelConstraints = errorPanel.defaultConstraints;
		errorLabelConstraints.gridy = 0;
		errorPanel.add(errorLabel, errorLabelConstraints);
		errorPanel.setLocation(this.getWidth() / 2 - errorPanel.getWidth() / 2,
				this.getHeight() - errorPanel.getHeight() - 59);
		errorPanel.setVisible(false);
		this.add(errorPanel);

		// save game input
		WrapperPanel savePanel = new WrapperPanel();

		InputPanel saveNameInputPanel = new InputPanel("Name", true);
		JTextField saveNameInput = new InputTextField();
		saveNameInputPanel.add(saveNameInput);
		GridBagConstraints saveNameInputPanelConstraints = savePanel.defaultConstraints;
		saveNameInputPanelConstraints.gridy = 0;
		savePanel.add(saveNameInputPanel, saveNameInputPanelConstraints);

		GridBagConstraints savePanelButtonConstraints = savePanel.doubleFirstConstraints;
		savePanelButtonConstraints.gridy = 1;
		savePanel.add(savePanelButton, savePanelButtonConstraints);
		savePanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Main.currentGame.save(String.valueOf(new Date().getTime()), saveNameInput.getText(),
						Main.currentGame.getPlayer1())) {
					savePanel.setVisible(false);
				} else {
					saveNameInputPanel.setError("Fehler beim speichern");
				}
			}
		});

		JButton cancelSavePanelButton = new InputButton("Abbrechen", false);
		GridBagConstraints cancelSavePanelButtonConstraints = savePanel.doubleSecondConstraints;
		cancelSavePanelButtonConstraints.gridy = 1;
		savePanel.add(cancelSavePanelButton, cancelSavePanelButtonConstraints);
		cancelSavePanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePanel.setVisible(false);
			}
		});

		savePanel.setLocation(this.getWidth() / 2 - savePanel.getWidth() / 2,
				this.getHeight() / 2 - savePanel.getHeight() / 2);
		savePanel.setVisible(false);
		this.add(savePanel);

		// pass turn to enemy warning
		JLabel passTurnWarningLabel = new HeaderLabel("Zug überspringen?", true);
		GridBagConstraints passTurnWarningLabelConstraints = passTurnWarningPanel.defaultConstraints;
		passTurnWarningLabelConstraints.gridy = 0;
		passTurnWarningPanel.add(passTurnWarningLabel, passTurnWarningLabelConstraints);

		JButton passTurnWarningYesButton = new InputButton("Ja", false);
		GridBagConstraints passTurnWarningYesButtonConstraints = passTurnWarningPanel.doubleFirstConstraints;
		passTurnWarningYesButtonConstraints.gridy = 1;
		passTurnWarningPanel.add(passTurnWarningYesButton, passTurnWarningYesButtonConstraints);
		passTurnWarningYesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.currentGame.getPlayer2().pass();
				passTurnWarningPanel.setVisible(false);
			}
		});

		JButton passTurnWarningNoButton = new InputButton("Nein", false);
		GridBagConstraints passTurnWarningNoButtonConstraints = passTurnWarningPanel.doubleSecondConstraints;
		passTurnWarningNoButtonConstraints.gridy = 1;
		passTurnWarningPanel.add(passTurnWarningNoButton, passTurnWarningNoButtonConstraints);
		passTurnWarningNoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passTurnWarningPanel.setVisible(false);
			}
		});

		passTurnWarningPanel.setLocation(this.getWidth() / 2 - passTurnWarningPanel.getWidth() / 2,
				this.getHeight() / 2 - passTurnWarningPanel.getHeight() / 2);
		passTurnWarningPanel.setVisible(false);
		this.add(passTurnWarningPanel);

		// create menu for placing ships
		GameMenuPanel addShipsGameMenu = new GameMenuPanel();

		JLabel placeShipsHeaderLabel = new HeaderLabel("Platzierte Schiffe", true);
		placeShipsHeaderLabel.setMinimumSize(addShipsGameMenu.largeDimension());
		placeShipsHeaderLabel.setPreferredSize(addShipsGameMenu.largeDimension());
		placeShipsHeaderLabel.setMaximumSize(addShipsGameMenu.largeDimension());
		placeShipsHeaderLabel.setSize(addShipsGameMenu.largeDimension());
		GridBagConstraints placeShipsHeaderLabelConstraints = addShipsGameMenu.defaultConstraints;
		placeShipsHeaderLabelConstraints.gridy = 0;
		addShipsGameMenu.add(placeShipsHeaderLabel, placeShipsHeaderLabelConstraints);

		JButton autoPlaceShipsButton = new InputButton("Automatisch platzieren", true);
		autoPlaceShipsButton.setMinimumSize(addShipsGameMenu.largeDimension());
		autoPlaceShipsButton.setPreferredSize(addShipsGameMenu.largeDimension());
		autoPlaceShipsButton.setMaximumSize(addShipsGameMenu.largeDimension());
		autoPlaceShipsButton.setSize(addShipsGameMenu.largeDimension());
		GridBagConstraints autoPlaceShipsButtonConstraints = addShipsGameMenu.defaultConstraints;
		autoPlaceShipsButtonConstraints.gridy = 1;
		addShipsGameMenu.add(autoPlaceShipsButton, autoPlaceShipsButtonConstraints);
		autoPlaceShipsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AIPlayer.placeShipsAutomatically(Main.currentGame.getPlayer1());

				repaint();

				updateButtonLabels();
			}
		});

		JButton removeShipsButton = new InputButton("Schiffe löschen", true);
		removeShipsButton.setMinimumSize(addShipsGameMenu.largeDimension());
		removeShipsButton.setPreferredSize(addShipsGameMenu.largeDimension());
		removeShipsButton.setMaximumSize(addShipsGameMenu.largeDimension());
		removeShipsButton.setSize(addShipsGameMenu.largeDimension());
		GridBagConstraints removeShipsButtonConstraints = addShipsGameMenu.defaultConstraints;
		removeShipsButtonConstraints.gridy = 2;
		addShipsGameMenu.add(removeShipsButton, removeShipsButtonConstraints);
		removeShipsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameMap.setCurrentlyPlacedShipSize(0);
			}
		});

		addShips2Button.setMinimumSize(addShipsGameMenu.largeDimension());
		addShips2Button.setPreferredSize(addShipsGameMenu.largeDimension());
		addShips2Button.setMaximumSize(addShipsGameMenu.largeDimension());
		addShips2Button.setSize(addShipsGameMenu.largeDimension());
		GridBagConstraints addShips2ButtoncConstraints = addShipsGameMenu.defaultConstraints;
		addShips2ButtoncConstraints.gridy = 3;
		addShipsGameMenu.add(addShips2Button, addShips2ButtoncConstraints);
		addShips2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameMap.setCurrentlyPlacedShipSize(2);
			}
		});

		addShips3Button.setMinimumSize(addShipsGameMenu.largeDimension());
		addShips3Button.setPreferredSize(addShipsGameMenu.largeDimension());
		addShips3Button.setMaximumSize(addShipsGameMenu.largeDimension());
		addShips3Button.setSize(addShipsGameMenu.largeDimension());
		GridBagConstraints addShips3ButtoncConstraints = addShipsGameMenu.defaultConstraints;
		addShips3ButtoncConstraints.gridy = 4;
		addShipsGameMenu.add(addShips3Button, addShips3ButtoncConstraints);
		addShips3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameMap.setCurrentlyPlacedShipSize(3);
			}
		});

		addShips4Button.setMinimumSize(addShipsGameMenu.largeDimension());
		addShips4Button.setPreferredSize(addShipsGameMenu.largeDimension());
		addShips4Button.setMaximumSize(addShipsGameMenu.largeDimension());
		addShips4Button.setSize(addShipsGameMenu.largeDimension());
		GridBagConstraints addShips4ButtoncConstraints = addShipsGameMenu.defaultConstraints;
		addShips4ButtoncConstraints.gridy = 5;
		addShipsGameMenu.add(addShips4Button, addShips4ButtoncConstraints);
		addShips4Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameMap.setCurrentlyPlacedShipSize(4);
			}
		});

		addShips5Button.setMinimumSize(addShipsGameMenu.largeDimension());
		addShips5Button.setPreferredSize(addShipsGameMenu.largeDimension());
		addShips5Button.setMaximumSize(addShipsGameMenu.largeDimension());
		addShips5Button.setSize(addShipsGameMenu.largeDimension());
		GridBagConstraints addShips5ButtoncConstraints = addShipsGameMenu.defaultConstraints;
		addShips5ButtoncConstraints.gridy = 6;
		addShipsGameMenu.add(addShips5Button, addShips5ButtoncConstraints);
		addShips5Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameMap.setCurrentlyPlacedShipSize(5);
			}
		});

		JButton addShipsReadyButton = new InputButton("BEREIT", true);
		addShipsReadyButton.setMinimumSize(addShipsGameMenu.largeDimension());
		addShipsReadyButton.setPreferredSize(addShipsGameMenu.largeDimension());
		addShipsReadyButton.setMaximumSize(addShipsGameMenu.largeDimension());
		addShipsReadyButton.setSize(addShipsGameMenu.largeDimension());
		GridBagConstraints addShipsReadyButtonConstraints = addShipsGameMenu.defaultConstraints;
		addShipsReadyButtonConstraints.gridy = 7;
		addShipsGameMenu.add(addShipsReadyButton, addShipsReadyButtonConstraints);
		addShipsReadyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean allShipsPlaced = true;

				int[] placedShipsOfSize = { 0, 0, 0, 0, 0, 0 };
				List<Ship> placedShips = Main.currentGame.getPlayer1().getShipList();
				for (Ship ship : placedShips) {
					placedShipsOfSize[ship.getLength()]++;
				}

				for (int i = 2; i <= 5; i++) {
					if (placedShipsOfSize[i] != Main.currentGame.getNumberOfShips(i)) {
						allShipsPlaced = false;
					}
				}

				if (allShipsPlaced) {
					gameMenu.setVisible(true);
					addShipsGameMenu.setVisible(false);
					inMatch = true;
					gameMap.finishedPlacing();
					Main.currentGame.setReady(Main.currentGame.getPlayer1());
				} else {
					setError("Nicht alles platziert.");
				}
			}
		});

		addShipsGameMenu.setLocation(this.getWidth() - addShipsGameMenu.getWidth() - 25, 10);
		this.add(addShipsGameMenu);

		// create menu for when in game
		currentPlayerLabel.setMinimumSize(gameMenu.smallDimension());
		currentPlayerLabel.setPreferredSize(gameMenu.smallDimension());
		currentPlayerLabel.setMaximumSize(gameMenu.smallDimension());
		currentPlayerLabel.setSize(gameMenu.smallDimension());
		GridBagConstraints currentPlayerLabelConstraints = gameMenu.doubleFirstConstraints;
		currentPlayerLabelConstraints.gridy = 0;
		gameMenu.add(currentPlayerLabel, currentPlayerLabelConstraints);

		changePlayerButton.setMinimumSize(gameMenu.smallDimension());
		changePlayerButton.setPreferredSize(gameMenu.smallDimension());
		changePlayerButton.setMaximumSize(gameMenu.smallDimension());
		changePlayerButton.setSize(gameMenu.smallDimension());
		GridBagConstraints changePlayerButtonConstraints = gameMenu.doubleSecondConstraints;
		changePlayerButtonConstraints.gridy = 0;
		gameMenu.add(changePlayerButton, changePlayerButtonConstraints);
		changePlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameMap.changeDisplayedPlayer();
				viewingSelf = !viewingSelf;
				updateButtonLabels();
			}
		});

		JLabel remainingShipsHeaderLabel = new HeaderLabel("Verbleibende Schiffe", true);
		remainingShipsHeaderLabel.setMinimumSize(gameMenu.largeDimension());
		remainingShipsHeaderLabel.setPreferredSize(gameMenu.largeDimension());
		remainingShipsHeaderLabel.setMaximumSize(gameMenu.largeDimension());
		remainingShipsHeaderLabel.setSize(gameMenu.largeDimension());
		GridBagConstraints remainingShipsHeaderLabelConstraints = gameMenu.defaultConstraints;
		remainingShipsHeaderLabelConstraints.gridy = 1;
		gameMenu.add(remainingShipsHeaderLabel, remainingShipsHeaderLabelConstraints);

		remainingShipsSize2Label.setMinimumSize(gameMenu.smallDimension());
		remainingShipsSize2Label.setPreferredSize(gameMenu.smallDimension());
		remainingShipsSize2Label.setMaximumSize(gameMenu.smallDimension());
		remainingShipsSize2Label.setSize(gameMenu.smallDimension());
		GridBagConstraints remainingShipsSize2LabelConstraints = gameMenu.doubleFirstConstraints;
		remainingShipsSize2LabelConstraints.gridy = 2;
		gameMenu.add(remainingShipsSize2Label, remainingShipsSize2LabelConstraints);

		remainingShipsSize3Label.setMinimumSize(gameMenu.smallDimension());
		remainingShipsSize3Label.setPreferredSize(gameMenu.smallDimension());
		remainingShipsSize3Label.setMaximumSize(gameMenu.smallDimension());
		remainingShipsSize3Label.setSize(gameMenu.smallDimension());
		GridBagConstraints remainingShipsSize3LabelConstraints = gameMenu.doubleSecondConstraints;
		remainingShipsSize3LabelConstraints.gridy = 2;
		gameMenu.add(remainingShipsSize3Label, remainingShipsSize3LabelConstraints);

		remainingShipsSize4Label.setMinimumSize(gameMenu.smallDimension());
		remainingShipsSize4Label.setPreferredSize(gameMenu.smallDimension());
		remainingShipsSize4Label.setMaximumSize(gameMenu.smallDimension());
		remainingShipsSize4Label.setSize(gameMenu.smallDimension());
		GridBagConstraints remainingShipsSize4LabelConstraints = gameMenu.doubleFirstConstraints;
		remainingShipsSize4LabelConstraints.gridy = 3;
		gameMenu.add(remainingShipsSize4Label, remainingShipsSize4LabelConstraints);

		remainingShipsSize5Label.setMinimumSize(gameMenu.smallDimension());
		remainingShipsSize5Label.setPreferredSize(gameMenu.smallDimension());
		remainingShipsSize5Label.setMaximumSize(gameMenu.smallDimension());
		remainingShipsSize5Label.setSize(gameMenu.smallDimension());
		GridBagConstraints remainingShipsSize5LabelConstraints = gameMenu.doubleSecondConstraints;
		remainingShipsSize5LabelConstraints.gridy = 3;
		gameMenu.add(remainingShipsSize5Label, remainingShipsSize5LabelConstraints);

		saveButton.setMinimumSize(gameMenu.largeDimension());
		saveButton.setPreferredSize(gameMenu.largeDimension());
		saveButton.setMaximumSize(gameMenu.largeDimension());
		saveButton.setSize(gameMenu.largeDimension());
		GridBagConstraints saveButtonConstraints = gameMenu.defaultConstraints;
		saveButtonConstraints.gridy = 4;
		gameMenu.add(saveButton, saveButtonConstraints);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePanel.setVisible(true);
			}
		});

		shootButton.setMinimumSize(gameMenu.largeDimension());
		shootButton.setPreferredSize(gameMenu.largeDimension());
		shootButton.setMaximumSize(gameMenu.largeDimension());
		shootButton.setSize(gameMenu.largeDimension());
		GridBagConstraints shootButtonConstraints = gameMenu.defaultConstraints;
		shootButtonConstraints.gridy = 5;
		gameMenu.add(shootButton, shootButtonConstraints);
		shootButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shootAtFocus();
			}
		});

		gameMenu.setVisible(false);
		gameMenu.setLocation(this.getWidth() - gameMenu.getWidth() - 25, 10);
		this.add(gameMenu);

		// create background as gameMap
		gameMap.setLocation(this.getWidth() / 2 - gameMap.getWidth() / 2,
				this.getHeight() / 2 - gameMap.getHeight() / 2);
		this.add(gameMap);

		// add mouse listener
		gameMap.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (!inMatch && gameMap.placeShip()) {
						updateButtonLabels();
					} else if (inMatch && !viewingSelf) {
						if (!gameMap.setShootFocus()) {
							shootAtFocus();
						}
					} else {
						if (!inMatch) {
							setError("Platzieren nicht möglich");
						} else if (!viewingSelf) {
							setError("Fehler");
						}
					}
				} else if (SwingUtilities.isRightMouseButton(e)) {
					if (!inMatch) {
						gameMap.changeCurrentlyPlacedShipOrientation();
					} else {
						gameMap.clearShootFocusIfSame();
					}
				}
			}
		});

		// add key listener
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				"enter_noCtrl");
		gameMap.getActionMap().put("enter_noCtrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				if (!inMatch && gameMap.placeShip()) {
					updateButtonLabels();
				} else if (inMatch && !viewingSelf) {
					if (!gameMap.setShootFocus()) {
						shootAtFocus();
					}
				} else {
					if (!inMatch) {
						setError("Platzieren nicht möglich");
					} else {
						setError("Fehler");
					}
				}
			}
		});
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "enter_ctrl");
		gameMap.getActionMap().put("enter_ctrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				if (!inMatch) {
					gameMap.changeCurrentlyPlacedShipOrientation();
				} else {
					gameMap.clearShootFocusIfSame();
				}
			}
		});
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
				"up_noCtrl");
		gameMap.getActionMap().put("up_noCtrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				gameMap.changeCurrentlyFocusedTile(false, -1);
			}
		});
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
				"down_noCtrl");
		gameMap.getActionMap().put("down_noCtrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				gameMap.changeCurrentlyFocusedTile(false, 1);
			}
		});
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
				"left_noCtrl");
		gameMap.getActionMap().put("left_noCtrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				gameMap.changeCurrentlyFocusedTile(true, -1);
			}
		});
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
				"right_noCtrl");
		gameMap.getActionMap().put("right_noCtrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				gameMap.changeCurrentlyFocusedTile(true, 1);
			}
		});
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK), "up_ctrl");
		gameMap.getActionMap().put("up_ctrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				gameMap.changePositionByTiles(false, -1);
			}
		});
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK), "down_ctrl");
		gameMap.getActionMap().put("down_ctrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				gameMap.changePositionByTiles(false, 1);
			}
		});
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK), "left_ctrl");
		gameMap.getActionMap().put("left_ctrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				gameMap.changePositionByTiles(true, -1);
			}
		});
		gameMap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK), "right_ctrl");
		gameMap.getActionMap().put("right_ctrl", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameOver) {
					return;
				}

				gameMap.changePositionByTiles(true, 1);
			}
		});

		// reposition elements
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				addShipsGameMenu.setLocation(e.getComponent().getWidth() - addShipsGameMenu.getWidth() - 25, 10);
				gameMenu.setLocation(e.getComponent().getWidth() - gameMenu.getWidth() - 25, 10);
				errorPanel.setLocation(e.getComponent().getWidth() / 2 - errorPanel.getWidth() / 2,
						e.getComponent().getHeight() - errorPanel.getHeight() - 59);
				savePanel.setLocation(e.getComponent().getWidth() / 2 - savePanel.getWidth() / 2,
						e.getComponent().getHeight() / 2 - savePanel.getHeight() / 2);
			}
		});

		this.updateButtonLabels();
	}

	/**
	 * Zeigt eine Fehlernachricht für 2 Sekunden auf dem Screen.
	 *
	 * @param text Der Fehler der angezeigt werden soll
	 */
	private void setError(String text) {
		errorLabel.setText(text);

		errorPanel.setVisible(true);

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				errorPanel.setVisible(false);
			}
		}, 2000);
	}

	/**
	 * Erneuert die beschriftung der Buttons.
	 */
	private void updateButtonLabels() {
		int[] placedShipsOfSize = { 0, 0, 0, 0, 0, 0 };

		List<Ship> placedShips = Main.currentGame.getPlayer1().getShipList();

		for (Ship ship : placedShips) {
			placedShipsOfSize[ship.getLength()]++;
		}

		addShips2Button.setText("Größe 2: " + placedShipsOfSize[2] + "/" + Main.currentGame.getNumberOfShips(2));
		addShips3Button.setText("Größe 3: " + placedShipsOfSize[3] + "/" + Main.currentGame.getNumberOfShips(3));
		addShips4Button.setText("Größe 4: " + placedShipsOfSize[4] + "/" + Main.currentGame.getNumberOfShips(4));
		addShips5Button.setText("Größe 5: " + placedShipsOfSize[5] + "/" + Main.currentGame.getNumberOfShips(5));
		if (placedShipsOfSize[2] == Main.currentGame.getNumberOfShips(2)) {
			addShips2Button.setEnabled(false);
		} else {
			addShips2Button.setEnabled(true);
		}
		if (placedShipsOfSize[3] == Main.currentGame.getNumberOfShips(3)) {
			addShips3Button.setEnabled(false);
		} else {
			addShips3Button.setEnabled(true);
		}
		if (placedShipsOfSize[4] == Main.currentGame.getNumberOfShips(4)) {
			addShips4Button.setEnabled(false);
		} else {
			addShips4Button.setEnabled(true);
		}
		if (placedShipsOfSize[5] == Main.currentGame.getNumberOfShips(5)) {
			addShips5Button.setEnabled(false);
		} else {
			addShips5Button.setEnabled(true);
		}

		if (viewingSelf) {
			currentPlayerLabel.setText("Du");

			for (Ship ship : placedShips) {
				if (ship.isDestroyed()) {
					placedShipsOfSize[ship.getLength()]--;
				}
			}

			remainingShipsSize2Label
					.setText("Größe 2: " + placedShipsOfSize[2] + "/" + Main.currentGame.getNumberOfShips(2));
			remainingShipsSize3Label
					.setText("Größe 3: " + placedShipsOfSize[3] + "/" + Main.currentGame.getNumberOfShips(3));
			remainingShipsSize4Label
					.setText("Größe 4: " + placedShipsOfSize[4] + "/" + Main.currentGame.getNumberOfShips(4));
			remainingShipsSize5Label
					.setText("Größe 5: " + placedShipsOfSize[5] + "/" + Main.currentGame.getNumberOfShips(5));
		} else {
			currentPlayerLabel.setText("Gegner:in");

			int[][] pointsShot = Main.currentGame.getPlayer1().getPointsShot();

			for (int xCord = 0; xCord < Main.currentGame.getPitchSize(); xCord++) {
				for (int yCord = 0; yCord < Main.currentGame.getPitchSize(); yCord++) {
					if (pointsShot[xCord][yCord] == 2) {
						int killedRootX = xCord, killedRootY = yCord;
						boolean searchingX = true, searchingY = true;

						while (searchingX || searchingY) {
							if (killedRootX - 1 >= 0 && pointsShot[killedRootX - 1][killedRootY] == 1) {
								killedRootX--;
							} else {
								searchingX = false;
							}

							if (killedRootY - 1 >= 0 && pointsShot[killedRootX][killedRootY - 1] == 1) {
								killedRootY--;
							} else {
								searchingY = false;
							}
						}

						pointsShot[killedRootX][killedRootY] = 2;

						searchingX = true;
						searchingY = true;
						for (int i = 1; searchingX || searchingY; i++) {
							if (killedRootX + i < pointsShot.length && pointsShot[killedRootX + i][killedRootY] >= 1) {
								pointsShot[killedRootX + i][killedRootY] = 3;
							} else {
								searchingX = false;
							}

							if (killedRootY + i < pointsShot.length && pointsShot[killedRootX][killedRootY + i] >= 1) {
								pointsShot[killedRootX][killedRootY + i] = 3;
							} else {
								searchingY = false;
							}

							placedShipsOfSize[i]--;
						}
					}
				}
			}

			remainingShipsSize2Label
					.setText("Größe 2: " + placedShipsOfSize[2] + "/" + Main.currentGame.getNumberOfShips(2));
			remainingShipsSize3Label
					.setText("Größe 3: " + placedShipsOfSize[3] + "/" + Main.currentGame.getNumberOfShips(3));
			remainingShipsSize4Label
					.setText("Größe 4: " + placedShipsOfSize[4] + "/" + Main.currentGame.getNumberOfShips(4));
			remainingShipsSize5Label
					.setText("Größe 5: " + placedShipsOfSize[5] + "/" + Main.currentGame.getNumberOfShips(5));

		}

		if (viewingSelf || !Main.currentGame.getPlayer1().isMyTurn()) {
			shootButton.setEnabled(false);
		} else {
			shootButton.setEnabled(true);
		}

		if (gameOver) {
			changePlayerButton.setEnabled(false);
			saveButton.setEnabled(false);
			shootButton.setEnabled(false);
			savePanelButton.setEnabled(false);
		}
	}

	/**
	 * Schießt auf den aktuellen Focus.
	 */
	private void shootAtFocus() {
		if (inMatch && Main.currentGame.getPlayer1().isMyTurn()) {
			if (gameMap.getShootFocus() != null) {
				int res = Main.currentGame.getPlayer1().shoot(gameMap.getShootFocus().add(1, 1));
				gameMap.clearShootFocus();

				if (res == 0) {
					setError("Daneben");
				} else if (res == 1) {
					setError("Treffer");
				} else if (res == 2) {
					updateButtonLabels();
					setError("Treffer, versenkt");
				} else {
					setError("Fehler");
				}
			} else {
				passTurnWarningPanel.setVisible(true);
			}
		} else {
			if (Main.currentGame.getPlayer2().isMyTurn()) {
				setError("Gegner:in am Zug");
			} else {
				setError("Gegner:in noch nicht bereit");
			}
		}
	}

	/**
	 * Ändert die Sichtbarkeit des Fensters auf <code>false</code>.
	 */
	public void hideFrame() {
		this.setVisible(false);
	}

	/**
	 * Ändert die Sichtbarkeit des Fensters auf <code>true</code>.
	 */
	public void showFrame() {
		this.setVisible(true);
	}

	public void processNotification(String type, Object object) {
		if (type.equals("WinPlayer1")) {
			gameOver = true;
			updateButtonLabels();

			gameOverLabel.setText("Du hast gewonnen :)");
			gameOverPanel.setVisible(true);
		} else if (type.equals("WinPlayer2")) {
			gameOver = true;
			updateButtonLabels();

			gameOverLabel.setText("Du hast verloren :(");
			gameOverPanel.setVisible(true);
		}
	}
}
