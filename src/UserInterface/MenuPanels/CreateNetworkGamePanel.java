package UserInterface.MenuPanels;

import java.awt.Dimension;
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
import Schiffeversenken.GameType;
import Schiffeversenken.Main;
import UserInterface.Menu;

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
	
	private JButton start;
	
	public CreateNetworkGamePanel(Menu parent, GameType type) {
		this.parent = parent;
		setup();
		fillWithContent();
		
		parent.createGame(4, type, null);
	}
	
	private void setup() {
		NotificationCenter.addObserver("ClientConnected", this);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	private void fillWithContent() {		
		JButton menu = new MenuButton();
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Main.currentGame.exit();
			}
		});
		
		SpinnerNumberModel spinner = new SpinnerNumberModel(5, 5, 25, 1);
		JSpinner pitchSize = new JSpinner(spinner);
		pitchSize.setMaximumSize(new Dimension(parent.getMaximumSize().width, 30));
		pitchSize.setAlignmentX(LEFT_ALIGNMENT);
		
		
		JPanel horizontal = new JPanel();
		horizontal.setLayout(new BoxLayout(horizontal, BoxLayout.LINE_AXIS));
		horizontal.setMaximumSize(new Dimension(parent.getMaximumSize().width, 30));
		
		JSpinner size2 = new JSpinner();
		JSpinner size3 = new JSpinner();
		JSpinner size4 = new JSpinner();
		JSpinner size5 = new JSpinner();
		
		horizontal.add(Box.createHorizontalStrut(10));
		horizontal.add(size2);
		horizontal.add(Box.createGlue());
		horizontal.add(size3);
		horizontal.add(Box.createGlue());
		horizontal.add(size4);
		horizontal.add(Box.createGlue());
		horizontal.add(size5);
		horizontal.add(Box.createHorizontalStrut(10));
		horizontal.setAlignmentX(LEFT_ALIGNMENT);
		
		
		start = new JButton("Spiel starten");
		start.setEnabled(false);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Main.currentGame.setPitchSize(Integer.parseInt(pitchSize.getValue().toString()));
				
				int[] ships = new int[4];
				ships[0] = Integer.parseInt(size2.getValue().toString());
				ships[1] = Integer.parseInt(size3.getValue().toString());
				ships[2] = Integer.parseInt(size4.getValue().toString());
				ships[3] = Integer.parseInt(size5.getValue().toString());
				Main.currentGame.setShips(ships);
				Main.currentGame.transmittSizeAndShips();
				
				parent.openGameWindow();
			}
		});
		
		add(menu);
		add(Box.createGlue());
		add(pitchSize);
		add(Box.createGlue());
		add(horizontal);
		add(Box.createGlue());
		add(start);
		add(Box.createVerticalStrut(10));
	}
	
	public void processNotification(String type, Object object) {
		if (type.equals("ClientConnected")) {
			start.setEnabled(true);
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
