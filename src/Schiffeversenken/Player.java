package Schiffeversenken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Notifications.NotificationCenter;


/**
 * Stell im Zusammenhang mit einer erbenden Klasse eine Instaz eines Spielers dar. Hier sind alle Methoden untergebracht, die von dem UserInterface aufgerufen werden müssen, 
 * um diesen Spieler steuern zu können.
 */
public abstract class Player {

	/**
	 * Speichert das Spiel, zu welchem dieser Spieler gehört
	 */
	protected Game game;
	
	/**
	 * Speichert den anderen Spieler, gegen welchen dieser Spieler das Spiel spielt
	 */
	protected Player otherPlayer;
	
	/**
	 * Speichert alle Schiffe, die dieser Spieler auf seinem Feld plaziert hat
	 */
	private List<Ship> ships;
	
	/**
	 * Speichert alle Punkte, die dieser Spieler bei seinem Gegner bereits beschossen hat. Indizes ergeben sich aus den Koordinaten eines Punktes. Index = [<x> - 1][<y> - 1] (da Koordinaten im Interval [1; <pitchSize>] liegen)
	 * -1, falls Feld noch nicht beschossen. 0, falls beschossen und Wasser. 1, falls beschossen und Schiff. 2, falls beschossen und Schiff wurde versenkt.
	 */
	private int[][] pointsShot;
	
	/**
	 * Speichert Anzahl der gegnerischen Schiffe, die dieser Spieler beim Gegner bereits zerstört hat.
	 */
	private int shipsDestroyed = 0;
	
	
	/**
	 * Speichert, ob dieser Spieler gerade am Zug ist und schießen darf, oder nicht
	 */
	private boolean isMyTurn = false;
		
	/**
	 * Erstellt im Zusammenhang mit einer von dieser Klasse erbenden Klasse eine neue Instanz eines Spielers.
	 * 
	 * @param game Spiel, zu welchem dieser Spieler gehört.
	 * @param otherPlayer Gegner dieses Spielers im Spiel (muss ggf. mit "null" gefüllt werden und später gesetzt werden, falls noch keine Instanz des Gegners existiert).
	 */
	public Player(Game game, Player otherPlayer) {
		this.game = game;
		this.otherPlayer = otherPlayer;
		this.pointsShot = new int[game.getPitchSize()][game.getPitchSize()];
		this.ships = new ArrayList<Ship>();
		shipsDestroyed = 0;
		
		for (int[] array : pointsShot) {
			Arrays.fill(array, -1);
		}
	}
	
	
	// Getter und Setter
	
	/**
	 * Erfragt, ob dieser Spieler am Zug ist.
	 * 
	 * @return True, falls dieser Spieler am Zug ist. Andernfalls false.
	 */
	public boolean isMyTurn() {return isMyTurn;}
	
	public void setOtherPlayer(Player player) {this.otherPlayer = player;}
	
	/**
	 * Verändert den Status dieses Spielers im Bezug auf Am-Zug-Sein.
	 * 
	 * @param bool Status, der gesetzt werden soll.
	 */
	public void setMyTurn(boolean bool) {this.isMyTurn = bool;}
	
	
	
	// Getter und Setter nur für Save/Load
	public int getShipsDestroyed() {return this.shipsDestroyed;}
	public int[][] getPointsShot() {return this.pointsShot;}
	public List<Ship> getShipList() {return ships;}
	
	public void setShipsDestroyed(int amount) {this.shipsDestroyed = amount;}
	public void setPointsShot(int[][] shots) {this.pointsShot = shots;}
	public void setShips(List<Ship> ships) {this.ships = ships;}
	
	
	
	
	/**
	 * Initialisiert das Array "this.pointsShot" neu. Dies muss im Falle der Veränderung der Spielfeldgröße "this.game.pitchSize" unbedingt gemacht werden!
	 */
	public void refreshPointsShot() {
		this.pointsShot = new int[game.getPitchSize()][game.getPitchSize()];
		
		for (int[] array : pointsShot) {
			Arrays.fill(array, -1);
		}
	}
	
	
	/**
	 * Gibt diesem Spieler den Zug.
	 */
	public abstract void pass();
		
	
	
