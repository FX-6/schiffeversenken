package UserInterface.UIComponents;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import Schiffeversenken.SettingsHandler;

public class InputButton extends JButton {
   private static final long serialVersionUID = 1L;

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
