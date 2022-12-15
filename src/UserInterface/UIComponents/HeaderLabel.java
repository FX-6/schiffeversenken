package UserInterface.UIComponents;

import java.awt.Font;

public class HeaderLabel extends UILabel {
   private static final long serialVersionUID = 1L;

   private Font font = new Font("Titel", Font.BOLD, fontSizeLarge);

   public HeaderLabel() {
      super();
      setup();
   }

   public HeaderLabel(String text) {
      super(text);
      setup();
   }

   public void setup() {
      setFont(font);
      setHorizontalAlignment(CENTER);
   }
}
