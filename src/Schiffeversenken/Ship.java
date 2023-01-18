package Schiffeversenken;

import java.util.Arrays;

/**
 * Stellt eine Instanz eines Schiffs dar. Das schiff wird von einem Wurzelpunkt und einer Länge sowie einer Orientierung aufgespannt.
 */

public class Ship {

	/**
	 *  Speicherung, an welcher Stelle das Schiff bereits beschossen wurde. Index = Entfernung zum Wurzelpunkt ind Feldern; 0 = Nicht beschädigt, 1 = beschädigt.
	 */
	private int[] damage;
	
	/**
	 * Speichert die Länge des Schiffs (2 <= length <= 5)
	 */
	private int length;
	
	/**
	 * Speichert die Orientierung des Schiffs (0 = horizontal, 1 = vertikal)
	 */
	private int orientation;
	
	/**
	 * Speichert den Wurzelpunkt des Schiffs (dieser liegt graphisch gesehen immer oben links. Er hat also die kleinste x- und die kleinste y-Koordinate des Schiffs)
	 */
	private Point rootPoint;
	
	/**
	 * Erzeugt eine neue Instanz eines Schiffs.
	 * 
	 * @param length Länge, die das Schiff haben soll (Länge zwischen 2 und 5).
	 * @param orientation Orientierung, die das Schiff haben soll (0 = horizontal, 1 = vertikal).
	 */
	public Ship(int length, int orientation) {
		this.length = length;
		this.rootPoint = new Point(0, 0);
		this.orientation = orientation;
		this.damage = new int[this.length];
		
		Arrays.fill(damage, 0);					// Füllt damage mit 0 (zur Sicherheit)
	}
	
	// Getter und Setter
	public int getLength() {return this.length;}
	public int getOrientation() {return this.orientation;}
	public Point getRootPoint() {return this.rootPoint;}
	
	public void setRootPoint(Point point) {this.rootPoint = point;}
	
	
	// Getter und Setter nur für Save/Load
	public int[] getDamage() {return damage;}
	
	public void setDamage(int[] damage) {this.damage = damage;}
	

	
	
	/**
	 * Status der vollständigen Zerstörung des Schiffs.
	 * 
	 * @return True, falls alle Felder des Schiffs beschossen wurden und das Schiff somit vollständig zerstört wurde. Andernfalls false.
	 */
	public boolean isDestroyed() {
		for (int i : damage) {
			if (i == 0) {
				return false;					// Wenn Schiff noch eine nicht beschädigte Stelle hat, wird return false aufgerufen
			}
		}
		return true;
	}
		
