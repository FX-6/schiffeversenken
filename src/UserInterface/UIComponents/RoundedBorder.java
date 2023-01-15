package UserInterface.UIComponents;

import javax.swing.border.Border;
import java.awt.*;

/**
 * Wird intern als abgerundete Border genutzt.
 */
public class RoundedBorder implements Border {
   private int borderRadius;
   private int borderWidth;
   private Color borderColor;

   /**
    * Erstellt eine neue runde Border.
    *
    * @param borderRadius Radius der Rundung
    * @param borderWidth Breite der Border
    * @param borderColor Farbe der Border
    */
   public RoundedBorder(int borderRadius, int borderWidth, Color borderColor) {
      this.borderRadius = borderRadius;
      this.borderWidth = borderWidth;
      this.borderColor = borderColor;
   }

   public Insets getBorderInsets(Component comp) {
      return new Insets(this.borderRadius + 1, this.borderRadius + 1, this.borderRadius + 2, this.borderRadius);
   }

   public boolean isBorderOpaque() {
      return true;
   }

   public void paintBorder(Component comp, Graphics g, int x, int y, int width, int height) {
      Graphics2D graphics = (Graphics2D) g;
      graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      graphics.setStroke(new BasicStroke(borderWidth));
      graphics.setColor(borderColor);
      graphics.drawRoundRect(
         x + (borderWidth / 2),
         y + (borderWidth / 2),
         width - (borderWidth / 2 * 3),
         height - (borderWidth / 2 * 3),
         borderRadius,
         borderRadius
      );
   }
}
