package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Schiffeversenken.GameType;
import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.HeaderLabel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.WrapperPanel;

public class MainPanel extends BackgroundPanel {

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

	private void setup() {}

	private void fillWithContent() {
      JPanel wrapperPanel = new WrapperPanel();

      JLabel header = new HeaderLabel("Schiffeversenken");

      JButton singlePlayer = new InputButton("Einzelspieler");
		singlePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
            parent.showCreateSingleplayerGame(GameType.AI);
			}
		});

		JButton multiPlayerNetwork = new InputButton("Mehrspieler");
		multiPlayerNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
				parent.showNetworkGame(GameType.NETWORK_CLIENT);
			}
		});

		JButton description = new InputButton("Spielanleitung");
		description.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
            parent.showTutorial();
			}
		});

		JButton settings = new InputButton("Einstellungen");
		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println(event.getActionCommand());
				parent.showSettings();
			}
		});

      wrapperPanel.add(header);
      wrapperPanel.add(singlePlayer);
      wrapperPanel.add(multiPlayerNetwork);
      wrapperPanel.add(description);
      wrapperPanel.add(settings);

      // add all to window
      add(Box.createGlue());
      add(wrapperPanel);
      add(Box.createGlue());
	}
}
