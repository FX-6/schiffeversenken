package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Notifications.Notification;
import Notifications.NotificationCenter;
import Schiffeversenken.GameType;
import UserInterface.Menu;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputTextField;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.WrapperPanel;

public class NetworkGamePanel extends JPanel implements Notification {

	/**
	 *
	 */
	private static final long serialVersionUID = 1743610007354983145L;

	private Menu parent;
	private GameType type;

   private InputPanel ipInputPanel;

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
      JTextField ipInput = new InputTextField();
      ipInput.addKeyListener(new KeyListener() {
         @Override
         public void keyTyped(KeyEvent e) {
            if (ipInput.getText().isBlank()) { return; }
            if (e.getKeyChar() == '\n') { parent.createGame(4, type, ipInput.getText()); }
         }

         @Override
         public void keyPressed(KeyEvent e) {}

         @Override
         public void keyReleased(KeyEvent e) {}
      });
      ipInputPanel.add(ipInput);

      // Join Game und Create Game Buttons
      JButton joinGameButton = new InputButton("Spiel beitreten");
      joinGameButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());

            parent.createGame(4, type, ipInput.getText());
         }
      });

      JButton createGameButton = new InputButton("Spiel erstellen");
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
      JButton menu = new MenuButton();
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

	public void processNotification(String type, Object object) {
		if (type.equals("ServerConnected")) {
			parent.openGameWindow();
		}
		if (type.equals("ConnectionFailed")) {
         ipInputPanel.setError("Verbindungsaufbau fehlgeschlagen");
		}
	}
}
