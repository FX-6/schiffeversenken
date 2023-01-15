package UserInterface.MenuPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import Schiffeversenken.SettingsHandler;
import UserInterface.Menu;
import UserInterface.UIComponents.BackgroundPanel;
import UserInterface.UIComponents.InputButton;
import UserInterface.UIComponents.InputComboBox;
import UserInterface.UIComponents.InputPanel;
import UserInterface.UIComponents.InputTextField;
import UserInterface.UIComponents.MenuButton;
import UserInterface.UIComponents.WrapperPanel;

public class SettingsPanel extends BackgroundPanel {
   private static final long serialVersionUID = 1L;

   Menu parent;

   public SettingsPanel(Menu parent) {
      this.parent = parent;

      // fill with content
      WrapperPanel wrapperPanel = new WrapperPanel();

      // theme input
      InputPanel themeInputPanel = new InputPanel("Themename", true);
      JComboBox<String> themeInput = new InputComboBox<String>(SettingsHandler.getThemes());
      themeInputPanel.add(themeInput);
      GridBagConstraints themeInputPanelConstraints = defaultConstraints;
      themeInputPanelConstraints.gridy = 0;
      wrapperPanel.add(themeInputPanel, themeInputPanelConstraints);

      // background color input
      InputPanel backgroundInputPanel = new InputPanel("Hintergrundfarbe", false);
      JTextField backgroundInput = new InputTextField();
      backgroundInput.setText(SettingsHandler.getSettingString("color.background"));
      backgroundInputPanel.add(backgroundInput);
      GridBagConstraints backgroundInputPanelConstraints = doubleFirstConstraints;
      backgroundInputPanelConstraints.gridy = 1;
      wrapperPanel.add(backgroundInputPanel, backgroundInputPanelConstraints);

      // font color input
      InputPanel foregroundInputPanel = new InputPanel("Schriftfarbe", false);
      JTextField foregroundInput = new InputTextField();
      foregroundInput.setText(SettingsHandler.getSettingString("color.foreground"));
      foregroundInputPanel.add(foregroundInput);
      GridBagConstraints foregroundInputPanelConstraints = doubleSecondConstraints;
      foregroundInputPanelConstraints.gridy = 1;
      wrapperPanel.add(foregroundInputPanel, foregroundInputPanelConstraints);

      // button background color input
      InputPanel buttonBackgroundInputPanel = new InputPanel("Hgfarbe (Button)", false);
      JTextField buttonBackgroundInput = new InputTextField();
      buttonBackgroundInput.setText(SettingsHandler.getSettingString("color.button.background"));
      buttonBackgroundInputPanel.add(buttonBackgroundInput);
      GridBagConstraints buttonBackgroundInputPanelConstraints = doubleFirstConstraints;
      buttonBackgroundInputPanelConstraints.gridy = 2;
      wrapperPanel.add(buttonBackgroundInputPanel, buttonBackgroundInputPanelConstraints);

      // button font color input
      InputPanel buttonForegroundInputPanel = new InputPanel("Schriftfarbe (Button)", false);
      JTextField buttonForegroundInput = new InputTextField();
      buttonForegroundInput.setText(SettingsHandler.getSettingString("color.button.foreground"));
      buttonForegroundInputPanel.add(buttonForegroundInput);
      GridBagConstraints buttonForegroundInputPanelConstraints = doubleSecondConstraints;
      buttonForegroundInputPanelConstraints.gridy = 2;
      wrapperPanel.add(buttonForegroundInputPanel, buttonForegroundInputPanelConstraints);

      // border color input
      InputPanel borderInputPanel = new InputPanel("Borderfarbe", false);
      JTextField borderInput = new InputTextField();
      borderInput.setText(SettingsHandler.getSettingString("color.border"));
      borderInputPanel.add(borderInput);
      GridBagConstraints borderInputPanelConstraints = doubleFirstConstraints;
      borderInputPanelConstraints.gridy = 3;
      wrapperPanel.add(borderInputPanel, borderInputPanelConstraints);

      // error color input
      InputPanel errorInputPanel = new InputPanel("Errorfarbe", false);
      JTextField errorInput = new InputTextField();
      errorInput.setText(SettingsHandler.getSettingString("color.error"));
      errorInputPanel.add(errorInput);
      GridBagConstraints errorInputPanelConstraints = doubleSecondConstraints;
      errorInputPanelConstraints.gridy = 3;
      wrapperPanel.add(errorInputPanel, errorInputPanelConstraints);

      // save button
      JButton saveButton = new InputButton("Speichern", true);
      saveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            SettingsHandler.setSettingString("settings.theme.path", (String)themeInput.getSelectedItem());

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
      GridBagConstraints saveButtonConstraints = defaultConstraints;
      saveButtonConstraints.gridy = 5;
      wrapperPanel.add(saveButton, saveButtonConstraints);

      // wrapperPanel
      wrapperPanel.setLocation(170, 68);
      this.add(wrapperPanel);

      // menu button
      JButton menuButton = new MenuButton();
      menuButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            parent.showMenu();
         }
      });
      this.add(menuButton);

      // recenter wrapperPanel
      this.addComponentListener(new ComponentAdapter() {
         public void componentResized(ComponentEvent e) {
            wrapperPanel.setLocation(e.getComponent().getWidth() / 2 - wrapperPanel.getWidth() / 2, e.getComponent().getHeight() / 2 - wrapperPanel.getHeight() / 2);
         }
      });
   }
}
