package UserInterface.UIComponents;

import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Font;
import Schiffeversenken.SettingsHandler;

public class InputTextField extends JTextField {
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

   public InputTextField() {
      super();
      setup();
   }

   private void setup() {
      setFont(new Font("Titel", Font.PLAIN, fontSizeLarge));
      setBackground(buttonBackground);
      setForeground(buttonFontColor);
      setBorder(new MatteBorder(0, 0, borderWidth, 0, borderColor));
   }

   public void setValue(String value) {
      this.setText(value);
   }

   public void setValue(int value) {
      this.setText(Integer.toString(value));
   }

   public String getStringValue() {
      return this.getText();
   }

   public int getIntValue() {
      return Integer.parseInt(this.getStringValue());
   }
}
