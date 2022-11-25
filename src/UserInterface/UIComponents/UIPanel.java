package UserInterface.UIComponents;

import javax.swing.JPanel;
import java.awt.Color;

public class UIPanel extends JPanel {
   protected int fontSizeLarge = 25;
   protected int fontSizeSmall = 15;

   protected int padding = 20;

   protected int itemWidth = 500;
   protected int itemHeigth = 80;

   protected int borderRadius = 15;
   protected int borderWidth = 2;

   protected Color backgroundColor = Color.decode("#FFFFFF");
   protected Color borderColor = Color.decode("#000000");
   protected Color fontColor = Color.decode("#FFFFFF");
   protected Color buttonBackground = Color.decode("#FFFFFF");
   protected Color errorColor = Color.decode("#FF0000");

   public UIPanel() {
      super();
      setup();
   }

   private void setup() {
      setBackground(backgroundColor);
   }
}
