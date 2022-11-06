package Schiffeversenken;

import java.util.Arrays;
import java.util.HashMap;

public abstract class Player {

	private Game game;
	private Player otherPlayer;
	private HashMap<Point, Ship> ships;
	private int[][] pointsShot;
	private int shipsDestroyed = 0;
	
	public Player(Game game, Player otherPlayer) {
		this.game = game;
		this.otherPlayer = otherPlayer;
		this.pointsShot = new int[game.getPitchSize()][game.getPitchSize()];
		this.ships = new HashMap<Point, Ship>();
		shipsDestroyed = 0;
		
		for (int[] array : pointsShot) {
			Arrays.fill(array, 0);
		}
	}
	
	
	public abstract void pass();
	
	
	
	// Muss aufgerufen werden, um zu Schießen.
	public void shoot(Point point) {
		int result = otherPlayer.shot(point);
		pointsShot[point.x][point.y] = result;
		
		if (result == 2) {
			shipsDestroyed++;
			if (shipsDestroyed == (game.getNumberOfShips(2) + game.getNumberOfShips(3) + game.getNumberOfShips(4) + game.getNumberOfShips(5))) {
				// Spiel wird beendet, dieser Spieler ist Sieger
			}
		}
	}
	
	
	
	
	// Getter und Setter
	public void setOtherPlayer(Player player) {this.otherPlayer = player;}
	
	
	
	
	
	// Wird nur von HumanPlayer und AIPlayer benötigt, ist der Einfachheit halber aber hier implementiert!
	
	public boolean placeShipAt(Ship ship, Point point) {
		Point p = ship.getRootPoint();
		ship.setRootPoint(point);
		for (Ship s : ships.values()) {
			if (s.hasColission(ship)) {
				ship.setRootPoint(p);			// rootPoint zuruecksetzen, falls es eine Kollision gibt
				return false;
			}
		}
		ships.put(ship.getRootPoint(), ship);
		return true;
	}
	
	protected Ship getShipAt(Point point) {
		return ships.get(point);
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
