package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
	private JButton joinGameButton;
	private JButton createGameButton;
	private JButton menu;

	public NetworkGamePanel(Menu parent, GameType type) {
		this.parent = parent;
		this.type = type;
		setup();
		fillWithContent();
	}

	private void setup() {
		NotificationCenter.addObserver("ServerConnected", this);
		NotificationCenter.addObserver("ConnectionFailed", this);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
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

      // Join Game und Create Game Buttons
      joinGameButton = new InputButton("Spiel beitreten");
      joinGameButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (SettingsHandler.validateIP(ipInput.getText())) {
               ipInputPanel.setError("");
               System.out.println(e.getActionCommand());
               Main.hostAddress = ipInput.getText();
               animateConnecting();
               parent.createGame(4, type);
            } else {
               ipInputPanel.setError("Invalide IP");
            }
         }
      });

      createGameButton = new InputButton("Spiel erstellen");
      createGameButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
            parent.showCreateNetworkGame(GameType.NETWORK_SERVER);
         }
      });

      // Alles in Wrapper einfügen
      wrapperPanel.add(ipInputPanel);
      wrapperPanel.add(joinGameButton);
      wrapperPanel.add(createGameButton);

      // Menu Button
      menu = new MenuButton();
      menu.setAlignmentX(CENTER_ALIGNMENT);
      menu.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showMenu();
         }
      });

      // Wrapper und rest einfügen
      add(menu);
      add(Box.createGlue());
      add(wrapperPanel);
      add(Box.createGlue());
   }

	private void animateConnecting() {
		menu.setEnabled(false);
		joinGameButton.setEnabled(false);
		createGameButton.setEnabled(false);
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
					menu.setEnabled(true);
					joinGameButton.setEnabled(true);
					createGameButton.setEnabled(true);
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
