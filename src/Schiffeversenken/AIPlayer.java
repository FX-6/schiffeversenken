package Schiffeversenken;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class AIPlayer extends Player {

	private int[][] priorities = new int[game.getPitchSize()][game.getPitchSize()]; //Ein 2D Array mit Schussprioritaeten.

	public AIPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);

		for (int i = 0; i < priorities.length; i++) {
			for (int j = 0; j < priorities[i].length; j++) {
				priorities[i][j] = 100;											//Wert 100 als Standard Prioritaet in alle Arrayfelder
			}
		}
	}

	@Override
	public void pass() {
		// Teile der KI mit, dass sie einen weiteren Zug ausüben darf
		otherPlayer.setMyTurn(false);
		setMyTurn(true);

		for (int i = 0; i < game.getPitchSize(); i++) {							//traegt die Werte aus PointsShot negativ ein
			for (int j = 0; j < game.getPitchSize(); j++) {
				if (getPointsShot()[i][j] == 0) {
					priorities[i][j] = 0;
				} else if (getPointsShot()[i][j] == 1) {
					priorities[i][j] = -1;
				} else if (getPointsShot()[i][j] == 2) {
					priorities[i][j] = -2;
				}
			}
		}

		for (int i = 0; i < priorities.length; i++) {							//setzt links und rechts oberhalb und
			for (int j = 0; j < priorities[i].length; j++) {					//links und rechts unterhalb aller Treffer
				if (priorities[i][j] == -1) {									//auf Prioritaet 0
					for (int k = 0; k < 4; k++) {
						int tempi = i-(k<2?1:0);
						int tempj = j-(k%2==0?1:0);
						if (tempi > 0 && tempi < game.getPitchSize() && tempj > 0 && tempj < game.getPitchSize()){
							priorities[tempi][tempj] = 0;
						}
					}
					if (i-- > 0) {												//setzt, wenn das Schiff mehr als 1 Feld getroffen ist,
						if (priorities[i--][j] == -1) {							//für vertikale Schiffe die Felder links und rechts
							if (j-- > 0) {										//auf Prio 0
								priorities[i][j--] = 0;							//und für horizontale Schiffe die Felder links und rechts
							}													//auf Prio 0
							if (j++ < game.getPitchSize()) {
								priorities[i][j++] = 0;
							}
						}
					}
					if (i++ > 0) {
						if (priorities[i++][j] == -1) {
							if (j-- > 0) {
								priorities[i][j--] = 0;
							}
							if (j++ < game.getPitchSize()) {
								priorities[i][j++] = 0;
							}
						}
					}
					if (j-- > 0) {
						if (priorities[i][j--] == -1) {
							if (i-- > 0) {
								priorities[i--][j] = 0;
							}
							if (i++ < game.getPitchSize()) {
								priorities[i++][j] = 0;
							}
						}
					}
					if (j++ > 0) {
						if (priorities[i][j++] == -1) {
							if (i-- > 0) {
								priorities[i--][j] = 0;
							}
							if (i++ < game.getPitchSize()) {
								priorities[i++][j] = 0;
							}
						}
					}
				}
			}
		}

		ArrayList<Point> maxs = new ArrayList<Point>();
		int max = 0;
		maxs.add(new Point(0, 0));
		for (int i = 0; i < game.getPitchSize(); i++) {							//traegt die Werte aus PointsShot negativ ein
			for (int j = 0; j < game.getPitchSize(); j++) {
				if (priorities[i][j] > max) {
					maxs.clear();
					maxs.add(new Point(i, j));
					max = priorities[i][j];
				} else if (priorities[i][j] == max) {
					maxs.add(new Point(i, j));
				}
			}
		}
		shoot(maxs.get(ThreadLocalRandom.current().nextInt(0,maxs.size())));
	}

   public static void placeShipsAutomatically(Player player) {
      // Für die Spielfeldgröße
      // Main.currentGame.getPitchSize();

      // Für die Anzahl der Schiffe einer größe
      // Main.currentGame.getNumberOfShips(int size);

      // Array mit anzahl der Schiffe der jeweiligen größe
      // int[] remainingShipsToBePlaced = {0, 0, 0, 0};       // index = schiffsgröße - 2
      // int[] remainingShipsToBePlaced = {0, 0, 0, 0, 0, 0}; // index = schiffsgröße
      // for (int i = 2; i <= 5; i++) {
      //    remainingShipsToBePlaced[i - 2] = Main.currentGame.getNumberOfShips(i); // wenn index = schiffsgröße - 2
      //    remainingShipsToBePlaced[i] = Main.currentGame.getNumberOfShips(i);     // wenn index = schiffsgröße
      // }

      // Zum Platzieren dann
      // player.placeShipAt(Ship ship, Point point);
   }
}

/**
 * Schiffe dürfen sich nicht berühren -> Felder neben Schiffen prio 0
 * Schiffe sind mindestens 2 Felder lang und in einer Linie -> sobald 2 benachbarte Teile bekannt sind ist entweder:
 * 			  an einem Ende answer 0 -> an anderem Ende prio max
 * 				  für answer 1: offenes Ende prio max
 * 				  für answer 2: Schiff zerstört
 * 		oder: an einem Ende answer 1 -> auf dieses Ende prio max
 * 				  für answer 0: auf anderes Ende prio max
 * 					für answer 1: offenes Ende prio max
 * 					für answer 2: Schiff zerstört
 * 				  für answer 2: Schiff zerstört
 * Schiffanzahl pro Länge bekannt -> Lücken die kleiner als größtes noch vorhandenes Schiff sind -> Felder prio 0
 *
 * Bei answer 0: direkte Nachbarfelder: prio norm -10
 * 				 diagonale Nachbarfelder: prio norm -5
 *
 * Schuss immer auf höchste prio
 */
