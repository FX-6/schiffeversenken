package Schiffeversenken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Timestamp;
import java.util.List;

public class SaveGameHandler {

	/*
	 * Idee:
	 * Wenn ein neues Spiel gespeichert werden soll, wird ein neues Objekt von dieser Klasse erstellt, das sich automatisch alle nötigen Daten
	 * über Main.currentGame holt und diese speichert. Zusätzlich wird ein vom Nutzer vergebener Name gespeichert.
	 * Ein erneutes Speichern des selben Spiels erzeugt eine neue Datei und überschreibt/löscht den alten Spielstand nicht!
	 *
	 * Der Dateiname enthält den Namen und die eindeutige ID, welche durch den Timestamp erzeugt wird, voneinander getrennt durch einen Punkt.
	 * 
	 * Nach dem Spiechern/Laden eines Spiels wird dieses Objekt nicht weiter benötigt und kann dem Garbage-Collector überlassen werden - auch wenn das
	 * Spiel danach weiter gespielt wird.
	 */
	
	// Filename: "<Name>.<Timestamp>"
	
	
	private File file;
	
	// Um ein neues Spiel zu speichern
	public SaveGameHandler(String name) throws IOException {
		String fileName = System.currentTimeMillis() + "." + name + ".json";
		
		file = new File(SettingsHandler.saveGamesPath + File.separator + fileName);
		
		
		
		
		
		// Game
		String[] game = new String[5];
		game[0] = writeAttribute("name", name, "");
		game[1] = writeAttribute("pitchSize", Main.currentGame.getPitchSize(), "");
		
		int[] ships = new int[4];
		for (int i = 0; i < 4; i++) {
			ships[i] = Main.currentGame.getNumberOfShips(i + 2);
		}
		game[2] = writeArray("ships", ships, "\t");
		
		game[3] = writeAttribute("player1", Main.currentGame.getPlayer1().getClass().toString(), "");
		game[4] = writeAttribute("player2", Main.currentGame.getPlayer2().getClass().toString(), "");
		
		
		
		// Player 1
		String[] player1 = new String[4];
		player1[0] = writeAttribute("isMyTurn", Main.currentGame.getPlayer1().isMyTurn(), "");
		player1[1] = writeAttribute("shipsDestroyed", Main.currentGame.getPlayer1().getShipsDestroyed(), "");
		player1[2] = write2DArray("pointsShot", Main.currentGame.getPlayer1().getPointsShot() , "\t");
		
		List<Ship> list1 = Main.currentGame.getPlayer1().getShipList();
		String[] shipList1 = new String[list1.size()];
		for (Ship ship : list1) {
			String[] attributes = new String[4];
			attributes[0] = writeAttribute("rootPoint", ship.getRootPoint().toString(), "\t");
			attributes[1] = writeAttribute("length", ship.getLength(), "\t");
			attributes[2] = writeAttribute("orientation", ship.getOrientation(), "\t");
			attributes[3] = writeArray("damage", ship.getDamage(), "\t");
			
			shipList1[list1.indexOf(ship)] = writeObject(attributes, "\t");
		}
		player1[3] = writeArray("ships", shipList1, "\t", false);
		
		
		
		// Player 2
		String[] player2 = new String[4];
		player2[0] = writeAttribute("isMyTurn", Main.currentGame.getPlayer2().isMyTurn(), "");
		player2[1] = writeAttribute("shipsDestroyed", Main.currentGame.getPlayer2().getShipsDestroyed(), "");
		player2[2] = write2DArray("pointsShot", Main.currentGame.getPlayer2().getPointsShot() , "\t");
		
		List<Ship> list2 = Main.currentGame.getPlayer2().getShipList();
		String[] shipList2 = new String[list2.size()];
		for (Ship ship : list2) {
			String[] attributes = new String[4];
			attributes[0] = writeAttribute("rootPoint", ship.getRootPoint().toString(), "\t");
			attributes[1] = writeAttribute("length", ship.getLength(), "\t");
			attributes[2] = writeAttribute("orientation", ship.getOrientation(), "\t");
			attributes[3] = writeArray("damage", ship.getDamage(), "\t");
			
			shipList2[list1.indexOf(ship)] = writeObject(attributes, "\t");
		}
		player2[3] = writeArray("ships", shipList2, "\t", false);
		
		
		
		
		
		
		FileWriter writer = new FileWriter(file);
		writer.write(writeObject(game, ""));
		writer.write("\n");
		writer.write(writeObject(player1, ""));
		writer.write("\n");
		writer.write(writeObject(player2, ""));
		writer.flush();
		writer.close();
		
	}
	
	
	// Um ein gespeichertes Spiel zu laden
	public SaveGameHandler(Timestamp id) {
		
	}
	
	
	
	private String writeObject(String[] attributes, String indentation) {
		String string = indentation + "{\n";
		
		for (String attribute : attributes) {
			string += indentation + "\t" + attribute + ",\n";
		}
		
		string = string.substring(0, string.length() - 2);
		string += "\n" + indentation + "}";
		return string;
	}
	
	
	private String writeAttribute(String key, Object value, String indentation) {
		if (value instanceof String) {
			return indentation + "\"" + key + "\":\"" + value + "\"";
		}
		return indentation + "\"" + key + "\":" + value;
	}
	
	
	private String writeArray(String key, Object[] values, String indentation, boolean isString) {
		String string = "\"" + key + "\":[\n";
		if (isString) {
			for (Object obj : values) {
				string += indentation + "\t\"" + obj + "\",\n";
			}
		}
		else {
			for (Object obj : values) {
				string += indentation + "\t" + obj + ",\n";
			}
		}
		if (values.length > 0) string = string.substring(0, string.length() - 2);
		string += "\n" + indentation + "]";
		return string;
	}
	
	private String writeArray(String key, int[] values, String indentation) {
		String string = "\"" + key + "\":[\n";
		for (int obj : values) {
			string += indentation + "\t" + obj + ",\n";
		}
		if (values.length > 0) string = string.substring(0, string.length() - 2);
		string += "\n" + indentation + "]";
		return string;
	}
	
	private String writeArray(Object[] values, String indentation, boolean isString) {
		String string = "[\n";
		if (isString) {
			for (Object obj : values) {
				string += indentation + "\t\"" + obj + "\",\n";
			}
		}
		else {
			for (Object obj : values) {
				string += indentation + "\t" + obj + ",\n";
			}
		}
		if (values.length > 0) string = string.substring(0, string.length() - 2);
		string += "\n" + indentation + "]";
		return string;
	}
	
	
	
	private String write2DArray(String key, int[][] array, String indentation) {
		String string = "\"" + key + "\":[\n";
		for (int[] values : array) {
			string += indentation + "\t[\n";
			for (int value : values) {
				string += indentation + "\t\t" + value + ",\n";
			}
			string = string.substring(0, string.length() - 2);
			string += "\n" + indentation + "\t],\n"; 
		}
		string = string.substring(0, string.length() - 2);
		string += "\n" + indentation + "]";
		return string;
	}
	
}