	/**
	 * Fügt dem Schiff Schaden zu, sofern sich ein Teil des Schiffs am übergebenen Punkt befindet. Andernfalls passiert nichts.
	 * 
	 * @param point Punkt, auf den geschossen wurde.
	 * @return True, falls dieses Schiff an übergebenem Punkt liegt und ihm Schaden hinzugefügt werden konnte. Andernfalls false.
	 */
	public boolean addDamage(Point point) {
		// Suche nach Punkt, falls Orientierung des Schiffs horizontal ist
		if (orientation == 0 && rootPoint.y == point.y) {
			int xDistance = rootPoint.getXDistance(point);
			if (xDistance < length) {							// Wenn Schiff auf "point" liegt
				damage[xDistance] = 1;
				return true;
			}
		}
		// Suche nach Punkt, falls Orientierung des Schiffs vertikal ist
		else if (orientation == 1 && rootPoint.x == point.x) {
			int yDistance = rootPoint.getYDistance(point);
			if (yDistance < length) {							// Wenn Schiff auf "point" liegt
				damage[yDistance] = 1;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Überprüft, ob dieses Schiff mit übergebenem Schiff kollidiert (=> Es gibt ein Feld für das gilt: Abstand von diesem Schiff und übergebenem Schiff ist kleiner als 1)
	 * 
	 * @param oShip Schiff, welches auf Kollisionen mit diesem Schiff überprüft werden soll.
	 * @return True, falls der Abstand der beiden Schiffe an irgendeinem Punkt kleiner als 1 ist. Andernfalls false.
	 */
	public boolean hasColission(Ship oShip) {
		// Der ganze Algorithmus ist eine effizientere Implementierung eines Algortihmus, der für jeden Punkt des einen Schiffs überprüft, ob ein Punkt des anderen Schiffs dort liegt
		// => Schachtelung zweier Schleifen, was Laufzeit O(n^2) ergibt
		
		// Beide Schiffe horiziontal
		if (orientation == 0 && oShip.orientation == 0) {
			
			// Schiffe liegen in gleicher oder zwei benachbarten Zeilen
			if (rootPoint.getYDistance(oShip.rootPoint)  < 2) {
				
				// Dieses Schiff liegt weiter links als das übergebene Schiff und es ist kein Feld Abstand zwischen ihnen
				if (rootPoint.x < oShip.rootPoint.x && rootPoint.getXDistance(oShip.rootPoint) <= length) {
					return true;
				}
				
				// Dieses Schiff liegt weiter rechts als das übergebene Schiff und es ist kein Feld Abstand zwischen ihnen
				else if (rootPoint.x >= oShip.rootPoint.x && rootPoint.getXDistance(oShip.rootPoint) <= oShip.length) {
					return true;
				}
			}
		}
		
		// Beide Schiffe vertikal
		else if (orientation == 1 && oShip.orientation == 1) {
			
			// Schiffe liegen in gleicher ider zwei benachbarten Spalten
			if (rootPoint.getXDistance(oShip.rootPoint)  < 2) {
				
				// Dieses Schiff liegt weiter oben als das übergebene Schiff und es ist kein Feld Abstand zwischen ihnen
				if (rootPoint.y < oShip.rootPoint.y && rootPoint.getYDistance(oShip.rootPoint) <= length) {
					return true;
				}
				
				// Dieses Schiff liegt weiter unten als das übergebene Schiff und es ist kein Feld Abstand zwischen ihnen
				else if (rootPoint.y >= oShip.rootPoint.y && rootPoint.getYDistance(oShip.rootPoint) <= oShip.length) {
					return true;
				}
			}
		}
		
		// Ein Schiff horizontal, das andere vertikal
		else if (orientation == 0 && oShip.orientation == 1) {
			// Berechnung eines Offsets im Fall dass übergebenes Schiff über diesem Schiff liegt (dann wird Koordinate des unteren Endes von übergebenem Schiff benötigt)
			int offset = rootPoint.y < oShip.rootPoint.y ? 0 : Math.min(oShip.length, rootPoint.getYDistance(oShip.rootPoint));			// Wenn "oShip" über Schiff liegt, muss die Y Distanz und maximal Länge von "oShip" der Offset seines rootPoints sein, andernfalls braucht es keinen
			for (int i = 0; i < length; i++) {
				
				// Beide Schiffe liegen so, dass sie ein "T" oder "+" formen
				if (rootPoint.add(i, 0).getXDistance(oShip.rootPoint) < 2 && rootPoint.add(i, 0).getYDistance(oShip.rootPoint.add(0, offset)) < 2) {
					return true;
				}
			}
		}
		
		// Ein Schiff vertikal, das andere horizontal
		else if (orientation == 1 && oShip.orientation == 0) {
			// Berechnung eines Offsets im Fall dass übergebenes Schiff links von diesem Schiff liegt (dann wird die Koordinate des rechten Endes von übergebenem Schiff benötigt)
			int offset = rootPoint.x < oShip.rootPoint.x ? 0 : Math.min(oShip.length, rootPoint.getXDistance(oShip.rootPoint));			// Wenn "oShip" links von  Schiff liegt, muss die X Distanz und maximal die Länge von "oShip" der Offset seines rootPoints sein, andernfalls braucht es keinen
			for (int i = 0; i < length; i++) {
				
				// Beide Schiffe liegen so, dass sei ein "T" oder "+" formen
				if (rootPoint.add(0, i).getYDistance(oShip.rootPoint) < 2 && rootPoint.add(0, i).getXDistance(oShip.rootPoint.add(offset, 0)) < 2) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	public String toString() {
		String string = "";
		for (int i = 0; i < length; i++) {
			if (damage[i] == 0) string += "=";
			else if (damage[i] == 1) string += "x";
		}
		return "Ship at " + rootPoint + " with orientation " + orientation + " and condition " + string;
	}
	
}
