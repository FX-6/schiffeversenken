package UserInterface.UIComponents;

import java.awt.Graphics;

import Schiffeversenken.SettingsHandler;

public class BackgroundPanel extends UIPanel {
   public BackgroundPanel() {
      super();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      for (int i = 0; i <= (this.getSize().height / 50); i++) {
         for (int j = 0; j <= (this.getSize().width / 50); j++) {
            g.drawImage(SettingsHandler.getImage("water.png"), j * 50, i * 50, null);
         }
      }
   }
}
