package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.GridBagConstraints;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;

import Notifications.Notification;
import Notifications.NotificationCenter;
import Schiffeversenken.Game;
import Schiffeversenken.GameExitStatus;
import Schiffeversenken.GameType;
import Schiffeversenken.Main;
import Schiffeversenken.SettingsHandler;
import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.HeaderLabel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputTextField;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.WrapperPanel;

// TODO Spiel starten Knopf deaktivieren, wenn der andere Verbindung wieder abbricht

public class CreateNetworkGamePanel extends BackgroundPanel implements Notification {
   private static final long serialVersionUID = 1L;

   private JButton startGameButton;

   public CreateNetworkGamePanel(Menu parent, GameType type) {
      NotificationCenter.addObserver("ClientConnected", this);

      // fill with content
      WrapperPanel wrapperPanel = new WrapperPanel();

      // IP Address heading
      JLabel ipAddressLabel = new HeaderLabel();
      try {
         ipAddressLabel.setText("IP: " + Inet4Address.getLocalHost().getHostAddress());
      } catch (UnknownHostException e1) {
         ipAddressLabel.setText("IP nicht gefunden");
         e1.printStackTrace();
      }

      // field size input
      InputPanel sizeInputPanel = new InputPanel("Spielfeldgröße", false);
      InputTextField sizeInput = new InputTextField();
      sizeInputPanel.add(sizeInput);
      GridBagConstraints sizeInputPanelConstraints = doubleFirstConstraints;
      sizeInputPanelConstraints.gridy = 0;
      wrapperPanel.add(sizeInputPanel, sizeInputPanelConstraints);

      // auto ship button
      JButton autoShipButton = new InputButton("Bevölkern", false);
      GridBagConstraints autoShipButtonConstraints = doubleSecondConstraints;
      autoShipButtonConstraints.gridy = 0;
      wrapperPanel.add(autoShipButton, autoShipButtonConstraints);

      // ship size 2 input
      InputPanel ship2InputPanel = new InputPanel("2er Schiffe", false);
      InputTextField ship2Input = new InputTextField();
      ship2InputPanel.add(ship2Input);
      GridBagConstraints ship2InputPanelConstraints = doubleFirstConstraints;
      ship2InputPanelConstraints.gridy = 1;
      wrapperPanel.add(ship2InputPanel, ship2InputPanelConstraints);

      // ship size 3 input
      InputPanel ship3InputPanel = new InputPanel("3er Schiffe", false);
      InputTextField ship3Input = new InputTextField();
      ship3InputPanel.add(ship3Input);
      GridBagConstraints ship3InputPanelConstraints = doubleSecondConstraints;
      ship3InputPanelConstraints.gridy = 1;
      wrapperPanel.add(ship3InputPanel, ship3InputPanelConstraints);

      // ship size 4 input
      InputPanel ship4InputPanel = new InputPanel("4er Schiffe", false);
      InputTextField ship4Input = new InputTextField();
      ship4InputPanel.add(ship4Input);
      GridBagConstraints ship4InputPanelConstraints = doubleFirstConstraints;
      ship4InputPanelConstraints.gridy = 2;
      wrapperPanel.add(ship4InputPanel, ship4InputPanelConstraints);

      // ship size 5 input
      InputPanel ship5InputPanel = new InputPanel("5er Schiffe", false);
      InputTextField ship5Input = new InputTextField();
      ship5InputPanel.add(ship5Input);
      GridBagConstraints ship5InputPanelConstraints = doubleSecondConstraints;
      ship5InputPanelConstraints.gridy = 2;
      wrapperPanel.add(ship5InputPanel, ship5InputPanelConstraints);

      // auto fill ships
      autoShipButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int[] ships = new int[4];
            Arrays.fill(ships, 0);

            if (SettingsHandler.validateSizeInput(sizeInput, sizeInputPanel)) {
               ships = Game.getShipsLeft(sizeInput.getIntValue(), ships);

               ship2Input.setValue(ships[0]);
               ship3Input.setValue(ships[1]);
               ship4Input.setValue(ships[2]);
               ship5Input.setValue(ships[3]);
            }
         }
      });

      // Start game button
      startGameButton = new InputButton("Spiel starten", true);
      startGameButton.setEnabled(false);
      startGameButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            InputTextField[] inputTextFields = {sizeInput, ship2Input, ship3Input, ship4Input, ship5Input};
            InputPanel[] inputPanels = { sizeInputPanel, ship2InputPanel, ship3InputPanel, ship4InputPanel, ship5InputPanel};

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
         }
      });
      GridBagConstraints startGameButtonConstraints = defaultConstraints;
      startGameButtonConstraints.gridy = 3;
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
            wrapperPanel.setLocation(e.getComponent().getWidth() / 2 - wrapperPanel.getWidth() / 2, e.getComponent().getHeight() / 2 - wrapperPanel.getHeight() / 2);
         }
      });

      parent.createGame(4, type);
   }

   public void processNotification(String type, Object object) {
      if (type.equals("ClientConnected")) {
         startGameButton.setEnabled(true);
      }
   }
}
