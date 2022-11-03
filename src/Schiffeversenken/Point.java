package Schiffeversenken;

public class Point {

	public final int x;
	public final int y;
	
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
}