	/** 
	 * Zugang für das UserInterface, um im Namen von diesem Spieler auf ein gegnerisches Feld zu schießen.
	 * 
	 * @param point Punkt, auf welchen geschossen werden soll.
	 * @return 0, falls kein gegenerisches Schiff getroffen wurde. 1, falls ein gegnerisches Schiff getroffen wurde. 2, falls zusätzlich ein gegnerisches
	 * 			Schiff vollständig versenkt wurde.
	 */
	public int shoot(Point point) {		
		int result = otherPlayer.shot(point);				// Auf gegnerisches Schiff schießen
		pointsShot[point.x - 1][point.y - 1] = result;		// Punkt speichern, auf den geschossen wurde
		
		// Falls ein Schiff getroffen wurde
		if (result >= 1) {
			// Punkte, an denen kein Schiff liegen darf (-> 1 Feld Wasser zwischen Schiffen) markieren
			if (point.x - 2 >= 0 && point.y - 2 >= 0) pointsShot[point.x - 2][point.y - 2] = 0;							// links oben
			if (point.x - 2 >= 0 && point.y < game.getPitchSize()) pointsShot[point.x - 2][point.y] = 0;				// unten links
			if (point.x < game.getPitchSize() && point.y - 2 >= 0) pointsShot[point.x][point.y - 2] = 0;				// rechts oben
			if (point.x < game.getPitchSize() && point.y < game.getPitchSize()) pointsShot[point.x][point.y] = 0;		// rechts unten
		}
		
		// Falls ein Schiff versenkt wurde
		if (result == 2) {
			shipsDestroyed++;
			
			
			// Restliche Felder markieren, an denen kein Schiff liegen darf (Kopfseiten des Schiffs)
			int x = point.x - 1;
			int y = point.y - 1;
			
			if (x > 0 && pointsShot[x-1][y] >= 1) {
				while (x >= 0 && pointsShot[x][y] >= 1) x--;
				if (x >= 0) pointsShot[x][y] = 0;
				x++;
				while (x < game.getPitchSize() && pointsShot[x][y] >= 1) x++;
				if (x < game.getPitchSize()) pointsShot[x][y] = 0;
			}
			else if (x < game.getPitchSize() - 1 && pointsShot[x+1][y] == 1) {
				while (x >= 0 && pointsShot[x][y] >= 1) x--;
				if (x >= 0) pointsShot[x][y] = 0;
				x++;
				while (x < game.getPitchSize() && pointsShot[x][y] >= 1) x++;
				if (x < game.getPitchSize()) pointsShot[x][y] = 0;
			}
			
			else if (y > 0 && pointsShot[x][y-1] >= 1) {
				while (y >= 0 && pointsShot[x][y] >= 1) y--;
				if (y >= 0) pointsShot[x][y] = 0;
				y++;
				while (y < game.getPitchSize() && pointsShot[x][y] >= 1) y++;
				if (y < game.getPitchSize()) pointsShot[x][y] = 0;
			}
			else if (y < game.getPitchSize() - 1 && pointsShot[x][y+1] >= 1) {
				while (y >= 0 && pointsShot[x][y] >= 1) y--;
				if (y >= 0) pointsShot[x][y] = 0;
				y++;
				while (y < game.getPitchSize() && pointsShot[x][y] >= 1) y++;
				if (y < game.getPitchSize()) pointsShot[x][y] = 0;
			}
				
				
			// Falls alle Schiffe versenkt wurden und das Spiel somit vorbei ist
			if (shipsDestroyed == (game.getNumberOfShips(2) + game.getNumberOfShips(3) + game.getNumberOfShips(4) + game.getNumberOfShips(5))) {
				// Spiel wird beendet, dieser Spieler ist Sieger
				Player player = this;
				new Thread(new Runnable() {
					public void run() {
						if (player == game.getPlayer1()) {
							NotificationCenter.sendNotification("WinPlayer1", null);
						} else {
							NotificationCenter.sendNotification("WinPlayer2", null);
						}
						// Netzwerkverbindung nach Vorgaben beenden ohne das Fenster zu schließen
						game.exit(player, GameExitStatus.GAME_FINISHED);
					}
				}, "SpielBeenden").start();
			}	
		}
		
		// Falls kein Schiff getroffen wurde
		if (result == 0 && !(this instanceof NetworkPlayer)) {
			// Neuer Thread, damit Antwort returnt wird
			new Thread(new Runnable() {
				public void run() {
					otherPlayer.pass();						// Gegner den Zug geben
				}
			}, "NeuerZug").start();;
		}
		
		return result;
	}
	
	
	
	
	
	
	/**
	 * Plaziert ein Schiff an einem Puntkt auf dem Spielfeld.
	 * Wird in der Praxis nur für Spieler dar Klassen {@link HumanPlayer} und {@link AIPlayer} benötigt.
	 * 
	 * @param ship Schiff, das plaziert werden soll.
	 * @param point Punkt, an welchem das Schiff plaziert werden soll.
	 * @return True, falls das Schiff erfolgreich plaziert werden konnte. False, falls das Schiff aus irgendeinem Grund nicht plaziert werden konnte.
	 */
	public boolean placeShipAt(Ship ship, Point point) {			
		// Ueberpruefen, ob es die Schiffslaenge geben darf
		if (ship.getLength() < 2 || ship.getLength() > 5) return false;
				
		// Ueberpruefen, ob die Orientierung stimmt
		if (ship.getOrientation() != 0 && ship.getOrientation() != 1) return false;
				
		// Ueberpruefen, ob Schiff im Feld (Feld geht von 1 bis game.pitchSize)
		if (point.x < 1 || point.y < 1) return false;
		if (ship.getOrientation() == 0) {
			if (point.x + ship.getLength() - 1 > game.getPitchSize() || point.y > game.getPitchSize()) return false;
		}
		else if (ship.getOrientation() == 1) {
			if (point.y + ship.getLength() - 1 > game.getPitchSize() || point.x > game.getPitchSize()) return false;
		}
							
		// Ueberpruefen, ob Schiff mit anderem Schiff kollidiert (inkl. 1 Block Wasser Abstand)
		Point p = ship.getRootPoint();
		int countSize = 0;						// Zaehler, wie viele Schiffe dieser Laenge bereits gesetzt sind
		ship.setRootPoint(point);
		for (Ship s : ships) {
			if (s.getLength() == ship.getLength()) countSize++;
			if (s.hasColission(ship)) {
				ship.setRootPoint(p);			// rootPoint zuruecksetzen, falls es eine Kollision gibt
				return false;
			}
		}
		if (countSize >= game.getNumberOfShips(ship.getLength())) {
			ship.setRootPoint(p);				// rootPoint zuruecksetzen, falls bereits maximale Anzahl an Schiffen dieser Laenge gesetzt sind
			return false;
		}
		
		// Wenn das Schiff zum ersten Mal plaziert wird (und nicht neu plaziert wird, weil man die Position ändern möchte)
		if (!ships.contains(ship)) {
			ships.add(ship);
		}
		return true;
	}
	
