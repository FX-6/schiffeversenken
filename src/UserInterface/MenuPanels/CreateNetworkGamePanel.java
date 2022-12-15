package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Notifications.Notification;
import Notifications.NotificationCenter;
import Schiffeversenken.Game;
import Schiffeversenken.GameExitStatus;
import Schiffeversenken.GameType;
import Schiffeversenken.Main;
import Schiffeversenken.SettingsHandler;
import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.DualRowPanel;
import UserInterface.UIComponents.HeaderLabel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputTextField;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.WrapperPanel;

// TODO Spiel starten Knopf deaktivieren, wenn der andere Verbindung wieder abbricht

public class CreateNetworkGamePanel extends BackgroundPanel implements Notification {

	/**
	 *
	 */
	private static final long serialVersionUID = -4350076887571572645L;

	private Menu parent;

   private JButton startGameButton;

	public CreateNetworkGamePanel(Menu parent, GameType type) {
		this.parent = parent;
		setup();
		fillWithContent();

		parent.createGame(4, type);
	}

	private void setup() {
		NotificationCenter.addObserver("ClientConnected", this);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	private void fillWithContent() {
      JPanel wrapperPanel = new WrapperPanel();

      // IP Address heading
      JLabel ipAddressLabel = new HeaderLabel();
      try {
         ipAddressLabel.setText("IP: " + Inet4Address.getLocalHost().getHostAddress());
      } catch (UnknownHostException e1) {
         ipAddressLabel.setText("IP nicht gefunden");
         e1.printStackTrace();
      }

      // Field size input and auto ship button
      JPanel sizeRow = new DualRowPanel();
      InputPanel sizeInputPanel = new InputPanel("Spielfeldgröße");
      InputTextField sizeInput = new InputTextField();
      sizeInputPanel.add(sizeInput);
      JButton autoShipButton = new InputButton("Bevölkern");
      sizeRow.add(sizeInputPanel);
      sizeRow.add(autoShipButton);

      // Ship size inputs
      JPanel shipsInputRow1 = new DualRowPanel();
      InputPanel ship2InputPanel = new InputPanel("2er Schiffe");
      InputTextField ship2Input = new InputTextField();
      ship2InputPanel.add(ship2Input);
      InputPanel ship3InputPanel = new InputPanel("3er Schiffe");
      InputTextField ship3Input = new InputTextField();
      ship3InputPanel.add(ship3Input);
      shipsInputRow1.add(ship2InputPanel);
      shipsInputRow1.add(ship3InputPanel);

      JPanel shipsInputRow2 = new DualRowPanel();
      InputPanel ship4InputPanel = new InputPanel("4er Schiffe");
      InputTextField ship4Input = new InputTextField();
      ship4InputPanel.add(ship4Input);
      InputPanel ship5InputPanel = new InputPanel("5er Schiffe");
      InputTextField ship5Input = new InputTextField();
      ship5InputPanel.add(ship5Input);
      shipsInputRow2.add(ship4InputPanel);
      shipsInputRow2.add(ship5InputPanel);

      // auto fill ships
      autoShipButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (SettingsHandler.validateSizeInput(sizeInput, sizeInputPanel)) {
               int[] ships = new int[4];
               Arrays.fill(ships, 0);
               ships = Game.getShipsLeft(sizeInput.getIntValue(), ships);

               ship2Input.setValue(ships[0]);
               ship3Input.setValue(ships[1]);
               ship4Input.setValue(ships[2]);
               ship5Input.setValue(ships[3]);
            }
         }
      });

      // Start game button
      startGameButton = new InputButton("Spiel starten");
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

      // Alles in Wrapper einfügen
      wrapperPanel.add(ipAddressLabel);
      wrapperPanel.add(sizeRow);
      wrapperPanel.add(shipsInputRow1);
      wrapperPanel.add(shipsInputRow2);
      wrapperPanel.add(startGameButton);

      // Menu Button
      JButton menu = new MenuButton();
      menu.setAlignmentX(CENTER_ALIGNMENT);
      menu.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            parent.exitGame(GameExitStatus.GAME_DISCARDED);;
            System.out.println(event.getActionCommand());
            parent.showMenu();
         }
      });

      // Alles einfügen
      add(menu);
      add(Box.createGlue());
      add(wrapperPanel);
      add(Box.createGlue());
	}

	public void processNotification(String type, Object object) {
		if (type.equals("ClientConnected")) {
			startGameButton.setEnabled(true);
		}
	}
}
