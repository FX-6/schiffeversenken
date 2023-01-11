package Schiffeversenken;

public class Point {

	// Liest einen Punkt aus einem String der Form "x,y" (Siehe toString())
	public static Point parsePoint(String string) {
		String[] list = string.split(",");
		int x = Integer.parseInt(list[0]);
		int y = Integer.parseInt(list[1]);
		return new Point(x, y);
	}
	
	
	
	public final int x;			// SPALTE
	public final int y;			// ZEILE
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	// Betrag der Entfernung zweier Punkte in x-Richtung
	public int getXDistance(Point oPoint) {
		int distance = oPoint.x - this.x;
		return distance >= 0 ? distance : -distance;
	}
	
	// Betrag der Entfernung zweier Punkte in y-Richtung
	public int getYDistance(Point oPoint) {
		int distance = oPoint.y - this.y;
		return distance >= 0 ? distance : -distance;
	}
	
	// Gibt einen neuen Punkt zurück, dessen koordinaten um "x" und "y" verändert wurden
	public Point add(int x, int y) {
		return new Point(this.x + x, this.y + y);
	}
	
	// Produziert einen beschreibenden String zu diesem Punkt
	public String toString() {
		return x + "," + y;
	}
	
	// Ueberprueft, ob ein Object object inhaltlich gleich zu diesem Punkt ist (ob beide Koordinaten uebereinstimmen)
	@Override
	public boolean equals(Object object) {
		if (object instanceof Point) {
			Point oPoint = (Point) object;
			return x == oPoint.x && y == oPoint.y;
		}
		return false;
	}
}
