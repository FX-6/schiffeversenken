package UserInterface.UIComponents;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;

public class InputButton extends JButton {
   protected int fontSizeLarge = 25;
   protected int fontSizeSmall = 15;

   protected int padding = 20;

   protected int itemWidth = 500;
   protected int itemHeigth = 80;

   protected int borderRadius = 15;
   protected int borderWidth = 2;

   protected Color backgroundColor = Color.decode("#FFFFFF");
   protected Color borderColor = Color.decode("#000000");
   protected Color fontColor = Color.decode("#FFFFFF");
   protected Color buttonBackground = Color.decode("#FFFFFF");
   protected Color errorColor = Color.decode("#FF0000");

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

      setBackground(backgroundColor);
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
