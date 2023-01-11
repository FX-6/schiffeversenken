package UserInterface.UIComponents;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import Schiffeversenken.SettingsHandler;

public class UIPanel extends JPanel {
   private static final long serialVersionUID = 1L;

   protected final int imageSize = 100;

   protected int fontSizeLarge = 25;
   protected int fontSizeSmall = 15;

   protected int padding = 20;

   protected int itemWidth = 500;
   protected int itemHeigth = 70;

   protected int borderRadius = SettingsHandler.getSettingInt("border.radius");
   protected int borderWidth = SettingsHandler.getSettingInt("border.width");

   protected Color backgroundColor = Color.decode(SettingsHandler.getSettingString("color.background"));
   protected Color borderColor = Color.decode(SettingsHandler.getSettingString("color.border"));
   protected Color fontColor = Color.decode(SettingsHandler.getSettingString("color.font"));
   protected Color buttonBackground = Color.decode(SettingsHandler.getSettingString("color.button.background"));
   protected Color buttonFontColor = Color.decode(SettingsHandler.getSettingString("color.button.font"));
   protected Color errorColor = Color.decode(SettingsHandler.getSettingString("color.error"));

   public GridBagConstraints defaultConstraints = new GridBagConstraints();
   public GridBagConstraints doubleFirstConstraints = new GridBagConstraints();
   public GridBagConstraints doubleSecondConstraints = new GridBagConstraints();

   public UIPanel() {
      super();

      defaultConstraints.gridx = 0;
      defaultConstraints.gridwidth = 2;
      defaultConstraints.gridheight = 1;
      defaultConstraints.insets = new Insets(0, 0, padding, 0);

      doubleFirstConstraints.gridx = 0;
      doubleFirstConstraints.gridwidth = 1;
      doubleFirstConstraints.gridheight = 1;
      doubleFirstConstraints.insets = new Insets(0, 0, padding, 0);

      doubleSecondConstraints.gridx = 1;
      doubleSecondConstraints.gridwidth = 1;
      doubleSecondConstraints.gridheight = 1;
      doubleSecondConstraints.insets = new Insets(0, padding, padding, 0);
   }
}
