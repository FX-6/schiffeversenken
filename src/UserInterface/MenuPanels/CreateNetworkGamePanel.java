package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import Notifications.Notification;
import Notifications.NotificationCenter;
import Schiffeversenken.GameExitStatus;
import Schiffeversenken.GameType;
import Schiffeversenken.Main;
import UserInterface.Menu;
import UserInterface.UIComponents.DualRowPanel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputSpinner;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.WrapperPanel;

/*
 * TODO: 	Keine falschen Eingaben in den Textfeldern;
 * 			Spiel starten Knopf deaktivieren, wenn der andere Verbindung wieder abbricht;
 * 			Schiffsanzahlen überprüfen (30% Füllgrad, etc) und Automatische Auswahl
 */

public class CreateNetworkGamePanel extends JPanel implements Notification {

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

      // Field size input and auto ship button
      JPanel sizeRow = new DualRowPanel();
      InputPanel sizeInputPanel = new InputPanel("Spielfeldgröße");
      JSpinner sizeInput = new InputSpinner(new SpinnerNumberModel(5, 5, 25, 1));
      sizeInputPanel.add(sizeInput);
      JButton autoShipButton = new InputButton("Bevölkern");
      sizeRow.add(sizeInputPanel);
      sizeRow.add(autoShipButton);

      // Ship size inputs
      JPanel shipsInputRow1 = new DualRowPanel();
      InputPanel ship2InputPanel = new InputPanel("2er Schiffe");
      JSpinner ship2Input = new InputSpinner(new SpinnerNumberModel(0, 0, 10, 1));
      ship2InputPanel.add(ship2Input);
      InputPanel ship3InputPanel = new InputPanel("3er Schiffe");
      JSpinner ship3Input = new InputSpinner(new SpinnerNumberModel(0, 0, 10, 1));
      ship3InputPanel.add(ship3Input);
      shipsInputRow1.add(ship2InputPanel);
      shipsInputRow1.add(ship3InputPanel);

      JPanel shipsInputRow2 = new DualRowPanel();
      InputPanel ship4InputPanel = new InputPanel("4er Schiffe");
      JSpinner ship4Input = new InputSpinner(new SpinnerNumberModel(0, 0, 10, 1));
      ship4InputPanel.add(ship4Input);
      InputPanel ship5InputPanel = new InputPanel("5er Schiffe");
      JSpinner ship5Input = new InputSpinner(new SpinnerNumberModel(0, 0, 10, 1));
      ship5InputPanel.add(ship5Input);
      shipsInputRow2.add(ship4InputPanel);
      shipsInputRow2.add(ship5InputPanel);

      // Start game button
      startGameButton = new InputButton("Spiel starten");
      startGameButton.setEnabled(false);
      startGameButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Main.currentGame.setPitchSize(Integer.parseInt(sizeInput.getValue().toString()));

            int[] ships = new int[4];
            ships[0] = Integer.parseInt(ship2Input.getValue().toString());
            ships[1] = Integer.parseInt(ship3Input.getValue().toString());
            ships[2] = Integer.parseInt(ship4Input.getValue().toString());
            ships[3] = Integer.parseInt(ship5Input.getValue().toString());
            Main.currentGame.setShips(ships);
            Main.currentGame.transmittSizeAndShips();

            parent.openGameWindow();
         }
      });

      // Alles in Wrapper einfügen
      wrapperPanel.add(sizeRow);
      wrapperPanel.add(shipsInputRow1);
      wrapperPanel.add(shipsInputRow2);
      wrapperPanel.add(startGameButton);

      // Menu Button
      JButton menu = new MenuButton();
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
