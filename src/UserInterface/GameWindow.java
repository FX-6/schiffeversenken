package UserInterface;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.*;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Notifications.Notification;
import Schiffeversenken.AIPlayer;
import Schiffeversenken.GameExitStatus;
import Schiffeversenken.Main;
import Schiffeversenken.SettingsHandler;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.GameMapPanel;
import UserInterface.UIComponents.GameMenuPanel;
import UserInterface.UIComponents.HeaderLabel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputTextField;
import UserInterface.UIComponents.WrapperPanel;

/*
 * TODO: Wenn das Spiel beendet wird, dann muss das Fenster wieder geschlossen werden!!
 */

public class GameWindow extends JFrame implements Notification {

	/**
	 *
	 */
	private static final long serialVersionUID = -6451215725250748331L;

	public GameWindow() {
		super("Schiffeversenken");

		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					Main.menuWindow.exitGame(GameExitStatus.GAME_DISCARDED);
				} catch (NullPointerException ex) {}
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

      // no layout for absolute Positioning
      this.setLayout(null);

      // background map
      GameMapPanel gameMap = new GameMapPanel();

      // error Label
      WrapperPanel errorPanel = new WrapperPanel();
      JLabel errorLabel = new HeaderLabel("Error", true);
      errorLabel.setForeground(Color.decode(SettingsHandler.getSettingString("color.error")));
      GridBagConstraints errorLabelConstraints = errorPanel.defaultConstraints;
      errorLabelConstraints.gridy = 0;
      errorPanel.add(errorLabel, errorLabelConstraints);
      errorPanel.setLocation(this.getWidth() / 2 - errorPanel.getWidth() / 2, this.getHeight() - errorPanel.getHeight() - 59);
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

