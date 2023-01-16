package UserInterface.UIComponents;

import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import Schiffeversenken.Main;
import Schiffeversenken.SettingsHandler;
import Schiffeversenken.Ship;

public class GameMapPanel extends UIPanel {
   private static final long serialVersionUID = 1L;

   private Point prevMouseLocation, windowPosition;
   private double zoomFactor = 1;
   private boolean inMatch = false;
   private boolean viewingSelf = true;

   private Image cloudImage = SettingsHandler.getImage("image_clouds");
   private Image zoomedCloudImage = cloudImage;
   private Image waterImage = SettingsHandler.getImage("image_water");
   private Image zoomedWaterImage = waterImage;
   private Image destroyedShipImage = SettingsHandler.getImage("image_ship_destroyed");
   private Image zoomedDestroyedShipImage = destroyedShipImage;

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
            zoomedCloudImage = cloudImage.getScaledInstance(zoomedItemSize, zoomedItemSize, Image.SCALE_FAST);
            zoomedWaterImage = waterImage.getScaledInstance(zoomedItemSize, zoomedItemSize, Image.SCALE_FAST);
            zoomedDestroyedShipImage = destroyedShipImage.getScaledInstance(zoomedItemSize, zoomedItemSize, Image.SCALE_FAST);

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

   public void finishedPlacing() { inMatch = true; repaint(); }

   public void changeDisplayedPlayer() { viewingSelf = !viewingSelf; repaint(); }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      Graphics2D g2d = (Graphics2D)g;

      int zoomedItemSize = (int)(imageSize * zoomFactor);

      this.setSize(zoomedItemSize * Main.currentGame.getPitchSize(), zoomedItemSize * Main.currentGame.getPitchSize());

      if (inMatch && viewingSelf) {
         for (int row = 0; row < Main.currentGame.getPitchSize(); row++) {
            for (int column = 0; column < Main.currentGame.getPitchSize(); column++) {
               g2d.drawImage(zoomedWaterImage, row * zoomedItemSize, column * zoomedItemSize, null);
            }
         }


      } else if (inMatch && !viewingSelf) {
         // -2 = part of destroyed ship; -1 = not shot yet; 0 = water; 1 = hit; 2 = destroyed ship
         int[][] pointsShot = Main.currentGame.getPlayer1().getPointsShot();

         for (int xCord = 0; xCord < pointsShot.length; xCord++) {
            for (int yCord = 0; yCord < pointsShot.length; yCord++) {
               if (pointsShot[xCord][yCord] == -1) {
                  // not shot yet
                  g2d.drawImage(zoomedCloudImage, xCord * zoomedItemSize, yCord * zoomedItemSize, null);
               } else if (pointsShot[xCord][yCord] == 0) {
                  // shot but water
                  g2d.drawImage(zoomedWaterImage, xCord * zoomedItemSize, yCord * zoomedItemSize, null);
               } else if (pointsShot[xCord][yCord] == 1) {
                  // shot and hit
                  g2d.drawImage(zoomedDestroyedShipImage, xCord * zoomedItemSize, yCord * zoomedItemSize, null);
               } else if (pointsShot[xCord][yCord] == 2) {
                  // shot and kill
                  int killedSize = 2;
                  int killedOrientation = 0;

                  // get root of destroyed ship
                  int killedRootX = xCord, killedRootY = yCord;
                  boolean searchingX = true, searchingY = true;
                  while (searchingX || searchingY) {
                     if (killedRootX - 1 >= 0 && pointsShot[killedRootX - 1][killedRootY] == 1) {
                        killedRootX--;
                     } else { searchingX = false; }

                     if (killedRootY - 1 >= 0 && pointsShot[killedRootX][killedRootY - 1] == 1) {
                        killedRootY--;
                     } else { searchingY = false; }
                  }

                  pointsShot[killedRootX][killedRootY] = 2;

                  // clear other hits of ship
                  searchingX = true; searchingY = true;
                  for (int i = 1; searchingX || searchingY; i++) {
                     if (killedRootX + i < pointsShot.length && pointsShot[killedRootX + i][killedRootY] >= 1) {
                        pointsShot[killedRootX + i][killedRootY] = -2;
                        killedOrientation = 0;
                     } else { searchingX = false; }

                     if (killedRootY + i < pointsShot.length && pointsShot[killedRootX][killedRootY + i] >= 1) {
                        pointsShot[killedRootX][killedRootY + i] = -2;
                        killedOrientation = 1;
                     } else { searchingY = false; }

                     killedSize = i;
                  }

                  Image zoomedShipImage = SettingsHandler.getImage("image_ship_" + killedSize + "_destroyed").getScaledInstance(zoomedItemSize, zoomedItemSize, Image.SCALE_FAST);

                  AffineTransform backup = g2d.getTransform();
                  AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(90 * killedOrientation), 0, 0);
                  g2d.setTransform(a);
                  g2d.drawImage(zoomedShipImage, xCord * zoomedItemSize, yCord * zoomedItemSize, null);
                  g2d.setTransform(backup);
               }
            }
         }
      } else if (!inMatch) {
         for (int row = 0; row < Main.currentGame.getPitchSize(); row++) {
            for (int column = 0; column < Main.currentGame.getPitchSize(); column++) {
               g2d.drawImage(zoomedWaterImage, row * zoomedItemSize, column * zoomedItemSize, null);
            }
         }

         List<Ship> placedShips = Main.currentGame.getPlayer1().getShipList();

         for (Ship ship : placedShips) {
            Image zoomedShipImage = SettingsHandler.getImage("image_ship_" + ship.getLength() + "_" + (ship.isDestroyed() ? "destroyed" : "healthy")).getScaledInstance(zoomedItemSize, zoomedItemSize, Image.SCALE_FAST);

            AffineTransform backup = g2d.getTransform();
            AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(90 * ship.getOrientation()), 0, 0);
            g2d.setTransform(a);
            g2d.drawImage(zoomedShipImage, ship.getRootPoint().x * zoomedItemSize, ship.getRootPoint().y * zoomedItemSize, null);
            g2d.setTransform(backup);

            if (!ship.isDestroyed() && ship.getDamage().length > 0) {
               for (int damagePos : ship.getDamage()) {
                  if (ship.getOrientation() == 0) {
                     g2d.drawImage(zoomedDestroyedShipImage, (ship.getRootPoint().x + damagePos) * zoomedItemSize, ship.getRootPoint().y * zoomedItemSize, null);
                  } else {
                     g2d.drawImage(zoomedDestroyedShipImage, ship.getRootPoint().x * zoomedItemSize, (ship.getRootPoint().y + damagePos) * zoomedItemSize, null);
                  }
               }
            }
         }
      }
   }
}
