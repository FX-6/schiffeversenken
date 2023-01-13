package Schiffeversenken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

import Notifications.NotificationCenter;

public class Game {
	
	private int pitchSize;
	private Player player1;					// Mensch oder AI
	private Player player2;					// Netzwerk oder AI
	private int[] ships = new int[4];
	
	private int readyCount = 0;
	
	public Game(int pitchSize) {
		this.pitchSize = pitchSize;
		
		Arrays.fill(ships, 0);				// Fuellt ships mit 0 (zur Sicherheit)
	}
	
	// Getter und Setter
	public int getPitchSize() {return this.pitchSize;}
	public Player getPlayer1() {return this.player1;}
	public Player getPlayer2() {return this.player2;}
	public int getNumberOfShips(int size) {return ships[size - 2];}
	
	public void setPitchSize(int size) {this.pitchSize = size;
										player1.refreshPointsShot();
										player2.refreshPointsShot();}
	public void setShips(int[] ships) {this.ships = ships;}
	public void setPlayer1(Player player) {this.player1 = player;}
	public void setPlayer2(Player player) {this.player2 = player;}
	
	
	
	
	/**
	 * Löst das Speichern des aktuellen Spielstands aus. Wurde das Speichern nicht durch einen Befehl, der über das Netzwerk empfangen wurde, ausgelöst,
	 * so wird die Aufforderung zum Speichern auch an das Netzwerk übertragen.
	 * 
	 * @param id Eindeutige ID, die den gespeicherten Spielstand identifiziert. Zu Empfehlen ist z.B. {@link System#currentTimeMillis()}.
	 * @param name Optionaler Name, der die Identifizierbarkeit des Spielstands für den Nutzer bei der Auswahl im Ladevorgang erleichtert.
	 * @param sender Objekt, das das Speichern des Spielstands auslöst. Falls sender kein Objekt der Klasse {@link NetworkPlayer} ist, wird eine
	 * 			Aufforderung zum Spiechern über das Netzwerk übertragen, sofern ein Spiel über das Netzwerk gespielt wird.
	 * @return True, falls das Spiel gespeichert werden konnte. False, falls keine Datei mit dem Spielstand erzeugt oder die erzeugte Datei nicht 
	 * 			beschrieben werden konnte.
	 */
	public boolean save(String id, String name, Object sender) {
		// true, falls Speichern erfolgreich, andernfalls false
		
		if (!(sender instanceof NetworkPlayer) && player2 instanceof NetworkPlayer) {
			NetworkPlayer player2 = (NetworkPlayer) this.player2;
			player2.sendSave(id);
		}
		
		// Spiel speichern
		try {
			new SaveGameHandler(id, name);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	
	/**
	 * Löst das Laden eines gespeicherten Spielstands aus. Wurde das Speichern nicht durch einen Befehl, der über das Netzwerk empfangen wurde, ausgelöst,
	 * so wird eine Aufforderung zum Speichern auch über das Netzwerk übertragen.
	 * 
	 * @param id Eindeutige ID, die den gepeicherten Spielstand, der geladen werden soll, identifiziert.
	 * @param sender Objekt, das das Laden eines Spielstands auslöst. Falls sender kein Objekt der Klasse {@link NetworkPlayer} ist, wird eine
	 * 			Aufforderung zum Speichern über das Netzwerk übertragen, sofern ein Spiel über das Netzwerk gespielt wird.
	 * @return True, falls der Spielstand mit der angegebenen ID geladen werden konnt. False, falls der Spielstand nicht gefunden oder die Datei nicht 
	 * 			gelesen werden konnte.
	 */
	public boolean load(String id, Object sender) {
		// true, falls Laden erfolgreich, andernfalls false
		
		if (!(sender instanceof NetworkPlayer) && player2 instanceof NetworkPlayer) {
			NetworkPlayer player2 = (NetworkPlayer) this.player2;
			player2.sendLoad(id);
		}
		
		// ... Spielstand laden
		try {
			new SaveGameHandler(id);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Überittelt im Falle eines Spiels über das Netzwerk die Größe des Spielfelds und die Anzahl der Schiffe über das Netzwerk.
	 */
	public void transmittSizeAndShips() {
		if (player2 instanceof NetworkPlayer) {
			NetworkPlayer player2 = (NetworkPlayer) this.player2;
			if (player2.isServer) {
				player2.sendSize(this.pitchSize);
				player2.sendShips(this.ships);
			}
		}
	}
	
	
	/**
	 * Muss aufgerufen werden, um dem Spiel mitzuteilen, dass der Spieler alle verfügbaren Schiffe auf dem Spielfeld plaziert hat.
	 * 
	 * Sofern das Spiel über das Netzwerk gespielt wird und der aufrufende Spieler kein Objekt der Klasse {@link NetworkPlayer} ist, wird 
	 * der "ready"-Befehl über das Netzwerk übertragen.
	 * 
	 * Haben beide Spieler diese Methode aufgrufen, wird {@link #player1} sein erster Zug erteilt. Falls das Spiel über das Netzwerk
	 * gespielt wird und diese Instanz der Spiels nicht als Server agiert, wird {@link #player2} der erste Zug übertragen.
	 * 
	 * @param sender Spieler, der dem Spiel mitteilen möchte, dass er alle verfügbaren Schiffe gesetzt hat.
	 */
	public void setReady(Player sender) {
		// Erhöht Zähler zur Überprüfung, ob beide Spieler ihre Schiffe gesetzt haben
		// Annahme: Jeder Spieler ruft diese Funktion nur einmal auf
		readyCount++;
		
		// Wenn Spieler 1 diese Funktion aufgerufen hat und das Spiel über das Netzwerk gespielt wird, wird "ready"-Befehl übertragen
		if (sender == player1) {
			if (player2 instanceof NetworkPlayer) {
				NetworkPlayer player2 = (NetworkPlayer) getPlayer2();
				player2.sendReady();
			}
		}
		
		// Überprüfung
		if (readyCount == 2) {
			// Überprüfung, ob es sich um ein SPiel über das Netzwerk handelt
			if (player2 instanceof NetworkPlayer) {
				NetworkPlayer player2 = (NetworkPlayer) getPlayer2();
				
				// Gibt Spieler 1 den ersten Zug, falls diese Instanz des Spiels als Server agiert
				if (player2.isServer) {
					player1.pass();
				} 
				
				// Gibt Spieler 2 den ersten Zug, falls diese Instanz des Spiels als Client agiert
				else {
					player2.setMyTurn(true);
					player1.setMyTurn(false);
				}
			} 
			
			// Gibt Spieler 1 den ersten Zug, falls das Spiel lokal gespielt wird
			else {
				player1.pass();
			}
		}
	}
	
	
	
	
	
	/**
	 * Veranlasst das Beenden des Spiels.
	 * Falls das Spiel über das Netzwerk gespielt wird und das auslösende Objekt keines der Klasse {@link NetworkPlayer} ist, wird der
	 * "exit"-Befehl über das Netzwerk übermittelt.
	 * Falls das Spiel nicht durch das Schließen des Fensters beendet wird, so wird das Spielfenster geschlossen und das Menü wieder angezeigt.
	 * 
	 * @param sender Objekt, das das Beenden des Spiels veranlasst hat.
	 * @param status Grund für das Beenden des Spiels.
	 */
	public void exit(Object sender, GameExitStatus status) {
		
		// Netzwerkverbindung beenden, falls Spiel über das Netzwerk gespielt wird und der Aufruf dieser Methode nicht durch einen Befehl über das Netzwerk veranlasst wurde
		if (!(sender instanceof NetworkPlayer)) {
			if (player2 instanceof NetworkPlayer) {
				NetworkPlayer player2 = (NetworkPlayer) getPlayer2();
				player2.endConnection();
			}
		} 
		
		// Schließen des Spiel Fensters, falls der Aufruf dieser Methode nicht durch das Schließen deselben Fensters veranlasst wurde
		if (!(sender instanceof JFrame) && status != GameExitStatus.CONNECTION_REFUESED) {
			Main.menuWindow.closeGameWindow();
		}
		
		// Enterfnen aller abbonierten Notifications, damit player1 und player2 von der GarbageCollection freigegeben werden
		NotificationCenter.removeAllObservers(player1);
		NotificationCenter.removeAllObservers(player2);
		
		// Spiel beenden
		Main.currentGame = null;
		System.out.println("Spiel beendet");
	}
	
	
	
	
	
	/*
     * Berechnet die theoretisch noch mögliche Anzahl an Schiffen einer Sorte für ein quadratisches Feld mit Seitenlänge "fieldSize" unter Berücksichtigung, dass
     * maximal 30% aller Felder von Schiffen und maximal 40% dieser Felder von Schiffen derselben Sorte belegt werden dürfen.
     * Berücksichtigt nicht, dass Schiffe nicht direkt nebeneinander gesetzt werden dürfen!
     * 
     * int fieldSize -> Seitenlänge eines quadratischen Spielfelds in Feldern
     * int[] shipsInUse -> Liste der Anzahl bereits benutzter Schiffe einer Größe; Aufsteigend sortiert, beginnend mit Größe 2
     * 
     * return int[] -> Liste der Anzahl an theoretisch noch plazierbaren Schiffen einer Größe, unter der Annahme, dass alle der in Zukunft noch plazierten Schiffe
     *                 von derselben Größe sind; Aufsteigend sortiert, beginnend mit Größe 2
     */
	/*
    public static int[] getShipsLeft(int fieldSize, int[] shipsInUse) {
    	double occupacityRate = 0.0;
    	double occupacityPerShip = 0.3;												// Für 0.25 ist der Returnwert shipsLeft konstant für jede Eingabe shipsInUse
    	
    	if (fieldSize <= 8) {
    		occupacityPerShip = 0.4;
    	}
    	
    	if (fieldSize <= 10) {
    		while (occupacityRate < 0.3 || occupacityRate > 0.4) {
        		occupacityRate = Math.random();
        	}
    	} else {
    		while (occupacityRate < 0.1 || occupacityRate > 0.4) {
        		occupacityRate = Math.random();
        	}
    	}
    	
        int[] shipsLeft = new int[shipsInUse.length];
        int maxFields = (int) Math.floor(fieldSize * fieldSize * occupacityRate);    	// Anzahl an Feldern, die maximal von Schiffen belegt werden dürfen
        int fieldsUsed = 0;                                                         	// Anzahl an Feldern, die bereits belegt sind
        for (int i = 0; i < shipsInUse.length; i++) {                               	//      -"-
            fieldsUsed = fieldsUsed + shipsInUse[i]*(i+2);                          	//      -"-
        }                                                                           	//      -"-
        int fieldsFree = maxFields - fieldsUsed;                                    	// Anzahl an Feldern, die unter Berücksichtigung der 30% Obergrenze noch belegt werden dürfen
        
        for (int i = 0; i < shipsInUse.length; i++) {
            int amountShipsFields = shipsInUse[i] * (i+2);                          	// Anzahl an Feldern, die von Schiffen dieser Sorte belegt sind
            int maxAmountShipsFields = (int) Math.floor(maxFields * occupacityPerShip); // Anzahl an Feldern, die von dieser Schiffssorte maximal belegt werden dürfen

            int amountLeft = (maxAmountShipsFields - amountShipsFields) / (i+2);    	// Anzahl an Feldern, die von dieser Schiffssorte noch belegt werden dürften

            // Sucht größte Anzahl an Feldern, die von dieser Schiffssorte noch belegt werden dürfen, unter Berücksichtigung der 30% Obergrenze aller Felder
            while (amountLeft*(i+2) > fieldsFree) {
                amountLeft--;
            }

            shipsLeft[i] = amountLeft;
        }
        
        return shipsLeft;
    }
    */
    
    
    
    
    
    
    
    /**
     * Berechnet eine zufällige Zusammensetzung der Anzahlen an Schiffen für die vier verschieden zulässigen Schiffsgrößen, sodass der Belegungsgrad des Spielfelds zwischen
     * 10% und 40% liegt.
     * 
     * @param fieldSize Seitenlänge des quadratischen Spielfeldes, für welche die Konfiguration berechnet werden soll.
     * @return Array mit der Anzahl an zulässigen Schiffen für eine Schiffsgröße (= Index + 2) ergibt.
     */
    public static int[] getRandomShipConfiguration(int fieldSize) {
    	int[] ships = new int[4];					// Liste mit Anzahlen der Schiffe, welche zurückgegeben wird
    	boolean[] shipSet = new boolean[4];			// Liste mit Wahrheitswerten, ob Schiffsgröße bereits bearbeitet wurde. Index i = Schiffsgröße - 2
    	
    	// Zufällige Festlegung der Belegungsrate des Spielfeldes zwischen 10% und 40%
    	double occupacityRate = Math.random();
    	while (occupacityRate < 0.1 || occupacityRate > 0.4) {
    		occupacityRate = Math.random();
    	}
    	    	
    	// Maximal belegbare Felder bei gegebener Spielfeldgröße und Belegungsrate
    	int maxFields = (int) Math.round(fieldSize * fieldSize * occupacityRate);
    	
    	// Auswählen der Anzahl an Schiffen für eine Größe in zufälliger Reihenfolge
    	Random random = new Random();
    	for (int i = 0; i < 4; i++) {
    		// Zufälliges Auswählen einer Schiffsgröße, die noch nicht bearbeitet wurde
    		int shipSize = random.nextInt(4);
    		while (shipSet[shipSize] == true) shipSize = random.nextInt(4);
    		    		
    		shipSet[shipSize] = true;														// Merken, dass diese Schiffsgröße bearbeitet wurd/wurde 
    		
    		int fieldsPerShip = (int) Math.floor(maxFields * 0.35);							// Berechnen, wie viele Felder pro Schiffsgröße befüllt werden dürfen (! dies sind mehr als 25% für eine größere Variabilität zwischen verschieden Konfigurationen)
    		while (ships[shipSize] * (shipSize + 2) < fieldsPerShip) ships[shipSize]++;		// Maximale Anzahl an Schiffen für Schiffsgröße berechnen und abspeichern
    		    		
    		maxFields -= ships[shipSize] * (shipSize + 2);									// Noach belegbare Felder berechnen
    	}
    	
    	return ships;
    }
	
}
