package UserInterface.MenuPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Notifications.Notification;
import Notifications.NotificationCenter;
import Schiffeversenken.GameType;
import UserInterface.Menu;

public class NetworkGamePanel extends JPanel implements Notification {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1743610007354983145L;
	
	private Menu parent;
	private GameType type;
	
	private JLabel conProcess;
	
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
		JButton menu = new MenuButton();
		
		// Beschreibung und Textfeld für IP-Adresse Eingabe
		JPanel horizontal = new JPanel();
		horizontal.setLayout(new BoxLayout(horizontal, BoxLayout.LINE_AXIS));
		horizontal.setMaximumSize(new Dimension(parent.getMaximumSize().width, 30));
		
		JLabel host = new JLabel("Host");
		
		JTextField ipAddress = new JTextField();
		ipAddress.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (ipAddress.getText().isBlank()) {
					return;
				}
				if (conProcess.isVisible()) conProcess.setVisible(false);
				if (e.getKeyChar() == '\n') {
					showConProcessLabel();
					parent.createGame(4, type, ipAddress.getText()); 
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		horizontal.add(Box.createHorizontalStrut(10));
		horizontal.add(host);
		horizontal.add(Box.createHorizontalStrut(10));
		horizontal.add(ipAddress);
		horizontal.add(Box.createHorizontalStrut(10));
		horizontal.setAlignmentX(LEFT_ALIGNMENT);
		
		
		// Wird aktiviert, falls der Verbindungsaufbau fehlgeschlagen ist
		JPanel processPanel = new JPanel();
		processPanel.setLayout(new BoxLayout(processPanel, BoxLayout.LINE_AXIS));
		processPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		conProcess = new JLabel("verbinden ...");
		conProcess.setVisible(false);
		conProcess.setAlignmentX(CENTER_ALIGNMENT);
		
		processPanel.add(Box.createGlue());
		processPanel.add(conProcess);
		processPanel.add(Box.createGlue());
		
		
		// Buttons um Spiel beizutreten oder selbst eins zu hosten
		JPanel horizontal2 = new JPanel();
		horizontal2.setLayout(new BoxLayout(horizontal2, BoxLayout.LINE_AXIS));
		
		JButton join = new JButton("Spiel beitreten");
		join.setFont(new Font("Subtitel", Font.BOLD, 20));
		join.setMaximumSize(new Dimension(parent.getMaximumSize().width, 50));
		join.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
				
				showConProcessLabel();
				parent.createGame(4, type, ipAddress.getText());
			}
		});
		
		JButton create = new JButton("Spiel erstellen");
		create.setFont(new Font("Titel", Font.BOLD, 20));
		create.setMaximumSize(new Dimension(parent.getMaximumSize().width, 50));
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
				parent.showCreateNetworkGame(GameType.NETWORK_SERVER);
			}
		});
		
		horizontal2.add(Box.createHorizontalStrut(10));
		horizontal2.add(join);
		horizontal2.add(create);
		horizontal2.add(Box.createHorizontalStrut(10));
		horizontal2.setAlignmentX(LEFT_ALIGNMENT);
		
		// Alles einfügen
		add(menu);
		add(Box.createGlue());
		add(horizontal);
		add(processPanel);
		add(Box.createGlue());
		add(horizontal2);
	}
	
	
	
	
	
	private void showConProcessLabel() {
		conProcess.setText("verbinden ...");
		conProcess.setForeground(Color.black);
		conProcess.setVisible(true);
	}
	
	
	
	
	
	
	
	public void processNotification(String type, Object object) {
		if (type.equals("ServerConnected")) {
			parent.openGameWindow();
		}
		if (type.equals("ConnectionFailed")) {
			conProcess.setText("Verbindungsaufbau fehlgeschlagen");
			conProcess.setForeground(Color.red);
		}
	}

	
	
	
	
	
	
	private class MenuButton extends JButton {
		private static final long serialVersionUID = 2634914788445693027L;

		MenuButton() {
			super("Menü");
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					System.out.println(event.getActionCommand());
					parent.showMenu();
				}
			});
		}
	}

}
