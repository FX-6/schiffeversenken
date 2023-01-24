package UserInterface.UIComponents;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import Schiffeversenken.Main;
import Schiffeversenken.SettingsHandler;
import Schiffeversenken.Ship;

/**
 * Rendert die Map im Hintergrund des Spielsfensters.
 * Bietet viele methoden um sie zu updaten.
 */
public class GameMapPanel extends UIPanel {
	private static final long serialVersionUID = 1L;

	private Point prevMouseLocation, windowPosition;
	private double zoomFactor = 1;
	private boolean gameOver = false;
	private boolean inMatch = false;
	private boolean viewingSelf = true;
	private int currentFocusedX = Main.currentGame.getPitchSize() / 2;
	private int currentFocusedY = Main.currentGame.getPitchSize() / 2;
	private int currentlyPlacedShipSize = 0;
	private int currentlyPlacedShipOrientation = 0;
	private Schiffeversenken.Point currentShootFocus = null;

	private Image cloudImage = SettingsHandler.getImage("image_clouds");
	private Image zoomedCloudImage = cloudImage.getScaledInstance(imageSize, imageSize, Image.SCALE_FAST);
	private Image waterImage = SettingsHandler.getImage("image_water");
	private Image zoomedWaterImage = waterImage.getScaledInstance(imageSize, imageSize, Image.SCALE_FAST);
	private Image destroyedShipImage = SettingsHandler.getImage("image_ship_destroyed");
	private Image zoomedDestroyedShipImage = destroyedShipImage.getScaledInstance(imageSize, imageSize,
			Image.SCALE_FAST);
	private Image destroyedWaterImage = SettingsHandler.getImage("image_water_destroyed");
	private Image zoomedDestroyedWaterImage = destroyedWaterImage.getScaledInstance(imageSize, imageSize,
			Image.SCALE_FAST);

