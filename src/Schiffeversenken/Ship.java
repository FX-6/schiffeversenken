package Schiffeversenken;

import java.util.Arrays;

public class Ship {

	private int[] damage;			// index entspricht Entfernung vom rootPoint entlang des Schiffes
	private int length;				// 2 <= length <= 5
	private int orientation;		// 0 = horizontal, 1 = vertikal
	private Point rootPoint;		// rootPoint graphisch gesehen immer oben links (kleinstes x und kleinstes y)
	
	public Ship(int length, int orientation) {
		this.length = length;
		this.rootPoint = new Point(0, 0);
		this.orientation = orientation;
		this.damage = new int[this.length];
		
		Arrays.fill(damage, 0);					// Fuellt damage mit 0 (zur Sicherheit)
	}
	
	// Getter und Setter
	public int getLength() {return this.length;}
	public int getOrientation() {return this.orientation;}
	public Point getRootPoint() {return this.rootPoint;}
	
	public void setRootPoint(Point point) {this.rootPoint = point;}
	
	
	// Getter und Setter nur für Save/Load
	public int[] getDamage() {return damage;}
	
	public void setDamage(int[] damage) {this.damage = damage;}
	

	
	
	// Ueberprueft, ob ein Schiff vollstaendig zerstoert ist
	public boolean isDestroyed() {
		for (int i : damage) {
			if (i == 0) {
				return false;					// Wenn Schiff noch eine nicht beschaedigte Stelle hat, wird return false aufgerufen
			}
		}
		return true;
	}
	
	// Fuegt dem Schiff Schaden zu, sofern ein Teil des Schiffes an Punkt "point" liegt. Wenn ja, gibt "true" zurück, andernfalls "false"
	public boolean addDamage(Point point) {
		if (orientation == 0 && rootPoint.y == point.y) {
			int xDistance = rootPoint.getXDistance(point);
			if (xDistance < length) {							// Wenn Schiff auf "point" liegt
				damage[xDistance] = 1;
				return true;
			}
		}
		else if (orientation == 1 && rootPoint.x == point.x) {
			int yDistance = rootPoint.getYDistance(point);
			if (yDistance < length) {							// Wenn Schiff auf "point" liegt
				damage[yDistance] = 1;
				return true;
			}
		}
		return false;
	}
	
	// Ueberprueft, ob Shiff mit anderem Schiff "oShip" an irgendeiner Stelle kolidiert (oder aufgrund des 1-Block Wasser Abstands nicht setzbar ist). Wenn ja, gibt "true" zurück, andernfalls "false"
	public boolean hasColission(Ship oShip) {
		// Beide Schiffe horiziontal
		if (orientation == 0 && oShip.orientation == 0) {
			if (rootPoint.getYDistance(oShip.rootPoint)  < 2) {
				if (rootPoint.x < oShip.rootPoint.x && rootPoint.getXDistance(oShip.rootPoint) <= length) {
					return true;
				}
				else if (rootPoint.x >= oShip.rootPoint.x && rootPoint.getXDistance(oShip.rootPoint) <= oShip.length) {
					return true;
				}
			}
		}
		
		// Beide Schiffe vertikal
		else if (orientation == 1 && oShip.orientation == 1) {
			if (rootPoint.getXDistance(oShip.rootPoint)  < 2) {
				if (rootPoint.y < oShip.rootPoint.y && rootPoint.getYDistance(oShip.rootPoint) <= length) {
					return true;
				}
				else if (rootPoint.y >= oShip.rootPoint.y && rootPoint.getYDistance(oShip.rootPoint) <= oShip.length) {
					return true;
				}
			}
		}
		
		// Ein Schiff horizontal, das andere vertikal
		else if (orientation == 0 && oShip.orientation == 1) {
			int offset = rootPoint.y < oShip.rootPoint.y ? 0 : Math.min(oShip.length, rootPoint.getYDistance(oShip.rootPoint));			// Wenn "oShip" über Schiff liegt, muss die Y Distanz und maximal Länge von "oShip" der Offset seines rootPoints sein, andernfalls braucht es keinen
			for (int i = 0; i < length; i++) {
				if (rootPoint.add(i, 0).getXDistance(oShip.rootPoint) < 2 && rootPoint.add(i, 0).getYDistance(oShip.rootPoint.add(0, offset)) < 2) {
					return true;
				}
			}
		}
		
		// Ein Schiff vertikal, das andere horizontal
		else if (orientation == 1 && oShip.orientation == 0) {
			int offset = rootPoint.x < oShip.rootPoint.x ? 0 : Math.min(oShip.length, rootPoint.getXDistance(oShip.rootPoint));			// Wenn "oShip" links von  Schiff liegt, muss die X Distanz und maximal die Länge von "oShip" der Offset seines rootPoints sein, andernfalls braucht es keinen
			for (int i = 0; i < length; i++) {
				if (rootPoint.add(0, i).getYDistance(oShip.rootPoint) < 2 && rootPoint.add(0, i).getXDistance(oShip.rootPoint.add(offset, 0)) < 2) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	// Produziert einen beschreibenden String zu diesem Schiff
	public String toString() {
		String string = "";
		for (int i = 0; i < length; i++) {
			if (damage[i] == 0) string += "=";
			else if (damage[i] == 1) string += "x";
		}
		return "Ship at " + rootPoint + " with orientation " + orientation + " and condition " + string;
	}
	
}
