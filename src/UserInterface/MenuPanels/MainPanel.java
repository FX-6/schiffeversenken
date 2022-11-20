package UserInterface.MenuPanels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import Schiffeversenken.GameType;
import UserInterface.Menu;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4275588428062437239L;

	Menu parent;
	
	public MainPanel(Menu parent) {
		this.parent = parent;
		setup();
		fillWithContent();
	}
	
	private void setup() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	private void fillWithContent() {		
		JButton singlePlayer = new MenuButton("Einzelspieler");
		singlePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
			}
		});
		
		JButton multiPlayerNetwork = new MenuButton("Mehrspieler");
		multiPlayerNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
				parent.showNetworkGame(GameType.NETWORK_CLIENT);
			}
		});
		
		JButton multiPlayerPC = new MenuButton("Mehrspieler KI vs. KI");
		multiPlayerPC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
			}
		});
		
		JButton description = new MenuButton("Spielanleitung");
		description.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
			}
		});
		
		JButton settings = new MenuButton("Einstellungen");
		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
				parent.showSettings();
			}
		});
		
		
		add(Box.createGlue());
		
		add(singlePlayer);
		add(Box.createVerticalStrut(10));
		add(multiPlayerNetwork);
		add(Box.createVerticalStrut(10));
		add(multiPlayerPC);
		add(Box.createVerticalStrut(10));
		add(description);
		add(Box.createVerticalStrut(10));
		add(settings);
		
		add(Box.createGlue());	
	}
	
	
	
	
	
	private class MenuButton extends JButton {
		
		private static final long serialVersionUID = -513454326770164185L;
		Font font = new Font("Titel", Font.BOLD, 25);
		Dimension size = new Dimension(500, 50);
		
		MenuButton(String titel) {
			super(titel);
			setFont(font);
			setMinimumSize(size);
			setPreferredSize(size);
			setMaximumSize(size);
			setAlignmentX(CENTER_ALIGNMENT);
		}
	}
	
}
