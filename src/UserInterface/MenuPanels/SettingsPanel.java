package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import Schiffeversenken.SettingsHandler;
import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.DualRowPanel;
import UserInterface.UIComponents.HeaderLabel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputComboBox;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputSpinner;
import UserInterface.UIComponents.InputTextField;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.WrapperPanel;

// TODO: Give savebutton function
// TODO: Overflow scroll

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
      JSpinner borderWidthInput = new InputSpinner(new SpinnerNumberModel(SettingsHandler.getSettingInt("border.width"), 0, 4, 1));
      borderWidthInputPanel.add(borderWidthInput);
      InputPanel borderRadiusInputPanel = new InputPanel("Radius");
      JSpinner borderRadiusInput = new InputSpinner(new SpinnerNumberModel(SettingsHandler.getSettingInt("border.radius"), 0, 25, 1));
      borderRadiusInputPanel.add(borderRadiusInput);
      bordersFirstRow.add(borderWidthInputPanel);
      bordersFirstRow.add(borderRadiusInputPanel);

      JLabel themeHeader = new HeaderLabel("Theme");

      InputPanel themeInputPanel = new InputPanel("Themename");
      JComboBox<String> themeInput = new InputComboBox<String>(SettingsHandler.getThemes());
      themeInputPanel.add(themeInput);

      JButton saveButton = new InputButton("Speichern");
      // give savebutton function

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
