package UserInterface.UIComponents;

import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import Schiffeversenken.Main;
import Schiffeversenken.SettingsHandler;

public class GameMapPanel extends UIPanel {
   private static final long serialVersionUID = 1L;

   private Point prevMouseLocation, windowPosition;
   private double zoomFactor = 1;

   private Image waterImage = SettingsHandler.getImage("image_water");
   private Image zoomedWaterImage = waterImage;

   public GameMapPanel() {
      super();

      this.setLayout(null);
      this.setSize(Main.currentGame.getPitchSize() * imageSize, Main.currentGame.getPitchSize() * imageSize);

      this.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            prevMouseLocation = e.getLocationOnScreen();
            updateWindowPositionVar();
         }
      });

      this.addMouseMotionListener(new MouseMotionAdapter() {
         public void mouseDragged(MouseEvent e) {
            Point currentMouseLocation = e.getLocationOnScreen();
            windowPosition.x += currentMouseLocation.x - prevMouseLocation.x;
            windowPosition.y += currentMouseLocation.y - prevMouseLocation.y;
            prevMouseLocation = currentMouseLocation;
            setLocation(windowPosition.x, windowPosition.y);
         }
      });

      this.addMouseWheelListener(new MouseWheelListener() {
         public void mouseWheelMoved(MouseWheelEvent e) {
            int oldWidth = getWidth();

            if (e.getWheelRotation() < 0) {
               // zoom in
               if (zoomFactor < 2) { zoomFactor += 0.1; }
            } else if (e.getWheelRotation() > 0) {
               // zoom out
               if (zoomFactor > 0.5) { zoomFactor -= 0.1; }
            }

            int zoomedItemSize = (int)(imageSize * zoomFactor);
            zoomedWaterImage = waterImage.getScaledInstance(zoomedItemSize, zoomedItemSize, Image.SCALE_FAST);

            repaint();

            recenter(oldWidth);
         }
      });
   }

   private void updateWindowPositionVar() { this.windowPosition = this.getLocation(); }

   // TODO Fix (Felix)
   private void recenter(int oldWidth) {
      int widthDif = oldWidth - this.getWidth();
      this.setLocation(this.getX() + widthDif, this.getY() + widthDif);
      repaint();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      int zoomedItemSize = (int)(imageSize * zoomFactor);

      this.setSize(zoomedItemSize * Main.currentGame.getPitchSize(), zoomedItemSize * Main.currentGame.getPitchSize());

      for (int row = 0; row < Main.currentGame.getPitchSize(); row++) {
         for (int column = 0; column < Main.currentGame.getPitchSize(); column++) {
            g.drawImage(zoomedWaterImage, row * zoomedItemSize, column * zoomedItemSize, null);
         }
      }
   }
}