	/**
	 * Liefert das Schiff, das an übergebenem Punkt liegt. Aufruf der Methode ist erfolglos, falls dieser Spieler ein Objekt der Klasse {@link NetworkPlayer} ist.
	 * 
	 * @param point Punkt, an welchem nach einem Schiff gesucht werden soll.
	 * @return Null, falls an übergebenem Punkt kein Schiff liegt. Andernfalls das gefundene Schiff.
	 */
	public Ship getShipAt(Point point) {
		for (Ship ship : ships) {
			// Überprüfen, ob der übergebene Punkt zufällig genau der Wurzelpunkt des Schiffs ist
			if (ship.getRootPoint().equals(point)) return ship;
			
			// Überprüfen, ob der übergebene Punkt innerhalb der Reichweite des Schiffskörpers ausgehend vom Wurzelpunkt liegt.
			if (ship.getOrientation() == 0 && ship.getRootPoint().getYDistance(point) == 0 && ship.getRootPoint().getXDistance(point) < ship.getLength()) {
				// Überprüfen, ob einer der Punkte, an welchen das Schiff liegt, der übergebene Punkt ist
				for(int i = 1; i < ship.getLength(); i++) {
					Point p = ship.getRootPoint().add(i, 0);
					if (p.equals(point)) return ship;
				}
			}
			else if (ship.getOrientation() == 1 && ship.getRootPoint().getXDistance(point) == 0 && ship.getRootPoint().getYDistance(point) < ship.getLength()) {
				// Überprüfen, ob einer der Punkte, an welchen das Schiff liegt, der übergebene Punkt ist
				for(int i = 1; i < ship.getLength(); i++) {
					Point p = ship.getRootPoint().add(0, i);
					if (p.equals(point)) return ship;
				}
			}
		}
		return null;
	}
	
	/**
	 * Entfernt die Plazierung das übergebene Schiff.
	 * Darf nur aufgerufen werden, wenn dieser Spieler "this.game.setReady(this)" noch NICHT aufgerufen hat.
	 * 
	 * @param ship Schiff, das entfernt werden soll.
	 * @return
	 */
	public boolean removeShip(Ship ship) {
		return ships.remove(ship);
	}
	
	/**
	 * Entfernt alle bereits plazierten Schiffe.
	 * Darf nur aufgerufen werden, wenn dieser Spieler "this.game.setReady(this)" noch NICHT aufgerufen hat.
	 */
	public void removeAllShips() {
		ships.clear();
	}
	
	
	
	/**
	 * Wird vom Gegner aufgerufen, um auf ein Schiff dieses Spielers zu schießen.
	 * 
	 * @param point Punkt, auf welchen der Gegner schießen möchte.
	 * @return 0, falls an diesem Punkt kein Schiff liegt. 1, falls an diesem Punkt ein Schiff liegt. 2, falls zusätzlich das getroffene Schiff versenkt wurde.
	 */
	protected int shot(Point point) {	
		Ship ship = getShipAt(point);
		if (ship == null) return 0;				// Wasser getroffen
		
		ship.addDamage(point);
		if (ship.isDestroyed()) return 2;		// Schiff getroffen und zerstoert
		
		return 1;								// Schiff getroffen und noch nicht zerstuert
	}
	
}
