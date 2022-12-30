package UserInterface.UIComponents;

import java.awt.Dimension;

public class MenuButton extends InputButton {
   private static final long serialVersionUID = 1L;

   private Dimension size = new Dimension(75, 25);

   public MenuButton() {
      super("Menü", true);
      setup();
   }

   private void setup() {
      setMinimumSize(size);
      setPreferredSize(size);
      setMaximumSize(size);
      setBorder(new RoundedBorder(borderRadius, borderWidth, borderColor));
   }
}
