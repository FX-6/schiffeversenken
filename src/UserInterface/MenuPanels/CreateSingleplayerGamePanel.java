package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import Schiffeversenken.Game;
import Schiffeversenken.GameExitStatus;
import Schiffeversenken.GameType;
import Schiffeversenken.Main;
import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.DualRowPanel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputSpinner;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.WrapperPanel;

// TODO serialVersionUID? Was ist das üebrhaupt?
// TODO Input validation (Felix)
// TODO Add Play as KI Checkbox (Felix)

public class CreateSingleplayerGamePanel extends BackgroundPanel {
	// private static final long serialVersionUID = ;

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

      // auto fill ships
      autoShipButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int[] ships = new int[4];
            Arrays.fill(ships, 0);
            ships = Game.getShipsLeft(Integer.parseInt(sizeInput.getValue().toString()), ships);

            ship2Input.setValue(ships[0]);
            ship3Input.setValue(ships[1]);
            ship4Input.setValue(ships[2]);
            ship5Input.setValue(ships[3]);
         }
      });

      // start game button
      JButton startGameButton = new InputButton("Spiel starten");
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
