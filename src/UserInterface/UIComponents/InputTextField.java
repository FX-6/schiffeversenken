package UserInterface.UIComponents;

import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Font;
import Schiffeversenken.SettingsHandler;

public class InputTextField extends JTextField {
   protected int fontSizeLarge = 25;
   protected int fontSizeSmall = 15;

   protected int padding = 20;

   protected int itemWidth = 500;
   protected int itemHeigth = 80;

   protected int borderRadius = SettingsHandler.getSettingInt("border.radius");
   protected int borderWidth = SettingsHandler.getSettingInt("border.width");

   protected Color backgroundColor = Color.decode(SettingsHandler.getSettingString("color.background"));
   protected Color borderColor = Color.decode(SettingsHandler.getSettingString("color.border"));
   protected Color fontColor = Color.decode(SettingsHandler.getSettingString("color.font"));
   protected Color buttonBackground = Color.decode(SettingsHandler.getSettingString("color.button.background"));
   protected Color buttonFontColor = Color.decode(SettingsHandler.getSettingString("color.button.font"));
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
