package UserInterface.UIComponents;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import Schiffeversenken.SettingsHandler;

public class InputButton extends JButton {
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

   protected Dimension size = new Dimension(itemWidth, itemHeigth);
   protected Font defaultFont = new Font("Titel", Font.PLAIN, fontSizeLarge);
   protected Font hoverFont = new Font("Titel", Font.BOLD, fontSizeLarge);

   public InputButton(String text, boolean single) {
      super(text);

      if (!single) { size.setSize(itemWidth / 2 - padding / 2, size.getHeight()); }
      this.setMinimumSize(size);
      this.setPreferredSize(size);
      this.setMaximumSize(size);
      this.setSize(size);
      this.setBackground(buttonBackground);
      this.setForeground(buttonFontColor);
      this.setFont(defaultFont);
      this.setBorder(new RoundedBorder(borderRadius, borderWidth, borderColor));

      this.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            setFont(hoverFont);
         }

         public void mouseExited(java.awt.event.MouseEvent evt) {
            setFont(defaultFont);
         }
      });
   }
}
