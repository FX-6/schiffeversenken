package UserInterface.UIComponents;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import Schiffeversenken.SettingsHandler;

public class InputButton extends JButton {
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

   private int fontSize = fontSizeLarge;
   protected Font defaultFont;
   protected Font hoverFont;

   public InputButton() {
      super();
      setup();
   }

   public InputButton(String text) {
      super(text);
      setup();
   }

   public InputButton(String text, boolean small) {
      super(text);
      if (small) {
         this.fontSize = fontSizeSmall;
      }
      setup();
   }

   private void setup() {
      defaultFont = new Font("Titel", Font.PLAIN, fontSize);
      hoverFont = new Font("Titel", Font.BOLD, fontSize);

      setBackground(buttonBackground);
      setForeground(buttonFontColor);
      setFont(defaultFont);
      setBorder(javax.swing.BorderFactory.createCompoundBorder(
         new RoundedBorder(borderRadius, borderWidth, borderColor),
         new javax.swing.border.EmptyBorder(padding, padding, padding, padding)
      ));

      addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            setFont(hoverFont);
         }

         public void mouseExited(java.awt.event.MouseEvent evt) {
            setFont(defaultFont);
         }
      });
   }
}
