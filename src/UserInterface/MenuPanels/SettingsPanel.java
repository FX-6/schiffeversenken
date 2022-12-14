package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
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

// TODO: Overflow scroll (Felix)

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

	private void setup() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

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
      foregroundInput.setText(SettingsHandler.getSettingString("color.font"));
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
      buttonForegroundInput.setText(SettingsHandler.getSettingString("color.button.font"));
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

      JLabel borderHeader = new HeaderLabel("Borders");

      JPanel bordersFirstRow = new DualRowPanel();
      InputPanel borderWidthInputPanel = new InputPanel("Breite");
      InputTextField borderWidthInput = new InputTextField();
      borderWidthInputPanel.add(borderWidthInput);
      InputPanel borderRadiusInputPanel = new InputPanel("Radius");
      InputTextField borderRadiusInput = new InputTextField();
      borderRadiusInputPanel.add(borderRadiusInput);
      bordersFirstRow.add(borderWidthInputPanel);
      bordersFirstRow.add(borderRadiusInputPanel);

      JLabel themeHeader = new HeaderLabel("Theme");

      InputPanel themeInputPanel = new InputPanel("Themename");
      JComboBox<String> themeInput = new InputComboBox<String>(SettingsHandler.getThemes());
      themeInputPanel.add(themeInput);

      JButton saveButton = new InputButton("Speichern");
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
               SettingsHandler.setSettingString("color.font", foregroundInput.getText());
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
               SettingsHandler.setSettingString("color.button.font", buttonForegroundInput.getText());
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

            try {
               if (borderWidthInput.getIntValue() < 0) {
                  borderWidthInputPanel.setError("Zu klein");
               } else if (borderWidthInput.getIntValue() > 4) {
                  borderWidthInputPanel.setError("Zu groß");
               } else {
                  borderWidthInputPanel.setError("");
                  SettingsHandler.setSettingString("border.width", borderWidthInput.getStringValue());
               }
            } catch (NumberFormatException err) {
               borderWidthInputPanel.setError("Invalider Input");
            }

            try {
               if (borderRadiusInput.getIntValue() < 0) {
                  borderRadiusInputPanel.setError("Zu klein");
               } else if (borderRadiusInput.getIntValue() > 4) {
                  borderRadiusInputPanel.setError("Zu groß");
               } else {
                  borderRadiusInputPanel.setError("");
                  SettingsHandler.setSettingString("border.width", borderRadiusInput.getStringValue());
               }
            } catch (NumberFormatException err) {
               borderRadiusInputPanel.setError("Invalider Input");
            }
         }
      });

      wrapperPanel.add(colorHeader);
      wrapperPanel.add(colorsFirstRow);
      wrapperPanel.add(colorsSecondRow);
      wrapperPanel.add(colorsThirdRow);
      wrapperPanel.add(borderHeader);
      wrapperPanel.add(bordersFirstRow);
      wrapperPanel.add(themeHeader);
      wrapperPanel.add(themeInputPanel);
      wrapperPanel.add(saveButton);

      JButton menuButton = new MenuButton();
      menuButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showMenu();
         }
      });

		add(menuButton); // Position top left
      menuButton.setLocation(20, 20);
      add(Box.createGlue());
      add(wrapperPanel);
      add(Box.createGlue());
	}
}
