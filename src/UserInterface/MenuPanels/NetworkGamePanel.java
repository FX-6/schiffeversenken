package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.*;
import java.awt.GridBagConstraints;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Notifications.Notification;
import Notifications.NotificationCenter;
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

public class NetworkGamePanel extends BackgroundPanel implements Notification {
   private static final long serialVersionUID = 1L;

   private int receivedGameData = 0; 				// Zählt, ob Schiffe und Spielfeldgröße empfangen wurden. Wenn 2, dann wird GameWindow geöffnet

   private Menu parent;
   private InputPanel ipInputPanel;

   private JTextField ipInput;
   private JButton joinGameAsHumanButton;
   private JButton joinGameAsAiButton;
   private JButton createGameAsHumanButton;
   private JButton createGameAsAiButton;
   private JButton menuButton;

   public NetworkGamePanel(Menu parent, GameType type) {
      this.parent = parent;
      // setup
      NotificationCenter.addObserver("ServerConnected", this);
      NotificationCenter.addObserver("ConnectionFailed", this);
      NotificationCenter.addObserver("ReceivedGameData", this);
      NotificationCenter.addObserver("GameLoaded", this);

      // fill with content
      WrapperPanel wrapperPanel = new WrapperPanel();

      // IP input
      ipInputPanel = new InputPanel("Host IP", true);
      ipInput = new InputTextField();
      ipInput.addKeyListener(new KeyListener() {
         @Override
         public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == '\n') {
               if (SettingsHandler.validateIP(ipInput.getText())) {
                  ipInputPanel.setError("");
                  Main.hostAddress = ipInput.getText();
                  animateConnecting();
                  parent.createGame(4, type);
               } else {
                  ipInputPanel.setError("Invalide IP");
               }
            }
         }

         @Override
         public void keyPressed(KeyEvent e) {}

         @Override
         public void keyReleased(KeyEvent e) {}
      });
      ipInputPanel.add(ipInput);
      GridBagConstraints sizeInputPanelConstraints = defaultConstraints;
      sizeInputPanelConstraints.gridy = 0;
      wrapperPanel.add(ipInputPanel, sizeInputPanelConstraints);

      // join game as header
      JLabel joinGameAsLabel = new HeaderLabel("Spiel beitreten als", true);
      GridBagConstraints joinGameAsLabelConstraints = defaultConstraints;
      joinGameAsLabelConstraints.gridy = 1;
      wrapperPanel.add(joinGameAsLabel, joinGameAsLabelConstraints);

      // join game as human button
      joinGameAsHumanButton = new InputButton("Mensch", false);
      joinGameAsHumanButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (SettingsHandler.validateIP(ipInput.getText())) {
               ipInputPanel.setError("");
               System.out.println(e.getActionCommand());
               Main.hostAddress = ipInput.getText();
               animateConnecting();
               parent.createGame(4, GameType.NETWORK_CLIENT);
            } else {
               ipInputPanel.setError("Invalide IP");
            }
         }
      });
      GridBagConstraints joinGameAsHumanButtonConstraints = doubleFirstConstraints;
      joinGameAsHumanButtonConstraints.gridy = 2;
      wrapperPanel.add(joinGameAsHumanButton, joinGameAsHumanButtonConstraints);

      // join game as ai button
      joinGameAsAiButton = new InputButton("Bot", false);
      joinGameAsAiButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (SettingsHandler.validateIP(ipInput.getText())) {
               ipInputPanel.setError("");
               System.out.println(e.getActionCommand());
               Main.hostAddress = ipInput.getText();
               animateConnecting();
               parent.createGame(4, GameType.NETWORK_AI_CLIENT);
            } else {
               ipInputPanel.setError("Invalide IP");
            }
         }
      });
      GridBagConstraints joinGameAsAiButtonConstraints = doubleSecondConstraints;
      joinGameAsAiButtonConstraints.gridy = 2;
      wrapperPanel.add(joinGameAsAiButton, joinGameAsAiButtonConstraints);

      // create game as header
      JLabel createGameAsLabel = new HeaderLabel("Spiel erstellen als", true);
      GridBagConstraints createGameAsLabelConstraints = defaultConstraints;
      createGameAsLabelConstraints.gridy = 3;
      wrapperPanel.add(createGameAsLabel, createGameAsLabelConstraints);

      // create game as human button
      createGameAsHumanButton = new InputButton("Mensch", false);
      createGameAsHumanButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
            parent.showCreateNetworkGame(GameType.NETWORK_SERVER);
         }
      });
      GridBagConstraints createGameAsHumanButtonConstraints = doubleFirstConstraints;
      createGameAsHumanButtonConstraints.gridy = 4;
      wrapperPanel.add(createGameAsHumanButton, createGameAsHumanButtonConstraints);

      // create game as ai button
      createGameAsAiButton = new InputButton("Bot", false);
      createGameAsAiButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
            parent.showCreateNetworkGame(GameType.NETWORK_AI_SERVER);
         }
      });
      GridBagConstraints createGameAsAiButtonConstraints = doubleSecondConstraints;
      createGameAsAiButtonConstraints.gridy = 4;
      wrapperPanel.add(createGameAsAiButton, createGameAsAiButtonConstraints);

      // wrapperPanel
      wrapperPanel.setLocation(170, 68);
      this.add(wrapperPanel);

      // MenuButton
      menuButton = new MenuButton();
      menuButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            if (Main.currentGame != null) {
            	parent.exitGame(GameExitStatus.GAME_DISCARDED);
            }
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
   }

   private void animateConnecting() {
      menuButton.setEnabled(false);
      joinGameAsHumanButton.setEnabled(false);
      createGameAsHumanButton.setEnabled(false);
      joinGameAsAiButton.setEnabled(false);
      createGameAsAiButton.setEnabled(false);
      ipInput.setEnabled(false);

      String text = ipInput.getText();

      ipInput.setText(text + " ");

      Timer timer = new Timer();
      TimerTask task = new TimerTask() {

         @Override
         public void run() {
            if (ipInput.getText().equals("")) {
               ipInput.setText(text);
               timer.cancel();
               menuButton.setEnabled(true);
               joinGameAsHumanButton.setEnabled(true);
               createGameAsHumanButton.setEnabled(true);
               joinGameAsAiButton.setEnabled(true);
               createGameAsAiButton.setEnabled(true);
               ipInput.setEnabled(true);
            }
            else if (ipInput.getText().equals(text + " ...")) {
               ipInput.setText(text + " ");
            }
            else if (ipInput.getText().startsWith("W")) {			// animateWaitingForServer() ist aktiviert worden
            	timer.cancel();
            	menuButton.setEnabled(true);
            }
            else {
               ipInput.setText(ipInput.getText() + ".");
            }
         }
      };
      timer.schedule(task, 0, 250);
   }

   private void animateWaitingForServer() {
	   Timer timer = new Timer();
	   TimerTask task = new TimerTask() {
		   public void run() {
			   if (ipInput.getText().equals("")) {
				   timer.cancel();
			   }
			   else if (ipInput.getText().equals("Warten auf Server ...")) {
				   ipInput.setText("Warten auf Server ");
			   }
			   else {
				   ipInput.setText(ipInput.getText() + ".");
			   }
		   }
	   };
	   timer.schedule(task, 0, 250);
   }

   public void processNotification(String type, Object object) {
      if (type.equals("ServerConnected")) {
    	  joinGameAsHumanButton.setEnabled(false);
          createGameAsHumanButton.setEnabled(false);
          joinGameAsAiButton.setEnabled(false);
          createGameAsAiButton.setEnabled(false);
          ipInput.setEnabled(false);

          ipInput.setText("Warten auf Server ");
          animateWaitingForServer();
      }

      else if (type.equals("ConnectionFailed")) {
         ipInput.setText("");
         ipInputPanel.setError("Connection failed");
      }

      else if (type.equals("ReceivedGameData")) {
    	  receivedGameData++;
    	  if (receivedGameData == 2) {
    		  ipInput.setText("");
    		  parent.openGameWindow();
    	  }
      }

      else if (type.equals("GameLoaded")) {
    	  parent.openGameWindow();
      }
   }
}
