package UserInterface.UIComponents;

import java.awt.Dimension;

// TODO: Positioning (20, 20)

public class MenuButton extends InputButton {
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
      setLocation(20, 20); // Kann wahrscheinlich nicht im Constructor stehen
   }
}
