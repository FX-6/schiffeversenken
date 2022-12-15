package UserInterface.UIComponents;

import java.awt.GridLayout;

public class DualRowPanel extends UIPanel {
   private static final long serialVersionUID = 1L;

   protected GridLayout dualGridLayout = new GridLayout(1, 2, padding, padding);

   public DualRowPanel() {
      super();
      setup();
   }

   private void setup() {
      setLayout(dualGridLayout);
   }
}
