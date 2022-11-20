package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import UserInterface.Menu;

public class SettingsPanel extends JPanel {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7953362830551040945L;
	
	Menu parent;

	public SettingsPanel(Menu parent) {
		this.parent = parent;
		setup();
		fillWithContent();
	}
	
	private void setup() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	private void fillWithContent() {
		JButton menu = new MenuButton();
		
		add(menu);
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
