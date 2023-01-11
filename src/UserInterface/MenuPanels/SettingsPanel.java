package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Schiffeversenken.SettingsHandler;
import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.DualRowPanel;
import UserInterface.UIComponents.HeaderLabel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputComboBox;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputTextField;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.WrapperPanel;

// TODO Overflow scroll (Felix)

public class SettingsPanel extends BackgroundPanel {

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

	private void setup() {}

	private void fillWithContent() {
      JPanel wrapperPanel = new WrapperPanel();

      JLabel colorHeader = new HeaderLabel("Farben");

      JPanel colorsFirstRow = new DualRowPanel();
      InputPanel backgroundInputPanel = new InputPanel("Hintergrund");
      JTextField backgroundInput = new InputTextField();
      backgroundInput.setText(SettingsHandler.getSettingString("color.background"));
      backgroundInputPanel.add(backgroundInput);
      InputPanel foregroundInputPanel = new InputPanel("Schrift");
      JTextField foregroundInput = new InputTextField();
      foregroundInput.setText(SettingsHandler.getSettingString("color.foreground"));
      foregroundInputPanel.add(foregroundInput);
      colorsFirstRow.add(backgroundInputPanel);
      colorsFirstRow.add(foregroundInputPanel);

      JPanel colorsSecondRow = new DualRowPanel();
      InputPanel buttonBackgroundInputPanel = new InputPanel("Button - Hintergrund");
      JTextField buttonBackgroundInput = new InputTextField();
      buttonBackgroundInput.setText(SettingsHandler.getSettingString("color.button.background"));
      buttonBackgroundInputPanel.add(buttonBackgroundInput);
      InputPanel buttonForegroundInputPanel = new InputPanel("Button - Schrift");
      JTextField buttonForegroundInput = new InputTextField();
      buttonForegroundInput.setText(SettingsHandler.getSettingString("color.button.foreground"));
      buttonForegroundInputPanel.add(buttonForegroundInput);
      colorsSecondRow.add(buttonBackgroundInputPanel);
      colorsSecondRow.add(buttonForegroundInputPanel);

      JPanel colorsThirdRow = new DualRowPanel();
      InputPanel borderInputPanel = new InputPanel("Border");
      JTextField borderInput = new InputTextField();
      borderInput.setText(SettingsHandler.getSettingString("color.border"));
      borderInputPanel.add(borderInput);
      InputPanel errorInputPanel = new InputPanel("Error");
      JTextField errorInput = new InputTextField();
      errorInput.setText(SettingsHandler.getSettingString("color.error"));
      errorInputPanel.add(errorInput);
      colorsThirdRow.add(borderInputPanel);
      colorsThirdRow.add(errorInputPanel);

      // save button
      JButton saveButton = new InputButton("Speichern", true);
      saveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (SettingsHandler.validateHEXColor(backgroundInput.getText())) {
               backgroundInputPanel.setError("");
               SettingsHandler.setSettingString("color.background", backgroundInput.getText());
            } else {
               backgroundInputPanel.setError("Invalider Input");
            }

            if (SettingsHandler.validateHEXColor(backgroundInput.getText())) {
               foregroundInputPanel.setError("");
               SettingsHandler.setSettingString("color.foreground", foregroundInput.getText());
            } else {
               foregroundInputPanel.setError("Invalider Input");
            }

            if (SettingsHandler.validateHEXColor(backgroundInput.getText())) {
               buttonBackgroundInputPanel.setError("");
               SettingsHandler.setSettingString("color.button.background", buttonBackgroundInput.getText());
            } else {
               buttonBackgroundInputPanel.setError("Invalider Input");
            }

            if (SettingsHandler.validateHEXColor(backgroundInput.getText())) {
               buttonForegroundInputPanel.setError("");
               SettingsHandler.setSettingString("color.button.foreground", buttonForegroundInput.getText());
            } else {
               buttonForegroundInputPanel.setError("Invalider Input");
            }

            if (SettingsHandler.validateHEXColor(backgroundInput.getText())) {
               errorInputPanel.setError("");
               SettingsHandler.setSettingString("color.error", errorInput.getText());
            } else {
               errorInputPanel.setError("Invalider Input");
            }

            if (SettingsHandler.validateHEXColor(backgroundInput.getText())) {
               borderInputPanel.setError("");
               SettingsHandler.setSettingString("color.border", borderInput.getText());
            } else {
               borderInputPanel.setError("Invalider Input");
            }
         }
      });

      // wrapperPanel
      wrapperPanel.setLocation(170, 68);
      this.add(wrapperPanel);

      JButton menuButton = new MenuButton();
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
}
