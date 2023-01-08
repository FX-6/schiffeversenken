package Schiffeversenken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Player {

	protected Game game;
	protected Player otherPlayer;
	private List<Ship> ships;
	private int[][] pointsShot;
	private int shipsDestroyed = 0;
	
	private boolean isMyTurn = false;
		
	public Player(Game game, Player otherPlayer) {
		this.game = game;
		this.otherPlayer = otherPlayer;
		this.pointsShot = new int[game.getPitchSize()][game.getPitchSize()];
		this.ships = new ArrayList<Ship>();
		shipsDestroyed = 0;
		
		for (int[] array : pointsShot) {
			Arrays.fill(array, 0);
		}
	}
	
	
	// Getter und Setter
	public boolean isMyTurn() {return isMyTurn;}
	
	public void setOtherPlayer(Player player) {this.otherPlayer = player;}
	public void setMyTurn(boolean bool) {this.isMyTurn = bool;}
	
	
	
	// Getter und Setter nur für Save/Load
	public int getShipsDestroyed() {return this.shipsDestroyed;}
	public int[][] getPointsShot() {return this.pointsShot;}
	public List<Ship> getShipList() {return ships;}
	
	
	public void refreshPointsShot() {
		this.pointsShot = new int[game.getPitchSize()][game.getPitchSize()];
	}
	
	
	
	public abstract void pass();
	
	//public abstract void ready();
	
	
	
	// Muss aufgerufen werden, um zu Schießen.
	public int shoot(Point point) {		
		int result = otherPlayer.shot(point);
		pointsShot[point.x - 1][point.y - 1] = result;
		
		if (result == 0) {
			//otherPlayer.pass();
		}
		
		else if (result == 2) {
			shipsDestroyed++;
			if (shipsDestroyed == (game.getNumberOfShips(2) + game.getNumberOfShips(3) + game.getNumberOfShips(4) + game.getNumberOfShips(5))) {
				// Spiel wird beendet, dieser Spieler ist Sieger
				System.out.println("SPIEL BEENDEN");
			}
		}
		
		return result;
	}
	
	// Muss aufgerufen werden, wenn Spiel als Client die Feldgroesse erhalten hat
	public void updatePointsShot() {
		this.pointsShot = new int[game.getPitchSize()][game.getPitchSize()];
		for(int[] array : pointsShot) {
			Arrays.fill(array, 0);
		}
	}
	
	
	
	
	
	// Wird nur von HumanPlayer und AIPlayer benötigt, ist der Einfachheit halber aber hier implementiert!
	
	public boolean placeShipAt(Ship ship, Point point) {	
		// Ueberpruefen, ob es die Schiffslaenge geben darf
		if (ship.getLength() < 2 || ship.getLength() > 5) return false;
		
		// Ueberpruefen, ob die Orientierung stimmt
		if (ship.getOrientation() != 0 && ship.getOrientation() != 1) return false;
		
		// Ueberpruefen, ob Schiff im Feld (Feld geht von 1 bis game.pitchSize)
		if (point.x < 1 || point.y < 1) return false;
		if (ship.getOrientation() == 0) {
			if (point.x + ship.getLength() > game.getPitchSize() || point.y > game.getPitchSize()) return false;
		}
		if (ship.getOrientation() == 1) {
			if (point.y + ship.getLength() > game.getPitchSize() || point.x > game.getPitchSize()) return false;
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
		ships.add(ship);
		return true;
	}
	
	public Ship getShipAt(Point point) {
		for (Ship ship : ships) {
			if (ship.getRootPoint().equals(point)) return ship;
			
			if (ship.getOrientation() == 0 && ship.getRootPoint().getYDistance(point) == 0 && ship.getRootPoint().getXDistance(point) < ship.getLength()) {
				for(int i = 1; i < ship.getLength(); i++) {
					Point p = ship.getRootPoint().add(i, 0);
					if (p.equals(point)) return ship;
				}
			}
			else if (ship.getOrientation() == 1 && ship.getRootPoint().getXDistance(point) == 0 && ship.getRootPoint().getYDistance(point) < ship.getLength()) {
				for(int i = 1; i < ship.getLength(); i++) {
					Point p = ship.getRootPoint().add(0, i);
					if (p.equals(point)) return ship;
				}
			}
		}
		return null;
	}
	
	
	
	// Muss in NetworkPlayer überschrieben werden!
	protected int shot(Point point) {	
		Ship ship = getShipAt(point);
		if (ship == null) return 0;				// Wasser getroffen
		
		ship.addDamage(point);
		if (ship.isDestroyed()) return 2;		// Schiff getroffen und zerstoert
		
		return 1;								// Schiff getroffen und noch nicht zerstuert
	}
	
}
