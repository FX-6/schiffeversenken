package UserInterface.UIComponents;

import java.awt.Dimension;

public class GameMenuPanel extends WrapperPanel {
   private static final long serialVersionUID = 1L;

   public GameMenuPanel() {
      super();

      this.itemWidth *= 0.7;
      this.itemHeigth *= 0.8;
      this.setSize(2 * borderWidth + 2 * padding + this.itemWidth, this.getWidth());
   }

   public Dimension smallDimension() { return new Dimension(this.itemWidth / 2 - padding / 2, this.itemHeigth); }

   public Dimension largeDimension() { return new Dimension(this.itemWidth, this.itemHeigth); }
}
