package UserInterface.UIComponents;

import java.awt.Dimension;
import java.awt.Font;

public class HeaderLabel extends UILabel {
   private static final long serialVersionUID = 1L;

   private Font font = new Font("Titel", Font.BOLD, fontSizeLarge);
   protected Dimension size = new Dimension(itemWidth, itemHeigth);

   public HeaderLabel() {
      super();
      setup();
   }

   public HeaderLabel(String text) {
      super(text);
      setup();
   }

   public HeaderLabel(String text, boolean single) {
      super(text);

      if (!single) { size.setSize(itemWidth / 2 - padding / 2, size.getHeight()); }

      setup();
   }

   public void setup() {
      this.setMinimumSize(size);
      this.setPreferredSize(size);
      this.setMaximumSize(size);
      this.setSize(size);
      this.setFont(font);
      this.setHorizontalAlignment(CENTER);
   }
}
