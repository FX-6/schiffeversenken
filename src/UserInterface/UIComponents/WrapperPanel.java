package UserInterface.UIComponents;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.GridLayout;

public class WrapperPanel extends UIPanel {
   private Dimension size = new Dimension((2 * borderWidth + 2 * padding) + itemWidth, (2 * borderWidth + 2 * padding));
   private GridLayout gridLayout = new GridLayout(0, 1, padding, padding);

   public WrapperPanel() {
      super();
      setup();
   }

   private void setup() {
      setLayout(gridLayout);
      setMinimumSize(size);
      setPreferredSize(size);
      setMaximumSize(size);
      setBorder(javax.swing.BorderFactory.createCompoundBorder(
         new RoundedBorder(borderRadius, borderWidth, borderColor),
         new javax.swing.border.EmptyBorder(padding, padding, padding, padding)
      ));
   }

   public Component add(Component comp) {
      super.add(comp);
      size.setSize(size.getWidth(), size.getHeight() + itemHeigth);
      return comp;
   }
}
