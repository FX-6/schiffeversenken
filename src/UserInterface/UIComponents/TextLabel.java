package UserInterface.UIComponents;

import java.awt.Dimension;

/**
 * Wird als standard Textlabel, das den Userstyles folgt, genutzt.
 */
public class TextLabel extends UILabel {
   private static final long serialVersionUID = 1L;

   protected Dimension size = new Dimension(itemWidth, itemHeigth);

   /**
    * Erstellt ein Label mit den Userstyles, text und passender Größe.
    *
    * @param text Text den das Label zeigen soll
    * @param single <code>true</code> wenn es alleine in einer Zeile steht, ansonsten <code>false</code>
    */
   public TextLabel(String text, boolean single) {
      super(text);

      if (!single) { size.setSize(itemWidth / 2 - padding / 2, itemHeigth);  }

      this.setMinimumSize(size);
      this.setPreferredSize(size);
      this.setMaximumSize(size);
      this.setSize(size);
   }
}
