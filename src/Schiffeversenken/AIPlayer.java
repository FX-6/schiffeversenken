package Schiffeversenken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import Notifications.Notification;
import Notifications.NotificationCenter;

public class AIPlayer extends Player implements Notification {

	private int[][] priorities = new int[game.getPitchSize()][game.getPitchSize()]; // Ein 2D Array mit
																					// Schussprioritaeten.

	public AIPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);
		NotificationCenter.addObserver("AIPlayerPlaceShips", this);
		// for (int i = 0; i < priorities.length; i++) {
		// for (int j = 0; j < priorities[i].length; j++) {
		// priorities[i][j] = 100; // Wert 100 als Standard Prioritaet in alle
		// Arrayfelder
		// }
		// }

		// for (int[] priorityRow : priorities) {
		// Arrays.fill(priorityRow, 100);
		// }
		// AIPlayer.placeShipsAutomatically(this);
		// Main.currentGame.setReady(this);
	}

	@Override
	public void pass() {
		System.out.println("pass");
		// Teile der KI mit, dass sie einen weiteren Zug ausüben darf
		otherPlayer.setMyTurn(false);
		setMyTurn(true);

		handleShoot();
	}

	private void handleShoot() {
		for (int i = 0; i < game.getPitchSize(); i++) { // traegt die Werte aus PointsShot ein
			for (int j = 0; j < game.getPitchSize(); j++) {
				if (getPointsShot()[i][j] == 0) { // verfehlt wird als 0 eingetragen
					priorities[i][j] = 0;
				} else if (getPointsShot()[i][j] == 1) { // treffer wird als -1 eingetragen
					priorities[i][j] = -1;
				} else if (getPointsShot()[i][j] == 2) { // versenkt wird als -2 eingetragen
					priorities[i][j] = -2;
				}
			}
		}

		for (int i = 0; i < priorities.length; i++) { // setzt links und rechts oberhalb und
			for (int j = 0; j < priorities[i].length; j++) { // links und rechts unterhalb aller Treffer
				if (priorities[i][j] == -1) { // auf Prioritaet 0
					for (int k = 0; k < 4; k++) {
						int tempi = i - (k < 2 ? 1 : -1);
						int tempj = j - ((k % 2) == 0 ? 1 : -1);
						if (tempi >= 0 && tempi < game.getPitchSize() && tempj >= 0 && tempj < game.getPitchSize()) {
							priorities[tempi][tempj] = 0;
						}

						if (k == 0) {
							tempi = i - 1;
							tempj = j;
						} else if (k == 1) {
							tempi = i;
							tempj = j - 1;
						} else if (k == 2) {
							tempi = i;
							tempj = j + 1;
						} else if (k == 3) {
							tempi = i + 1;
							tempj = j;
						}

						if (tempi >= 0 && tempi < game.getPitchSize() && tempj >= 0 && tempj < game.getPitchSize()
								&& priorities[tempi][tempj] == 100) {
							priorities[tempi][tempj] = 200;
						}
					}
				}
			}
		}

		ArrayList<Point> maxs = new ArrayList<Point>();
		int max = 0;
		maxs.add(new Point(0, 0));
		for (int i = 0; i < game.getPitchSize(); i++) { // sucht alle Felder mit höchstem Prioritätswert
			for (int j = 0; j < game.getPitchSize(); j++) {
				// System.out.println(priorities.length + "a " + i + " " + j);
				if (priorities[i][j] > max) {
					maxs.clear();
					maxs.add(new Point(i, j));
					max = priorities[i][j];
				} else if (priorities[i][j] == max) {
					maxs.add(new Point(i, j));
				}
			}
		}

		for (int i = 0; i < priorities.length; i++) {
			for (int j = 0; j < priorities.length; j++) {
				System.out.printf("%3s, ", Integer.toString(priorities[j][i]));
			}
			System.out.println(" ");
		}

		Point target = maxs.get(ThreadLocalRandom.current().nextInt(0, maxs.size())).add(1, 1);
		System.out.println(target.x + " " + target.y);
		int res = shoot(target); // schießt zufällig auf ein Feld mit höchstem Prioritätswert
		System.out.println(res);
		for (int i = 0; i < game.getPitchSize(); i++) { // traegt die Werte aus PointsShot ein
			for (int j = 0; j < game.getPitchSize(); j++) {
				if (getPointsShot()[i][j] == 0) { // verfehlt wird als 0 eingetragen
					priorities[i][j] = 0;
				} else if (getPointsShot()[i][j] == 1) { // treffer wird als -1 eingetragen
					priorities[i][j] = -1;
				} else if (getPointsShot()[i][j] == 2) { // versenkt wird als -2 eingetragen
					priorities[i][j] = -2;
				}
			}
		}
		if (res == 2) {
			for (int i = 0; i < priorities.length; i++) {
				for (int j = 0; j < priorities.length; j++) {
					if (priorities[j][i] == 200) {
						priorities[j][i] = 0;
					} else if (priorities[j][i] == -2) {
						System.out.println("found a -2");
						for (int k = 0; k < 8; k++) {
							int tempI = i + ((k<3)?(-1):((k>2)&&(k<5))?(0):(k>4)?(1):0);
							int tempJ = j + (((k==0)||(k==3)||(k==5))?(-1):((k==1)||(k==6))?(0):((k==2)||(k==4)||(k==7))?(1):0);
							if (tempI >= 0 && tempJ >= 0 && tempI < priorities.length && tempJ < priorities.length) {
								if (priorities[tempJ][tempI] > 0) {
									priorities[tempJ][tempI] = 0;
									System.out.println(tempJ + " " + tempI);
								}
							}
						}
					}
				}
			}
		}
		if (res != 0) {
			this.handleShoot();
		}
	}

	public static void placeShipsAutomatically(Player player) { // setzt auomatisch Schiffe nach dem Zufallsprinzip
		player.removeAllShips();
		boolean failed = false;
		boolean done = false;

		// int ships = remainingShipsToBePlaced[2] + remainingShipsToBePlaced[3] +
		// remainingShipsToBePlaced[4] + remainingShipsToBePlaced[5];

		for (int i = 0; i < 2000; i++) { // versucht bis zu 2000 mal alle Schiffe zu platzieren
			failed = false;
			int[] remainingShipsToBePlaced = { 0, 0, 0, 0, 0, 0 };
			for (int i2 = 2; i2 <= 5; i2++) {
				remainingShipsToBePlaced[i2] = Main.currentGame.getNumberOfShips(i2);
			}
			System.out.println(remainingShipsToBePlaced[2] + " " + remainingShipsToBePlaced[3] + " "
					+ remainingShipsToBePlaced[4] + " " + remainingShipsToBePlaced[5]);

			if (done) {
				System.out.println("succes! at try: " + i);
				break;
			}
			player.removeAllShips();
			for (int j = 2; j < 6; j++) { // platziert die SChiffe von klein nach groß
				// failed = false;
				if (failed) {
					break;
				}
				while (remainingShipsToBePlaced[j] > 0) { // platziert allee Schiffe einer Größe
					if (failed) {
						break;
					}
					for (int k = 0; k < 500; k++) { // bis zu 500 platzierungsversuche pro Schiff
						if (failed) { // nach 500 Versuchen werden alle Schiffe gelöscht und der nächste Versuch
										// startet
							break;
						}
						if (player.placeShipAt(new Ship(j, (int) Math.floor(Math.random() * 2)),
								new Point((int) (Math.random() * (Main.currentGame.getPitchSize() + 1)),
										(int) (Math.random() * (Main.currentGame.getPitchSize() + 1))))) {
							remainingShipsToBePlaced[j]--;
							// System.out.println("placed ship type: " + j + " Number: " +
							// (remainingShipsToBePlaced[j] + 1) + " at try " + (k+1));
							k = 500;
							if (remainingShipsToBePlaced[2] + remainingShipsToBePlaced[3] + remainingShipsToBePlaced[4]
									+ remainingShipsToBePlaced[5] == 0) {
								done = true;
							}
						} else {
							// System.out.println("missed a place");
						}
						if (k == 499) {
							failed = true;
							System.out.println("failed try " + (i + 1) + " at ship type: " + j + " Number: "
									+ (remainingShipsToBePlaced[j]));
						}
					}
				}
			}
		}

		// if (Main.currentGame.getNumberOfShips(2) +
		// Main.currentGame.getNumberOfShips(3) + Main.currentGame.getNumberOfShips(4) +
		// Main.currentGame.getNumberOfShips(5) > 0) {
		// System.out.println("placeShipsAutomatically failed 10 times");
		// }
		if (failed) {
			player.removeAllShips();
			System.out.println("Failed");
		}
		// Für die Spielfeldgröße
		// Main.currentGame.getPitchSize();

		// Für die Anzahl der Schiffe einer größe
		// Main.currentGame.getNumberOfShips(int size);

		// Array mit anzahl der Schiffe der jeweiligen größe

		// Zum Platzieren dann
		// player.placeShipAt(Ship ship, Point point);
	}

	@Override
	public void processNotification(String type, Object object) { // wenn man gegen den Bot spielt lässt dieser seine
																	// Schiffe automatisch
		if (type.equals("AIPlayerPlaceShips")) { // platzieren sobald Feldgröße und Schiffanzahlen festgelegt sind
			System.out.println("AIPlayerPlaceShips");
			priorities = new int[game.getPitchSize()][game.getPitchSize()];
			for (int[] priorityRow : priorities) {
				Arrays.fill(priorityRow, 100);
			}
			AIPlayer.placeShipsAutomatically(this);
			Main.currentGame.setReady(this);
		}

	}
}