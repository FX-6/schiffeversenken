package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import Schiffeversenken.Game;
import Schiffeversenken.GameExitStatus;
import Schiffeversenken.GameType;
import Schiffeversenken.Main;
import Schiffeversenken.SettingsHandler;
import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.DualRowPanel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputTextField;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.WrapperPanel;

public class CreateSingleplayerGamePanel extends BackgroundPanel {
	private static final long serialVersionUID = 1L;

   Menu parent;

   public CreateSingleplayerGamePanel(Menu parent, GameType type) {
      this.parent = parent;
      setup();
      fillWithContent();

      parent.createGame(4, type);
   }

   private void setup() {
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
   }

   private void fillWithContent() {
      JPanel wrapperPanel = new WrapperPanel();

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

      // start game button
      JButton startGameButton = new InputButton("Spiel starten");
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

      // add comps to wrapper panel
      wrapperPanel.add(sizeRow);
      wrapperPanel.add(shipsInputRow1);
      wrapperPanel.add(shipsInputRow2);
      wrapperPanel.add(startGameButton);

      // menu button
      JButton menuButton = new MenuButton();
      menuButton.setAlignmentX(CENTER_ALIGNMENT);
      menuButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            parent.exitGame(GameExitStatus.GAME_DISCARDED);
            System.out.println(event.getActionCommand());
            parent.showMenu();
         }
      });

      add(menuButton);
      add(Box.createGlue());
      add(wrapperPanel);
      add(Box.createGlue());
   }
}
