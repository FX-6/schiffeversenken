package UserInterface.UIComponents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;

public class WrapperPanel extends UIPanel {
   private static final long serialVersionUID = 1L;

   protected Dimension size = new Dimension(itemWidth + 2 * borderWidth + 2 * padding, 2 * borderWidth + padding);

   public WrapperPanel() {
      super();

      this.setLayout(new GridBagLayout());
      this.setMaximumSize(size);
      this.setPreferredSize(size);
      this.setMaximumSize(size);
      this.setSize(size);
      this.setBackground(backgroundColor);
      this.setBorder(BorderFactory.createCompoundBorder(
         new RoundedBorder(borderRadius, borderWidth, borderColor),
         new EmptyBorder(padding, padding, 0, padding)
      ));
   }

   public Component add(Component comp, GridBagConstraints c) {
      super.add(comp, c);

      if (c.insets.left == 0) {
         size.setSize(this.getWidth(), size.getHeight() + comp.getHeight() + padding);
         this.setMinimumSize(size);
         this.setPreferredSize(size);
         this.setMaximumSize(size);
         this.setSize(size);
      }

      return comp;
   }
}