	/**
	 * Erstellt eine neue Map.
	 */
	public GameMapPanel() {
		super();

		this.setLayout(null);
		this.setSize(Main.currentGame.getPitchSize() * imageSize, Main.currentGame.getPitchSize() * imageSize);

		this.addMouseListener(new MouseAdapter() {
			// speichert die Maus location für's veerschieben der Map
			public void mousePressed(MouseEvent e) {
				prevMouseLocation = e.getLocationOnScreen();
				windowPosition = getLocation();
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			// ändert die Position beim drag'n'drop
			public void mouseDragged(MouseEvent e) {
				Point currentMouseLocation = e.getLocationOnScreen();
				windowPosition.x += currentMouseLocation.x - prevMouseLocation.x;
				windowPosition.y += currentMouseLocation.y - prevMouseLocation.y;
				prevMouseLocation = currentMouseLocation;
				setLocation(windowPosition.x, windowPosition.y);
			}

			// ändert das Feld über dem die Maus gerade hovert
			public void mouseMoved(MouseEvent e) {
				int zoomedItemSize = (int) (imageSize * zoomFactor);

				currentFocusedX = e.getX() / zoomedItemSize;
				currentFocusedY = e.getY() / zoomedItemSize;

				getParent().repaint();
			}
		});

		this.addMouseWheelListener(new MouseWheelListener() {
			// änderet den Zoom beim scrollen
			public void mouseWheelMoved(MouseWheelEvent e) {
				int oldZommedItemSize = (int) (imageSize * zoomFactor);

				if (e.getWheelRotation() < 0) {
					// zoom in
					if (zoomFactor < 2) {
						zoomFactor += 0.1;
					}
				} else if (e.getWheelRotation() > 0) {
					// zoom out
					if (zoomFactor > 0.5) {
						zoomFactor -= 0.1;
					}
				}

				int newZoomedItemSize = (int) (imageSize * zoomFactor);
				zoomedCloudImage = cloudImage.getScaledInstance(newZoomedItemSize, newZoomedItemSize, Image.SCALE_FAST);
				zoomedWaterImage = waterImage.getScaledInstance(newZoomedItemSize, newZoomedItemSize, Image.SCALE_FAST);
				zoomedDestroyedShipImage = destroyedShipImage.getScaledInstance(newZoomedItemSize, newZoomedItemSize,
						Image.SCALE_FAST);
				zoomedDestroyedWaterImage = destroyedWaterImage.getScaledInstance(newZoomedItemSize, newZoomedItemSize,
						Image.SCALE_FAST);

				int widthDiff = (oldZommedItemSize - newZoomedItemSize) * Main.currentGame.getPitchSize() / 2;
				setLocation(getX() + widthDiff, getY() + widthDiff);

				getParent().repaint();
			}
		});
	}

	/**
	 * Sagt der Map das fertig platziert wurde.
	 * Repaints parent.
	 */
	public void finishedPlacing() {
		inMatch = true;

		this.getParent().repaint();
	}

	/**
	 * Sagt der Map das der beobachtete Spieler geändert wurde.
	 * Rendert dann ein anderes Spielfeld.
	 * Repaints parent.
	 */
	public void changeDisplayedPlayer() {
		viewingSelf = !viewingSelf;

		this.getParent().repaint();
	}

	/**
	 * Verschiebt die Map um eine gewissen anzahl an Feldern.
	 * Repaints parent.
	 *
	 * @param horizontal Wenn <code>true</code> dann horizontal
	 * @param amount     Anzahl um die die Map verschoben werden soll
	 */
	public void changePositionByTiles(boolean horizontal, int amount) {
		int zoomedItemSize = (int) (imageSize * zoomFactor);

		if (horizontal) {
			setLocation(getLocation().x + (zoomedItemSize * -amount), getLocation().y);
		} else {
			setLocation(getLocation().x, getLocation().y + (zoomedItemSize * -amount));
		}

		this.getParent().repaint();
	}

	/**
	 * Ändert das Feld über dem gerade gehovert wird.
	 * Repaints parent.
	 *
	 * @param horizontal Wenn <code>true</code> dann horizontal
	 * @param amount     Anzahl um die das Feld geändert wird
	 */
	public void changeCurrentlyFocusedTile(boolean horizontal, int amount) {
		if (horizontal) {
			if (currentFocusedX + amount >= 0 && currentFocusedX + amount < Main.currentGame.getPitchSize()) {
				currentFocusedX += amount;
			}
		} else {
			if (currentFocusedY + amount >= 0 && currentFocusedY + amount < Main.currentGame.getPitchSize()) {
				currentFocusedY += amount;
			}
		}

		this.getParent().repaint();
	}

	/**
	 * Setzt die größe des Schiffs das platziert werden soll.
	 * <= 2 um Schiffe zu löschen.
	 * Repaints parent.
	 *
	 * @param size Die neue größe
	 */
	public void setCurrentlyPlacedShipSize(int size) {
		currentlyPlacedShipSize = size;

		this.getParent().repaint();
	}

	/**
	 * Toggelt die Orientierung des Schiffs das platziert werden soll.
	 * Repaints parent.
	 */
	public void changeCurrentlyPlacedShipOrientation() {
		currentlyPlacedShipOrientation = (currentlyPlacedShipOrientation == 1 ? 0 : 1);

		this.getParent().repaint();
	}

	/**
	 * Platziert ein Schiff wenn man im Spiel ist.
	 * Repaints parent (wenn es funktioniert hat).
	 *
	 * @return <code>true</code> wenn es funktioniert hat
	 */
	public boolean placeShip() {
		if (!inMatch) {
			boolean returnVal = false;

			if (currentlyPlacedShipSize >= 2) {
				returnVal = Main.currentGame.getPlayer1().placeShipAt(
						new Ship(currentlyPlacedShipSize, currentlyPlacedShipOrientation),
						new Schiffeversenken.Point(currentFocusedX + 1, currentFocusedY + 1));
			} else {
				returnVal = removeShip();
			}
			this.getParent().repaint();
			return returnVal;
		} else {
			return false;
		}
	}

	/**
	 * Löscht ein Schiff wenn man im Spiel ist.
	 *
	 * @return <code>true</code> wenn es funktioniert hat
	 */
	private boolean removeShip() {
		if (!inMatch) {
			boolean returnVal = Main.currentGame.getPlayer1()
					.removeShip(Main.currentGame.getPlayer1()
							.getShipAt(new Schiffeversenken.Point(currentFocusedX + 1, currentFocusedY + 1)));
			return returnVal;
		} else {
			return false;
		}
	}

	/**
	 * Setzt den Focus auf das gerade gehovert Feld.
	 * Entfernt den Focus falls man gerade auf ihm hovert.
	 * Repaints parent.
	 *
	 * @return <code>true</code> wenn der Focus geändert wurde, false wenn der Focus
	 *         entfernt wurde
	 */
	public boolean setShootFocus() {
		if (gameOver) {
			return false;
		}

		boolean returnVal = false;

		if (currentShootFocus != null && currentShootFocus.x == currentFocusedX
				&& currentShootFocus.y == currentFocusedY) {
			returnVal = false;
		} else {
			currentShootFocus = new Schiffeversenken.Point(currentFocusedX, currentFocusedY);
			returnVal = true;
		}

		this.getParent().repaint();

		return returnVal;
	}

	/**
	 * Liefert den aktuellen Focus.
	 *
	 * @return Der Focus als {@link Schiffeversenken.Point}
	 */
	public Schiffeversenken.Point getShootFocus() {
		return currentShootFocus;
	}

	/**
	 * Löscht den aktuellen Focus.
	 * Repaints parent.
	 */
	public void clearShootFocus() {
		if (gameOver) {
			return;
		}

		currentShootFocus = null;
		this.getParent().repaint();
	}

	/**
	 * Löscht den Focus, falls man auf ihm hovert.
	 */
	public void clearShootFocusIfSame() {
		if (gameOver) {
			return;
		}

		if (currentShootFocus != null && currentShootFocus.x == currentFocusedX
				&& currentShootFocus.y == currentFocusedY) {
			currentShootFocus = null;
			this.getParent().repaint();
		}
	}

	/**
	 * Sagt der Map dass das Spiel vorbei ist.
	 */
	public void setGameOver() {
		gameOver = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		int zoomedItemSize = (int) (imageSize * zoomFactor);

		this.setSize(zoomedItemSize * Main.currentGame.getPitchSize(),
				zoomedItemSize * Main.currentGame.getPitchSize());

		if (inMatch && viewingSelf) {
			int[][] enemyPointsShot = Main.currentGame.getPlayer2().getPointsShot();

			for (int xCord = 0; xCord < enemyPointsShot.length; xCord++) {
				for (int yCord = 0; yCord < enemyPointsShot.length; yCord++) {
					if (enemyPointsShot[xCord][yCord] == 0) {
						g2d.drawImage(zoomedDestroyedWaterImage, xCord * zoomedItemSize, yCord * zoomedItemSize, null);
					} else {
						g2d.drawImage(zoomedWaterImage, xCord * zoomedItemSize, yCord * zoomedItemSize, null);
					}
				}
			}

			List<Ship> placedShips = Main.currentGame.getPlayer1().getShipList();

			for (Ship ship : placedShips) {
				int[] damageOfShip = ship.getDamage();

				if (ship.getOrientation() == 0) {
					Image zoomedShipImage = SettingsHandler
							.getImage("image_ship_" + ship.getLength() + "_"
									+ (ship.isDestroyed() ? "destroyed" : "healthy"))
							.getScaledInstance(zoomedItemSize * ship.getLength(), zoomedItemSize, Image.SCALE_FAST);

					g2d.drawImage(zoomedShipImage, (ship.getRootPoint().x - 1) * zoomedItemSize,
							(ship.getRootPoint().y - 1) * zoomedItemSize, null);

					for (int damgePos = 0; damgePos < damageOfShip.length && !ship.isDestroyed(); damgePos++) {
						if (damageOfShip[damgePos] == 1) {
							ship.toString();
							g2d.drawImage(zoomedDestroyedShipImage,
									(ship.getRootPoint().x - 1 + damgePos) * zoomedItemSize,
									(ship.getRootPoint().y - 1) * zoomedItemSize, null);
						}
					}
				} else {
					Image zoomedShipImage = SettingsHandler
							.getRotatedImage("image_ship_" + ship.getLength() + "_"
									+ (ship.isDestroyed() ? "destroyed" : "healthy"))
							.getScaledInstance(zoomedItemSize, zoomedItemSize * ship.getLength(), Image.SCALE_FAST);

					g2d.drawImage(zoomedShipImage, (ship.getRootPoint().x - 1) * zoomedItemSize,
							(ship.getRootPoint().y - 1) * zoomedItemSize, null);

					for (int damgePos = 0; damgePos < damageOfShip.length && !ship.isDestroyed(); damgePos++) {
						if (damageOfShip[damgePos] == 1) {
							g2d.drawImage(zoomedDestroyedShipImage,
									(ship.getRootPoint().x - 1) * zoomedItemSize,
									(ship.getRootPoint().y - 1 + damgePos) * zoomedItemSize, null);

						}
					}
				}
			}

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(borderWidth));
			g2d.setColor(borderColor);
			g2d.drawRoundRect(currentFocusedX * zoomedItemSize, currentFocusedY * zoomedItemSize, zoomedItemSize,
					zoomedItemSize, borderRadius, borderRadius);
		} else if (inMatch && !viewingSelf) {
			// 1 = not shot yet; 0 = water; 1 = hit; 2 = destroyed ship; 3 = part of
			// destroyed ship
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

						pointsShot[xCord][yCord] = 3;

						// get root of destroyed ship
						int killedRootX = xCord, killedRootY = yCord;
						boolean searchingX = true, searchingY = true;
						while (searchingX || searchingY) {
							if (killedRootX - 1 >= 0 && pointsShot[killedRootX - 1][killedRootY] == 1) {
								killedRootX--;
							} else {
								searchingX = false;
							}

							if (killedRootY - 1 >= 0 && pointsShot[killedRootX][killedRootY - 1] == 1) {
								killedRootY--;
							} else {
								searchingY = false;
							}
						}

						pointsShot[killedRootX][killedRootY] = 2;

						// clear other hits of ship
						searchingX = true;
						searchingY = true;
						for (int i = 1; searchingX || searchingY; i++) {
							if (searchingX && killedRootX + i < pointsShot.length
									&& pointsShot[killedRootX + i][killedRootY] >= 1) {
								pointsShot[killedRootX + i][killedRootY] = 3;
								killedOrientation = 0;
							} else {
								searchingX = false;
							}

							if (searchingY && killedRootY + i < pointsShot.length
									&& pointsShot[killedRootX][killedRootY + i] >= 1) {
								pointsShot[killedRootX][killedRootY + i] = 3;
								killedOrientation = 1;
							} else {
								searchingY = false;
							}

							killedSize = i;
						}

						if (killedOrientation == 0) {
							Image zoomedShipImage = SettingsHandler.getImage("image_ship_" + killedSize + "_destroyed")
									.getScaledInstance(zoomedItemSize * killedSize, zoomedItemSize, Image.SCALE_FAST);

							g2d.drawImage(zoomedShipImage, xCord * zoomedItemSize, yCord * zoomedItemSize, null);
						} else {
							Image zoomedShipImage = SettingsHandler
									.getRotatedImage("image_ship_" + killedSize + "_destroyed")
									.getScaledInstance(zoomedItemSize, zoomedItemSize * killedSize, Image.SCALE_FAST);

							g2d.drawImage(zoomedShipImage, xCord * zoomedItemSize, yCord * zoomedItemSize, null);
						}
					}
				}
			}

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(borderWidth));
			g2d.setColor(borderColor);
			g2d.drawRoundRect(currentFocusedX * zoomedItemSize, currentFocusedY * zoomedItemSize, zoomedItemSize,
					zoomedItemSize, borderRadius, borderRadius);

			if (currentShootFocus != null) {
				g2d.setColor(errorColor);
				g2d.drawRoundRect(currentShootFocus.x * zoomedItemSize + borderWidth,
						currentShootFocus.y * zoomedItemSize + borderWidth, zoomedItemSize - 2 * borderWidth,
						zoomedItemSize - 2 * borderWidth, borderRadius, borderRadius);
			}
		} else if (!inMatch) {
			for (int row = 0; row < Main.currentGame.getPitchSize(); row++) {
				for (int column = 0; column < Main.currentGame.getPitchSize(); column++) {
					g2d.drawImage(zoomedWaterImage, row * zoomedItemSize, column * zoomedItemSize, null);
				}
			}

			List<Ship> placedShips = Main.currentGame.getPlayer1().getShipList();

			for (Ship ship : placedShips) {
				if (ship.getOrientation() == 0) {
					Image zoomedShipImage = SettingsHandler.getImage("image_ship_" + ship.getLength() + "_healthy")
							.getScaledInstance(zoomedItemSize * ship.getLength(), zoomedItemSize, Image.SCALE_FAST);

					g2d.drawImage(zoomedShipImage, (ship.getRootPoint().x - 1) * zoomedItemSize,
							(ship.getRootPoint().y - 1) * zoomedItemSize, null);
				} else {
					Image zoomedShipImage = SettingsHandler
							.getRotatedImage("image_ship_" + ship.getLength() + "_healthy")
							.getScaledInstance(zoomedItemSize, zoomedItemSize * ship.getLength(), Image.SCALE_FAST);

					g2d.drawImage(zoomedShipImage, (ship.getRootPoint().x - 1) * zoomedItemSize,
							(ship.getRootPoint().y - 1) * zoomedItemSize, null);
				}
			}

			if (currentlyPlacedShipSize <= 1) {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setStroke(new BasicStroke(borderWidth));
				g2d.setColor(borderColor);
				g2d.drawRoundRect(currentFocusedX * zoomedItemSize, currentFocusedY * zoomedItemSize, zoomedItemSize,
						zoomedItemSize, borderRadius, borderRadius);
			} else if (currentlyPlacedShipOrientation == 0) {
				Image zoomedShipImage = SettingsHandler.getImage("image_ship_" + currentlyPlacedShipSize + "_healthy")
						.getScaledInstance(zoomedItemSize * currentlyPlacedShipSize, zoomedItemSize, Image.SCALE_FAST);

				g2d.drawImage(zoomedShipImage, currentFocusedX * zoomedItemSize, currentFocusedY * zoomedItemSize,
						null);
			} else {
				Image zoomedShipImage = SettingsHandler
						.getRotatedImage("image_ship_" + currentlyPlacedShipSize + "_healthy")
						.getScaledInstance(zoomedItemSize, zoomedItemSize * currentlyPlacedShipSize, Image.SCALE_FAST);

				g2d.drawImage(zoomedShipImage, currentFocusedX * zoomedItemSize, currentFocusedY * zoomedItemSize,
						null);
			}
		}
	}
}
