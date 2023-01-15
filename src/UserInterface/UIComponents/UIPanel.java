package UserInterface.UIComponents;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import Schiffeversenken.SettingsHandler;

/**
 * Wird genutzt um die Userstyles und standard Constraints zu laden.
 * Sollte nicht als Komponente in der UI genutzt werden.
 */
public class UIPanel extends JPanel {
   private static final long serialVersionUID = 1L;

   protected int imageSize = SettingsHandler.getSettingInt("settings.image.size");
   protected int fontSizeSmall = SettingsHandler.getSettingInt("settings.fontsize.small");
   protected int fontSizeLarge = SettingsHandler.getSettingInt("settings.fontsize.large");
   protected int itemWidth = SettingsHandler.getSettingInt("settings.items.width");
   protected int itemHeigth = SettingsHandler.getSettingInt("settings.items.height");
   protected int itemSmallWidth = SettingsHandler.getSettingInt("settings.items.small.width");
   protected int itemSmallHeigth = SettingsHandler.getSettingInt("settings.items.small.height");
   protected int padding = SettingsHandler.getSettingInt("settings.items.padding");
   protected int borderWidth = SettingsHandler.getSettingInt("settings.border.width");
   protected int borderRadius = SettingsHandler.getSettingInt("settings.border.radius");

   protected Color backgroundColor = Color.decode(SettingsHandler.getSettingString("color.background"));
   protected Color borderColor = Color.decode(SettingsHandler.getSettingString("color.border"));
   protected Color fontColor = Color.decode(SettingsHandler.getSettingString("color.foreground"));
   protected Color buttonBackground = Color.decode(SettingsHandler.getSettingString("color.button.background"));
   protected Color buttonFontColor = Color.decode(SettingsHandler.getSettingString("color.button.foreground"));
   protected Color errorColor = Color.decode(SettingsHandler.getSettingString("color.error"));

   public GridBagConstraints defaultConstraints = new GridBagConstraints();
   public GridBagConstraints doubleFirstConstraints = new GridBagConstraints();
   public GridBagConstraints doubleSecondConstraints = new GridBagConstraints();

   /**
    * Erstellt ein Panel mit den Userstyles und den standard Constraints.
    */
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
