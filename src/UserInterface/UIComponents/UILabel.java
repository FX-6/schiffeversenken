package UserInterface.UIComponents;

import javax.swing.JLabel;
import java.awt.Color;
import Schiffeversenken.SettingsHandler;

public class UILabel extends JLabel {
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

   public UILabel() {
      super();
      setup();
   }

   public UILabel(String text) {
      super(text);
      setup();
   }

   private void setup() {
      this.setForeground(fontColor);
   }
}
