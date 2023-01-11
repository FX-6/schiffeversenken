package UserInterface.UIComponents;

import java.awt.Dimension;

public class TextLabel extends UILabel {
   private static final long serialVersionUID = 1L;

   protected Dimension size = new Dimension(itemWidth, itemHeigth);

   public TextLabel(String text, boolean single) {
      super(text);

      if (!single) { size.setSize(itemWidth / 2 - padding / 2, itemHeigth);  }

      this.setMinimumSize(size);
      this.setPreferredSize(size);
      this.setMaximumSize(size);
      this.setSize(size);
   }
}
