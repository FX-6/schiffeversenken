package UserInterface.UIComponents;

import java.awt.Graphics;

import Schiffeversenken.SettingsHandler;

/*
 * Wird als Hintergrund genutzt, rendert ein Bild gekachelt.
 */
public class BackgroundPanel extends UIPanel {
   private static final long serialVersionUID = 1L;

   protected String backgroundImage = "image_water";

   /**
    * Standard Hintergrund mit Wasser
    */
   public BackgroundPanel() {
      super();

      this.setLayout(null);
   }

   /**
    * Hinterund mit bestimmten Bild.
    *
    * @param backgroundImage Name des Hintergrundbilds
    */
   public BackgroundPanel(String backgroundImage) {
      super();

      this.backgroundImage = backgroundImage;
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      for (int i = 0; i <= (this.getSize().height / imageSize); i++) {
         for (int j = 0; j <= (this.getSize().width / imageSize); j++) {
            g.drawImage(SettingsHandler.getImage(backgroundImage), j * imageSize, i * imageSize, null);
         }
      }
   }
}
