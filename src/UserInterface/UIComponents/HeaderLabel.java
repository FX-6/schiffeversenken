package UserInterface.UIComponents;

import java.awt.Dimension;
import java.awt.Font;

/**
 * Wird als Überschrift, die den Userstyles folgt, genutzt.
 */
public class HeaderLabel extends UILabel {
   private static final long serialVersionUID = 1L;

   private Font font = new Font("Titel", Font.BOLD, fontSizeLarge);
   protected Dimension size = new Dimension(itemWidth, itemHeigth);

   /**
    * Erstellt ein Label mit den Userstyles, text und passender Größe.
    *
    * @param text Text den das Label zeigen soll
    * @param single <code>true</code> wenn es alleine in einer Zeile steht, ansonsten <code>false</code>
    */
   public HeaderLabel(String text, boolean single) {
      super(text);

      if (!single) { size.setSize(itemWidth / 2 - padding / 2, size.getHeight()); }

      this.setMinimumSize(size);
      this.setPreferredSize(size);
      this.setMaximumSize(size);
      this.setSize(size);
      this.setFont(font);
      this.setHorizontalAlignment(CENTER);
   }
}