      JButton savePanelButton = new InputButton("Speichern", true);
      GridBagConstraints savePanelButtonConstraints = savePanel.defaultConstraints;
      savePanelButtonConstraints.gridy = 1;
      savePanel.add(savePanelButton, savePanelButtonConstraints);
      savePanelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (Main.currentGame.save(String.valueOf(new Date().getTime()), saveNameInput.getText(), Main.currentGame.getPlayer1())) {
               savePanel.setVisible(false);
            } else {
               saveNameInputPanel.setError("Fehler beim speichern");
            }
         }
      });

      savePanel.setLocation(this.getWidth() / 2 - savePanel.getWidth() / 2, this.getHeight() / 2 - savePanel.getHeight() / 2);
      savePanel.setVisible(false);
      this.add(savePanel);

      // create menu for when in game
      GameMenuPanel gameMenu = new GameMenuPanel();

      JLabel currentPlayerLabel = new HeaderLabel("Du", false);
      currentPlayerLabel.setMinimumSize(gameMenu.smallDimension());
      currentPlayerLabel.setPreferredSize(gameMenu.smallDimension());
      currentPlayerLabel.setMaximumSize(gameMenu.smallDimension());
      currentPlayerLabel.setSize(gameMenu.smallDimension());
      GridBagConstraints currentPlayerLabelConstraints = gameMenu.doubleFirstConstraints;
      currentPlayerLabelConstraints.gridy = 0;
      gameMenu.add(currentPlayerLabel, currentPlayerLabelConstraints);

      JButton changePlayerButton = new InputButton("Ändern", false);
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
            if (currentPlayerLabel.getText().equals("Du")) {
               currentPlayerLabel.setText("Gegner:in");
            } else {
               currentPlayerLabel.setText("Du");
            }
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

      JLabel remainingShipsSize2Label = new HeaderLabel("Größe 2: 1/1", false);
      remainingShipsSize2Label.setMinimumSize(gameMenu.smallDimension());
      remainingShipsSize2Label.setPreferredSize(gameMenu.smallDimension());
      remainingShipsSize2Label.setMaximumSize(gameMenu.smallDimension());
      remainingShipsSize2Label.setSize(gameMenu.smallDimension());
      GridBagConstraints remainingShipsSize2LabelConstraints = gameMenu.doubleFirstConstraints;
      remainingShipsSize2LabelConstraints.gridy = 2;
      gameMenu.add(remainingShipsSize2Label, remainingShipsSize2LabelConstraints);

      JLabel remainingShipsSize3Label = new HeaderLabel("Größe 3: 1/1", false);
      remainingShipsSize3Label.setMinimumSize(gameMenu.smallDimension());
      remainingShipsSize3Label.setPreferredSize(gameMenu.smallDimension());
      remainingShipsSize3Label.setMaximumSize(gameMenu.smallDimension());
      remainingShipsSize3Label.setSize(gameMenu.smallDimension());
      GridBagConstraints remainingShipsSize3LabelConstraints = gameMenu.doubleSecondConstraints;
      remainingShipsSize3LabelConstraints.gridy = 2;
      gameMenu.add(remainingShipsSize3Label, remainingShipsSize3LabelConstraints);

      JLabel remainingShipsSize4Label = new HeaderLabel("Größe 4: 1/1", false);
      remainingShipsSize4Label.setMinimumSize(gameMenu.smallDimension());
      remainingShipsSize4Label.setPreferredSize(gameMenu.smallDimension());
      remainingShipsSize4Label.setMaximumSize(gameMenu.smallDimension());
      remainingShipsSize4Label.setSize(gameMenu.smallDimension());
      GridBagConstraints remainingShipsSize4LabelConstraints = gameMenu.doubleFirstConstraints;
      remainingShipsSize4LabelConstraints.gridy = 3;
      gameMenu.add(remainingShipsSize4Label, remainingShipsSize4LabelConstraints);

      JLabel remainingShipsSize5Label = new HeaderLabel("Größe 5: 1/1", false);
      remainingShipsSize5Label.setMinimumSize(gameMenu.smallDimension());
      remainingShipsSize5Label.setPreferredSize(gameMenu.smallDimension());
      remainingShipsSize5Label.setMaximumSize(gameMenu.smallDimension());
      remainingShipsSize5Label.setSize(gameMenu.smallDimension());
      GridBagConstraints remainingShipsSize5LabelConstraints = gameMenu.doubleSecondConstraints;
      remainingShipsSize5LabelConstraints.gridy = 3;
      gameMenu.add(remainingShipsSize5Label, remainingShipsSize5LabelConstraints);

      JButton saveButton = new InputButton("Speichern", true);
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

      JButton shootButton = new InputButton("SCHIEßEN", true); // ẞ (groß) oder ß (klein)?
      shootButton.setMinimumSize(gameMenu.largeDimension());
      shootButton.setPreferredSize(gameMenu.largeDimension());
      shootButton.setMaximumSize(gameMenu.largeDimension());
      shootButton.setSize(gameMenu.largeDimension());
      GridBagConstraints shootButtonConstraints = gameMenu.defaultConstraints;
      shootButtonConstraints.gridy = 5;
      gameMenu.add(shootButton, shootButtonConstraints);

      gameMenu.setVisible(false);
      gameMenu.setLocation(this.getWidth() - gameMenu.getWidth() - 25, 10);
      this.add(gameMenu);

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
         }
      });

      JButton addShips2Button = new InputButton("Größe 2: 0/1", true);
      addShips2Button.setMinimumSize(addShipsGameMenu.largeDimension());
      addShips2Button.setPreferredSize(addShipsGameMenu.largeDimension());
      addShips2Button.setMaximumSize(addShipsGameMenu.largeDimension());
      addShips2Button.setSize(addShipsGameMenu.largeDimension());
      GridBagConstraints addShips2ButtoncConstraints = addShipsGameMenu.defaultConstraints;
      addShips2ButtoncConstraints.gridy = 2;
      addShipsGameMenu.add(addShips2Button, addShips2ButtoncConstraints);

      JButton addShips3Button = new InputButton("Größe 3: 0/1", true);
      addShips3Button.setMinimumSize(addShipsGameMenu.largeDimension());
      addShips3Button.setPreferredSize(addShipsGameMenu.largeDimension());
      addShips3Button.setMaximumSize(addShipsGameMenu.largeDimension());
      addShips3Button.setSize(addShipsGameMenu.largeDimension());
      GridBagConstraints addShips3ButtoncConstraints = addShipsGameMenu.defaultConstraints;
      addShips3ButtoncConstraints.gridy = 3;
      addShipsGameMenu.add(addShips3Button, addShips3ButtoncConstraints);

      JButton addShips4Button = new InputButton("Größe 4: 0/1", true);
      addShips4Button.setMinimumSize(addShipsGameMenu.largeDimension());
      addShips4Button.setPreferredSize(addShipsGameMenu.largeDimension());
      addShips4Button.setMaximumSize(addShipsGameMenu.largeDimension());
      addShips4Button.setSize(addShipsGameMenu.largeDimension());
      GridBagConstraints addShips4ButtoncConstraints = addShipsGameMenu.defaultConstraints;
      addShips4ButtoncConstraints.gridy = 4;
      addShipsGameMenu.add(addShips4Button, addShips4ButtoncConstraints);

      JButton addShips5Button = new InputButton("Größe 5: 0/1", true);
      addShips5Button.setMinimumSize(addShipsGameMenu.largeDimension());
      addShips5Button.setPreferredSize(addShipsGameMenu.largeDimension());
      addShips5Button.setMaximumSize(addShipsGameMenu.largeDimension());
      addShips5Button.setSize(addShipsGameMenu.largeDimension());
      GridBagConstraints addShips5ButtoncConstraints = gameMenu.defaultConstraints;
      addShips5ButtoncConstraints.gridy = 5;
      addShipsGameMenu.add(addShips5Button, addShips5ButtoncConstraints);

      JButton addShipsReadyButton = new InputButton("BEREIT", true);
      addShipsReadyButton.setMinimumSize(addShipsGameMenu.largeDimension());
      addShipsReadyButton.setPreferredSize(addShipsGameMenu.largeDimension());
      addShipsReadyButton.setMaximumSize(addShipsGameMenu.largeDimension());
      addShipsReadyButton.setSize(addShipsGameMenu.largeDimension());
      GridBagConstraints addShipsReadyButtonConstraints = gameMenu.defaultConstraints;
      addShipsReadyButtonConstraints.gridy = 6;
      addShipsGameMenu.add(addShipsReadyButton, addShipsReadyButtonConstraints);
      addShipsReadyButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            // TODO Send ready signal (Felix)
            gameMenu.setVisible(true);
            addShipsGameMenu.setVisible(false);
            gameMap.finishedPlacing();
         }
      });

      addShipsGameMenu.setLocation(this.getWidth() - addShipsGameMenu.getWidth() - 25, 10);
      this.add(addShipsGameMenu);

      // create background as gameMap
      gameMap.setLocation(this.getWidth() / 2 - gameMap.getWidth() / 2, this.getHeight() / 2 - gameMap.getHeight() / 2);
      this.add(gameMap);

      // create clouds as background background
      JPanel backgroundPanel2 = new BackgroundPanel("image_cloud");
      backgroundPanel2.setSize(this.getSize());
      // this.add(backgroundPanel2);

      // reposition elements
      this.addComponentListener(new ComponentAdapter() {
         public void componentResized(ComponentEvent e) {
            addShipsGameMenu.setLocation(e.getComponent().getWidth() - addShipsGameMenu.getWidth() - 25, 10);
            errorPanel.setLocation(e.getComponent().getWidth() / 2 - errorPanel.getWidth() / 2, e.getComponent().getHeight() - errorPanel.getHeight() - 59);
            savePanel.setLocation(e.getComponent().getWidth() / 2 - savePanel.getWidth() / 2, e.getComponent().getHeight() / 2 - savePanel.getHeight() / 2);
            backgroundPanel2.setSize(getSize());
         }
      });
   }

	public void hideFrame() {
		this.setVisible(false);
	}

	public void showFrame() {
		this.setVisible(true);
	}

	public void processNotification(String type, Object object) {}
}
