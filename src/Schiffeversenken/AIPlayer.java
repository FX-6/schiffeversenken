package Schiffeversenken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import Notifications.Notification;
import Notifications.NotificationCenter;

/**
 * Stellt eine Instanz eines computergesteuerten Spielers dar. Hier sind alle Methoden untergebracht,
 * die benötigt werden um den AIPlayer vollautomatisch spielen zu lassen.
 */

public class AIPlayer extends Player implements Notification {

	/**
	 * Speichert alle Felder des Spielfelds mit einer ihnen zugewiesenen Priorität,
	 * die entscheidet auf welches Feld geschossen wird.
	 */
	private int[][] priorities = new int[game.getPitchSize()][game.getPitchSize()]; // Ein 2D Array mit Schussprioritaeten.
	
	/**
	 *Speichert, ob das Spiel zu Ende ist.
	 */
	private boolean gameOver = false;

	/**
	 * Erstellt eine neue Instanz eines computergesteuerten Spielers.
	 * 
	 * @param game Spiel, zu welchem dieser Spieler gehören soll.
	 * @param otherPlayer Gegner dieses Spielers.
	 */
	public AIPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);
		NotificationCenter.addObserver("AIPlayerPlaceShips", this); //Notifications werden eingerichtet
		NotificationCenter.addObserver("WinPlayer1", this); //Notifications werden eingerichtet
		NotificationCenter.addObserver("WinPlayer2", this); //Notifications werden eingerichtet
	}

	/**
	 * Gibt diesem Spieler den Zug.
	 */
	@Override
	public void pass() { // Teile der KI mit, dass sie einen weiteren Zug ausüben darf
		otherPlayer.setMyTurn(false);
		setMyTurn(true);
		if (!gameOver) {
			handleShoot();
		}
	}

	//Getter und Setter zum Speichern / Laden
	public int[][] getPriorities() {return this.priorities;}							// Getter zum Speichern eines Spiels
	public void setPriorities(int[][] priorities) {this.priorities = priorities;}		// Setter zum Laden eines Spiels
	
	/**
	 * Berechnet den Prioritätswert jedes Felds des Spielfelds.
	 * Schießt auf ein / das Feld mit dem höchsten Prioritätswert.
	 */
	private void handleShoot() {
		NotificationCenter.sendNotification("UI-Repaint", null);

		for (int i = 0; i < game.getPitchSize(); i++) // traegt die Werte aus PointsShot ein
			for (int j = 0; j < game.getPitchSize(); j++)
				if (getPointsShot()[i][j] == 0)  	  priorities[i][j] = 0; 	// verfehlt wird als 0 eingetragen
				else if (getPointsShot()[i][j] == 1)  priorities[i][j] = -1;	// treffer wird als -1 eingetragen
				else if (getPointsShot()[i][j] == 2)  priorities[i][j] = -2;	// versenkt wird als -2 eingetragen

		for (int i = 0; i < priorities.length; i++) 							// setzt links und rechts oberhalb und
			for (int j = 0; j < priorities[i].length; j++) 						// links und rechts unterhalb aller Treffer
				if (priorities[i][j] == -1) 									// auf Prioritaet 0
					for (int k = 0; k < 4; k++) {
						int tempi = i - (k < 2 ? 1 : -1);
						int tempj = j - ((k % 2) == 0 ? 1 : -1);
						if (tempi >= 0 && tempi < game.getPitchSize() && tempj >= 0 && tempj < game.getPitchSize()) priorities[tempi][tempj] = 0;
						if      (k == 0) { tempi = i - 1; tempj = j; } //setzt links, rechts oberhalb und unterhalb des treffers auf Prio 200
						else if (k == 1) { tempi = i; tempj = j - 1; }
						else if (k == 2) { tempi = i; tempj = j + 1; }
						else if (k == 3) { tempi = i + 1; tempj = j; }
						if (tempi >= 0 && tempi < game.getPitchSize() && tempj >= 0 && tempj < game.getPitchSize() && priorities[tempi][tempj] == 100) priorities[tempi][tempj] = 200;
					}

		ArrayList<Point> maxs = new ArrayList<Point>();			//ein Array mit allen maximalen Prio werten
		int max = 0;											//Wert des maximalen Prio werts
		maxs.add(new Point(0, 0));
		for (int i = 0; i < game.getPitchSize(); i++) 			//schreibt alle Felder mit maximalem Prio wert
			for (int j = 0; j < game.getPitchSize(); j++)		//in das Array
				if (priorities[i][j] > max) {
					maxs.clear();
					maxs.add(new Point(i, j));
					max = priorities[i][j];
				} else if (priorities[i][j] == max) maxs.add(new Point(i, j));

		Point target = maxs.get(ThreadLocalRandom.current().nextInt(0, maxs.size())).add(1, 1);
		int res = shoot(target); 							// schießt zufällig auf ein Feld mit höchstem Prioritätswert

		for (int i = 0; i < game.getPitchSize(); i++)  								// traegt die Werte aus PointsShot ein
			for (int j = 0; j < game.getPitchSize(); j++)
				if      (getPointsShot()[i][j] == 0) priorities[i][j] = 0; 			// verfehlt wird als 0 eingetragen
				else if (getPointsShot()[i][j] == 1) priorities[i][j] = -1; 		// treffer wird als -1 eingetragen
				else if (getPointsShot()[i][j] == 2) priorities[i][j] = -2;			// versenkt wird als -2 eingetragen

		if (res == 2)																//nachdem ein Schiff zerstört wurde
			for (int i = 0; i < priorities.length; i++)
				for (int j = 0; j < priorities.length; j++)
					if      (priorities[j][i] == 200) priorities[j][i] = 0;			//werden alle Prio 200 mit 0 ersetzt
					else if (priorities[j][i] == -2)								//und alle Felder um das letzte zerstoerte Schiffsteil
						for (int k = 0; k < 8; k++) {								//werden zu Prio 0 gesetzt
							int tempI = i + ((k<3)?(-1):((k>2)&&(k<5))?(0):(k>4)?(1):0);
							int tempJ = j + (((k==0)||(k==3)||(k==5))?(-1):((k==1)||(k==6))?(0):((k==2)||(k==4)||(k==7))?(1):0);
							if (tempI >= 0 && tempJ >= 0 && tempI < priorities.length && tempJ < priorities.length)
								if (priorities[tempJ][tempI] > 0) priorities[tempJ][tempI] = 0;
						}

		if (res != 0) this.handleShoot(); //wenn eine Schiff getroffen oder zerstört wurde nochmal schießen
	}

	/**
	 * Platziert automatisch zufällig die Schiffe auf dem Feld des übergebenen Spielers.
	 * 
	 * @param player Der Spieler auf dessen Feld alle Schiffe platziert werden sollen.
	 */
	public static void placeShipsAutomatically(Player player) { // setzt auomatisch Schiffe nach dem Zufallsprinzip
		boolean failed = false, done = false;
		for (int i = 0; i < 2000 && !done; i++) { 					// versucht bis zu 2000 mal alle Schiffe zu platzieren
			failed = false;
			int[] remainingShipsToBePlaced = { 0, 0, 0, 0, 0, 0 };
			for (int i2 = 2; i2 <= 5; i2++) remainingShipsToBePlaced[i2] = Main.currentGame.getNumberOfShips(i2);
			player.removeAllShips();
			for (int j = 2; j < 6 && !failed; j++) 						// platziert die SChiffe von klein nach groß
				while (remainingShipsToBePlaced[j] > 0 && !failed)		// platziert allee Schiffe einer Größe
					for (int k = 0; k < 500 && !failed; k++) 			// bis zu 500 platzierungsversuche pro Schiff
						if (player.placeShipAt(new Ship(j, (int) Math.floor(Math.random() * 2)), new Point((int) (Math.random() * (Main.currentGame.getPitchSize() + 1)),(int) (Math.random() * (Main.currentGame.getPitchSize() + 1))))) {
							remainingShipsToBePlaced[j]--;				// nach 500 Versuchen werden alle Schiffe gelöscht und der nächste Versuch startet
							k = 500;
							if (remainingShipsToBePlaced[2] + remainingShipsToBePlaced[3] + remainingShipsToBePlaced[4] + remainingShipsToBePlaced[5] == 0) done = true;
						} else if (k == 499) failed = true;
		}
		if (failed) { player.removeAllShips(); System.out.println("placeShipsAutomatically Failed"); }
	}

	/** 
	 * Verarbeitet eine Nachricht, die über das Netzwerk empfangen wurde.
	 * 
	 * @param object Nachricht, welche empfangen wurde.
	 */
	@Override
	public void processNotification(String type, Object object) { 		// wenn man gegen den Bot spielt lässt dieser seine Schiffe automatisch
		if (type.equals("AIPlayerPlaceShips")) { 				// platzieren sobald Feldgröße und Schiffanzahlen festgelegt sind
			priorities = new int[game.getPitchSize()][game.getPitchSize()];
			for (int[] priorityRow : priorities) Arrays.fill(priorityRow, 100);
			AIPlayer.placeShipsAutomatically(this);
			Main.currentGame.setReady(this);
			NotificationCenter.sendNotification("UI-AIPlayerReady", null);
		} else if (!gameOver && (type.equals("WinPlayer1") || type.equals("WinPlayer2"))) gameOver = true;
	}
}