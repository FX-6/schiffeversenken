package Schiffeversenken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Notifications.NotificationCenter;

/**
 * Speichert den aktuellen Spielstand in einer neuen Datei oder lädt einen Spielstand aus einer Datei.
 */

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
	
	/**
	 * Speichert den aktuellen Spielstand in einer Datei. Speicherformt ist konform zum JSON-Format.
	 * Nach erzeugung eines Objekts durch diesen Konstruktor, ist dieses Objekt nicht mehr zu gebrauchen.
	 * 
	 * @param id Eindeutige ID, welche diesen Spielstand identifizieren soll.
	 * @param name Optionaler Name, der die Identifizierbarkeit des Spielstands für den Nutzer bei der Auswahl im Ladevorgang erleichtert.
	 * @throws IOException Der Spielstand konnte nicht in eine Datei geschrieben werden.
	 */
	public SaveGameHandler(String id, String name) throws IOException {
		String fileName = id + "#" + name + ".json";
		
		file = new File(SettingsHandler.saveGamesPath + File.separator + fileName);
		
		
		// Es werden nach und nach immer längere Strings erstellt, die Objekte mit ihren Attributen
		// als JSON-String darstellen. Erst zum Schluss werden sie alle in eine Datei geschrieben.
		
		
		// Speicherung von Game-Objekt
		String[] game = new String[5];
		game[0] = writeAttribute("name", name, "");																	// Übergebener Name; Wird beim Laden nicht benötigt/berücksichtigt
		game[1] = writeAttribute("pitchSize", Main.currentGame.getPitchSize(), "");									// Größe des Spielfelds
		
		int[] ships = new int[4];
		for (int i = 0; i < 4; i++) {																				
			ships[i] = Main.currentGame.getNumberOfShips(i + 2);
		}
		game[2] = writeArray("ships", ships, "\t");																	// Anzahl der Schiffe für die Schiffsgrößen
		
		game[3] = writeAttribute("player1", Main.currentGame.getPlayer1().getClass().toString(), "");				// Art (Klasse) des ersten Spielers
		game[4] = writeAttribute("player2", Main.currentGame.getPlayer2().getClass().toString(), "");				// Art (Klasse) des zweiten Spielers
		
		
		
		// Speicherung von Player1-Objekt
		String[] player1 = new String[4];
		if (Main.currentGame.getPlayer1() instanceof AIPlayer) player1 = new String[5];								// Falls Spieler 1 eine KI ist, muss zusätzliches gespeichert werden
		player1[0] = writeAttribute("isMyTurn", Main.currentGame.getPlayer1().isMyTurn(), "");						// Ob Spieler am Zug ist
		player1[1] = writeAttribute("shipsDestroyed", Main.currentGame.getPlayer1().getShipsDestroyed(), "");		// Anzahl der beim Gegner zerstörten Schiffe
		player1[2] = write2DArray("pointsShot", Main.currentGame.getPlayer1().getPointsShot() , "\t");				// Karte der bereits beschossenen Punkte
		
		List<Ship> list1 = Main.currentGame.getPlayer1().getShipList();
		String[] shipList1 = new String[list1.size()];
		for (Ship ship : list1) {
			String[] attributes = new String[4];
			attributes[0] = writeAttribute("rootPoint", ship.getRootPoint().toString(), "\t");						// Ursprung eines Schiffs
			attributes[1] = writeAttribute("length", ship.getLength(), "\t");										// Länge eines Schiffs
			attributes[2] = writeAttribute("orientation", ship.getOrientation(), "\t");								// Orientierung eines Schiffs
			attributes[3] = writeArray("damage", ship.getDamage(), "\t");											// Beschädigungen eines Schiffs
			
			shipList1[list1.indexOf(ship)] = writeObject(attributes, "\t");											// Ein Schiff
		}
		player1[3] = writeArray("ships", shipList1, "\t", false);													// Liste aller eigenen Schiffe
		
		if (Main.currentGame.getPlayer1() instanceof AIPlayer) {
			AIPlayer ai = (AIPlayer) Main.currentGame.getPlayer1();
			player1[4] = write2DArray("priorities", ai.getPriorities(), "\t");										// Prioritäten für den nächsten Schuss
		}
		
		
		
		// Speicherung von Player2-Objekt
		// Kommentarie siehe oben bei Player1
		String[] player2 = new String[4];
		if (Main.currentGame.getPlayer2() instanceof AIPlayer) player2 = new String[5];
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
			
			shipList2[list2.indexOf(ship)] = writeObject(attributes, "\t");
		}
		player2[3] = writeArray("ships", shipList2, "\t", false);
		
		if (Main.currentGame.getPlayer2() instanceof AIPlayer) {
			AIPlayer ai = (AIPlayer) Main.currentGame.getPlayer2();
			player2[4] = write2DArray("priorities", ai.getPriorities(), "\t");
		}
		
		
		
		
		// Schreiben der JSON-Strings in die Datei.
		
		FileWriter writer = new FileWriter(file);
		writer.write(writeObject(game, ""));				// Das Spiel
		writer.write("\n");
		writer.write(writeObject(player1, ""));				// Spieler 1
		writer.write("\n");
		writer.write(writeObject(player2, ""));				// Spieler 2
		writer.flush();
		writer.close();
		
	}
	
	
	/**
	 * Lädt einen Spielstand aus einer Datei.
	 * Nach Erzeugung eines Objekts durch diesen Konstruktor, ist das Objekt nicht mehr zu gebrauchen.
	 * 
	 * @param id Eindeutige ID, welche den Spielstand, der geladen werden soll, identifiziert.
	 * @throws FileNotFoundException Der Spielstand mit der übergebenen ID kann nicht geladen werden, da die zugehörige Datei nicht existiert.
	 */
	public SaveGameHandler(String id) throws FileNotFoundException {
		File file = null;
		
		// Sucht die richtige Datei mit der übergebenen ID
		for (String string : SettingsHandler.getSavedGames()) {
			if (string.startsWith(".")) continue;
			if (string.split("#")[0].equals(id)) {
				file = new File(SettingsHandler.saveGamesPath + File.separator + string);
				break;
			}
		}
		
		
		// Das Game-Objekt und die Objekte Player1 und Player2 existieren bereits. Es müssen lediglich ihre Attribute aktualisiert werden,
		// die relevant für den Spielstand sind.
		// Attribute, die den Zustand des Spiels, der Netzwerkverbindung, o.ä. speichern, müssen/dürfen nicht überschrieben werden.
		
				
		Scanner reader = new Scanner(file);
		
		int objectDepth = 0;
		int object = 0;					// 1 = Game, 2 = Player1, 3 = Player2
		
		// Liest jede Zeile aus der Datei
		while (reader.hasNextLine()) {
			String line = reader.nextLine().trim();
			
			if (line.contains("{")) {
				objectDepth++;
				if (objectDepth == 1) {
					object++;
				}
			}
			if (line.contains("}")) objectDepth--;

			
			if (line.contains(":")) {
				String key = line.split(":")[0].replace("\"", "");
				String value = line.split(":")[1].replace(",", "");
				
				
				// Liest Feldgröße ein -> Game
				if (key.equals("pitchSize")) {
					Main.currentGame.setPitchSize(Integer.parseInt(value));
				}
				
				// Liest Anzahl der Schiffe pro Schiffsgrößen ein -> Game
				else if (key.equals("ships") && object == 1) {
					int[] ships = new int[4];
					ships[0] = Integer.parseInt(reader.nextLine().trim().replace(",", ""));
					ships[1] = Integer.parseInt(reader.nextLine().trim().replace(",", ""));
					ships[2] = Integer.parseInt(reader.nextLine().trim().replace(",", ""));
					ships[3] = Integer.parseInt(reader.nextLine().trim().replace(",", ""));
					
					Main.currentGame.setShips(ships);
				}
				
				// Überprüfen, ob gespeicherte Spieler mit Spieler des Spiels übereinstimmen
				else if (key.equals("player1")) {
					if (!Main.currentGame.getPlayer1().getClass().toString().equals(value.replace("\"", ""))) throw new FileNotFoundException("Players do not match!");
				}
				
				// Überprüfen, ob gespeicherte Spieler mit Spieler des Spiels übereinstimmen
				else if (key.equals("player2") ) {
					if (!Main.currentGame.getPlayer2().getClass().toString().equals(value.replace("\"", ""))) throw new FileNotFoundException("Players do not match!");
				}
				
				// Liest Wert, ob Spieler am Zug ist, ein -> Player
				else if (key.equals("isMyTurn")) {
					// -> Player1
					if (object == 2) {
						Main.currentGame.getPlayer1().setMyTurn(Boolean.parseBoolean(value));
					}
					// -> Player2
					else if (object == 3) {
						Main.currentGame.getPlayer2().setMyTurn(Boolean.parseBoolean(value));
					}
				}
				
				// Liest Anzahl der beim Gegner zerstörten Schiffe ein -> Player
				else if (key.equals("shipsDestroyed")) {
					// -> Player1
					if (object == 2) {
						Main.currentGame.getPlayer1().setShipsDestroyed(Integer.parseInt(value));
					}
					// -> Player2
					else if (object == 3) {
						Main.currentGame.getPlayer2().setShipsDestroyed(Integer.parseInt(value));
					}
				}
				
				// List Karte der bereits beschossenen Punkte ein -> Player
				else if (key.equals("pointsShot")) {
					int pitchSize = Main.currentGame.getPitchSize();
					int[][] shots = new int[pitchSize][pitchSize];
					
					// Setzt Karte zusammen
					for (int x = 0; x < pitchSize; x++) {
						reader.nextLine();
						for (int y = 0; y < pitchSize; y++) {
							shots[x][y] = Integer.parseInt(reader.nextLine().trim().replace(",", ""));
						}
						reader.nextLine();
					}
					
					// -> Player1
					if (object == 2) {
						Main.currentGame.getPlayer1().setPointsShot(shots);
					}
					// -> Player2
					else if (object == 3) {
						Main.currentGame.getPlayer2().setPointsShot(shots);
					}
				}
				
				// Liest Schiffe des Spielers ein -> Player
				else if (key.equals("ships") && object > 1) {
					// Nur wenn Spieler 2 kein Netzwerk Spieler ist und dieser gerade abgearbeitet wird. Sonst wäre Liste leer und es kommt zu fehlern
					if (object == 3 && !(Main.currentGame.getPlayer2() instanceof NetworkPlayer)) {
						int numberOfShips = Main.currentGame.getNumberOfShips(2) + Main.currentGame.getNumberOfShips(3) + Main.currentGame.getNumberOfShips(4) + Main.currentGame.getNumberOfShips(5);
						
						List<Ship> ships = new ArrayList<Ship>();
						
						for (int i = 0; i < numberOfShips; i++) {
							reader.nextLine();
													
							String[] coords = reader.nextLine().trim().split(":")[1].replace("\"", "").split(",");
							Point rootPoint = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));				// Ursprung des Schiffs
							
							int length = Integer.parseInt(reader.nextLine().trim().split(":")[1].replace(",", ""));				// Länge des Schiffs
							int orientation = Integer.parseInt(reader.nextLine().trim().split(":")[1].replace(",", ""));		// Orientierung des Schiffs
							
							reader.nextLine();
							
							int[] damage = new int[length];
							for (int j = 0; j < length; j++) {
								damage[j] = Integer.parseInt(reader.nextLine().trim().replace(",", ""));						// Beschädigungen am Schiff
							}
							
							// Erzeugt neues Schiffs-Objekt aus eingelesenen Daten
							Ship ship = new Ship(length, orientation);
							ship.setRootPoint(rootPoint);
							ship.setDamage(damage);
							
							ships.add(ship);
													
							reader.nextLine();
							reader.nextLine();
						}
						
						// -> Player1
						if (object == 2) {
							Main.currentGame.getPlayer1().setShips(ships);
						}
						// -> Player2
						else if (object == 3) {
							Main.currentGame.getPlayer2().setShips(ships);
						}
					}
					
				}
				
				// Liest Schussprioritäten für KI ein
				else if (key.equals("priorities")) {
					int pitchSize = Main.currentGame.getPitchSize();
					int[][] priorities = new int[pitchSize][pitchSize];
					
					// Setzt Karte zusammen
					for (int x = 0; x < pitchSize; x++) {
						reader.nextLine();
						for (int y = 0; y < pitchSize; y++) {
							priorities[x][y] = Integer.parseInt(reader.nextLine().trim().replace(",", ""));
						}
						reader.nextLine();
					}
					
					// -> Player1
					if (object == 2) {
						if (Main.currentGame.getPlayer1() instanceof AIPlayer) {
							AIPlayer ai = (AIPlayer) Main.currentGame.getPlayer1();
							ai.setPriorities(priorities);
						}
					}
					// -> Player2
					else if (object == 3) {
						if (Main.currentGame.getPlayer2() instanceof AIPlayer) {
							AIPlayer ai = (AIPlayer) Main.currentGame.getPlayer2();
							ai.setPriorities(priorities);
						}
					}
				}
								
			}
			
		}
		reader.close();
		
		NotificationCenter.sendNotification("GameLoaded", null);				// Dass die UI sich aktualisieren kann, wenn das Spiel geladen wurde
		
	}
	
	
	/**
	 * Erzeugung eines JSON-Strings für ein Objekt.
	 * 
	 * @param attributes Alle Attribute des Objekts.
	 * @param indentation Einrückung.
	 * @return JSON-String.
	 */
	private String writeObject(String[] attributes, String indentation) {
		String string = indentation + "{\n";
		
		for (String attribute : attributes) {
			string += indentation + "\t" + attribute + ",\n";
		}
		
		string = string.substring(0, string.length() - 2);
		string += "\n" + indentation + "}";
		return string;
	}
	
	
	/**
	 * Erzeugung eines JSON-String für ein Attribut.
	 * 
	 * @param key Attribut-Name.
	 * @param value Attribut-Wert.
	 * @param indentation Einrückung.
	 * @return JSON-String.
	 */
	private String writeAttribute(String key, Object value, String indentation) {
		if (value instanceof String) {
			return indentation + "\"" + key + "\":\"" + value + "\"";
		}
		return indentation + "\"" + key + "\":" + value;
	}
	
	
	/**
	 * Erzeugung eines JSON-Strings für ein Attribut, das ein Array als Wert hat.
	 * 
	 * @param key Attribut-Name.
	 * @param values Attribut-Wert.
	 * @param indentation Einrückung.
	 * @param isString Sollen die Elemente des Arrays als String gespeichert werden.
	 * @return JSON-String.
	 */
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
	
	
	/**
	 * Erzeugung eines JSON-Strings für ein Attribut, das ein Integer-Array als Wert hat.
	 * 
	 * @param key Attribut-Name.
	 * @param values Attribut-Wert.
	 * @param indentation Einrückung.
	 * @return JSON-String.
	 */
	private String writeArray(String key, int[] values, String indentation) {
		String string = "\"" + key + "\":[\n";
		for (int obj : values) {
			string += indentation + "\t" + obj + ",\n";
		}
		if (values.length > 0) string = string.substring(0, string.length() - 2);
		string += "\n" + indentation + "]";
		return string;
	}
	
	
	/**
	 * Erzeugung eines JSONStrings für ein Attribut, das ein zweidimensionales Array als Wert hat.
	 * 
	 * @param key Attribut-Name.
	 * @param array Attribut-Wert.
	 * @param indentation Einrückung.
	 * @return JSON-String.
	 */
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
