package UserInterface.UIComponents;

import java.awt.GridLayout;

public class DualRowPanel extends UIPanel {
   protected GridLayout dualGridLayout = new GridLayout(1, 2, padding, padding);

   public DualRowPanel() {
      super();
      setup();
   }

   private void setup() {
      setLayout(dualGridLayout);
   }
}
