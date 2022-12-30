package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Notifications.Notification;
import Notifications.NotificationCenter;
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

public class NetworkGamePanel extends BackgroundPanel implements Notification {

	/**
	 *
	 */
	private static final long serialVersionUID = 1743610007354983145L;

	private Menu parent;
	private GameType type;

	private InputPanel ipInputPanel;

	private JTextField ipInput;
	private JButton joinGameAsHumanButton;
	private JButton joinGameAsAiButton;
	private JButton createGameAsHumanButton;
	private JButton createGameAsAiButton;
	private JButton menuButton;

	public NetworkGamePanel(Menu parent, GameType type) {
		this.parent = parent;
		this.type = type;
		setup();
		fillWithContent();
	}

	private void setup() {
		NotificationCenter.addObserver("ServerConnected", this);
		NotificationCenter.addObserver("ConnectionFailed", this);
	}

	private void fillWithContent() {
      JPanel wrapperPanel = new WrapperPanel();

      // IP input
      ipInputPanel = new InputPanel("Host IP");
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

      // Join game as human button
      joinGameAsHumanButton = new InputButton("Mensch");
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

      // Join game as ai button
      joinGameAsAiButton = new InputButton("Bot");
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

      // Create game as human button
      createGameAsHumanButton = new InputButton("Mensch");
      createGameAsHumanButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
            parent.showCreateNetworkGame(GameType.NETWORK_SERVER);
         }
      });

      // Create game as ai button
      createGameAsAiButton = new InputButton("Bot");
      createGameAsAiButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
            parent.showCreateNetworkGame(GameType.NETWORK_AI_SERVER);
         }
      });

      // Join game button rows
      JPanel joinGameButtonRow = new DualRowPanel();
      joinGameButtonRow.add(joinGameAsHumanButton);
      joinGameButtonRow.add(joinGameAsAiButton);
      JPanel createGameButtonRow = new DualRowPanel();
      createGameButtonRow.add(createGameAsHumanButton);
      createGameButtonRow.add(createGameAsAiButton);

      // Add all to wrapper
      wrapperPanel.add(ipInputPanel);
      wrapperPanel.add(new HeaderLabel("Spiel beitreten als"));
      wrapperPanel.add(joinGameButtonRow);
      wrapperPanel.add(new HeaderLabel("Spiel erstellen als"));
      wrapperPanel.add(createGameButtonRow);

      // MenuButton
      menuButton = new MenuButton();
      menuButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showMenu();
         }
      });

      // add all to window
      add(menuButton);
      add(Box.createGlue());
      add(wrapperPanel);
      add(Box.createGlue());
   }

	private void animateConnecting() {
		menuButton.setEnabled(false);
		joinGameAsHumanButton.setEnabled(false);
		createGameAsHumanButton.setEnabled(false);
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
					ipInput.setEnabled(true);
				}
				else if (ipInput.getText().equals(text + " ...")) {
					ipInput.setText(text + " ");
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
			parent.openGameWindow();
		}
		if (type.equals("ConnectionFailed")) {
			ipInput.setText("");
			ipInputPanel.setError("Verbindung fehlgeschlagen");
		}
	}
}
