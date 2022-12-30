package UserInterface.UIComponents;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class WrapperPanel extends UIPanel {
   private static final long serialVersionUID = 1L;

   private Dimension size = new Dimension((2 * borderWidth + 2 * padding) + itemWidth, (2 * borderWidth + 2 * padding));
   private BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
   private GridLayout gridLayout = new GridLayout(0, 1, padding, padding);
   private JPanel containerPanel = new UIPanel();

   public WrapperPanel() {
      super();
      setup();
   }

   private void setup() {
      this.setLayout(boxLayout);
      this.setOpaque(false);

      containerPanel.setLayout(gridLayout);
      containerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
      containerPanel.setMinimumSize(size);
      containerPanel.setPreferredSize(size);
      containerPanel.setMaximumSize(size);
      containerPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
         new RoundedBorder(borderRadius, borderWidth, borderColor),
         new javax.swing.border.EmptyBorder(padding, padding, padding, padding)
      ));

      this.originalAdd(Box.createHorizontalGlue());
      this.originalAdd(containerPanel);
      this.originalAdd(Box.createHorizontalGlue());
   }

   private Component originalAdd(Component comp) {
      super.add(comp);
      return comp;
   }

   public Component add(Component comp) {
      containerPanel.add(comp);
      size.setSize(size.getWidth(), size.getHeight() + itemHeigth);
      return comp;
   }
}
