package Schiffeversenken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class AIPlayer extends Player {

	public AIPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);

		int[][] priorities = new int[game.getPitchSize()][game.getPitchSize()]; //Ein 2D Array mit Schussprioritaeten.
		for (int i = 0; i < priorities.length; i++) {							
			for (int j = 0; j < priorities[i].length; j++) {					
				priorities[i][j] = 100;											//Wert 100 als Standard Prioritaet
			}
		}

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
		
	}

	@Override
	public void pass() {
		// Teile der KI mit, dass sie einen weiteren Zug ausüben darf
		otherPlayer.setMyTurn(false);
		setMyTurn(true);
	}

}



/* Schiffe dürfen sich nicht berühren -> Felder neben Schiffen prio 0
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
 * 
 */