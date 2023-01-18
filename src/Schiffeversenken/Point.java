package Schiffeversenken;


/**
 * Dient als Datentyp um einen Punkt auf dem Spielfeld referenzieren zu können.
 */
public class Point {

	/**
	 * Erzeugt einen Punkt aus einem String der Form "x,y" (Siehe {@link #toString()}). Es werden keine Fehler abgefangen, falls der übergebene String nicgt der geforderten Form entspricht.
	 * 
	 * @param string Zeichenkette, aus welcher der Punkt gelesen werden soll.
	 * @return Aus der Zeichenkette gelesener Punkt
	 */
	public static Point parsePoint(String string) {
		String[] list = string.split(",");
		int x = Integer.parseInt(list[0]);
		int y = Integer.parseInt(list[1]);
		return new Point(x, y);
	}
	
	
	/**
	 * x-Koordinate (horizontale Achse; Spalte des Spielfelds)
	 */
	public final int x;
	
	/**
	 * y-Koordinate (vertikale Achse; Zeile des Spielfelds)
	 */
	public final int y;
	
	/**
	 * Erzeugt eine Instanz eines Punktes.
	 * @param x x-Koordinate des Punktes (horizontale Achse; Spalte des Spielfelds).
	 * @param y y-Koordinate des Punktes (vertikale Achse; Zeile des Spielfelds).
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Berechnet den Betrag der Entfernung dieses Punktes zum übergebenen Punkt im Bezug auf ausschließlich die x-Koordinate.
	 * 
	 * @param oPoint Punkt, zu welchem die Entfernung in x-Richtung berechnet werden soll
	 * @return Betrag der Entfernung der beiden Punkte in x-Richtung. 0, falls beide Punkte in derselben Spalte liegen.
	 */
	public int getXDistance(Point oPoint) {
		int distance = oPoint.x - this.x;
		return distance >= 0 ? distance : -distance;
	}
	
	/**
	 * Berechnet den Betrag der Entfernung dieses Punktes zum übergebenen Punkt im Bezug auf ausschließlich die y-Koordinate.
	 * 
	 * @param oPoint Punkt, zu welchem die Entfernung in y-Richtung berechnet werden soll
	 * @return Betrag der Entfernung der beiden Punkte in y-Richtung. 0, falls beide Punkte in derselben Zeile liegen.
	 */
	public int getYDistance(Point oPoint) {
		int distance = oPoint.y - this.y;
		return distance >= 0 ? distance : -distance;
	}
	
	/**
	 * Erzeugt einen neuen Punkt auf grundlage dieses Punktes, mit um x und y veränderte Koordinaten.
	 * 
	 * @param x x-Abstand, um welchen sich der neue Punkt von diesem Punkt unterscheiden soll.
	 * @param y y-Abstand, um welchen sich der neue Punkt von diesem Punkt unterscheiden soll.
	 * @return Neu erzeugter Punkt.
	 */
	public Point add(int x, int y) {
		return new Point(this.x + x, this.y + y);
	}
	
	/**
	 * Erzeugt einen String der Form "x,y", der diesen Punkt repräsentiert.
	 */
	public String toString() {
		return x + "," + y;
	}
	
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Point) {
			Point oPoint = (Point) object;
			return x == oPoint.x && y == oPoint.y;
		}
		return false;
	}
}
