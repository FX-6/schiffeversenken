package Schiffeversenken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFrame;

import Notifications.Notification;
import Notifications.NotificationCenter;

public class Game implements Notification {
	
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
	
	
	
	
	// Speichert einen Spielstand
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
	
	// Lädt einen Spielstand
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
	
	// Überträgt im Falle eines Netzwerkspiels als Server die Feldgroesse und die Anzahl an Schiffen fuer eine Groesse
	public void transmittSizeAndShips() {
		if (player2 instanceof NetworkPlayer) {
			NetworkPlayer player2 = (NetworkPlayer) this.player2;
			player2.sendSize(this.pitchSize);
			player2.sendShips(this.ships);
		}
	}
	
	// Teilt über das Netzwerk mit, dass alle Schiffe gesetzt wurden und das Spiel beginnen kann
	public void transmittReady() {
		if (player2 instanceof NetworkPlayer) {
			NetworkPlayer player2 = (NetworkPlayer) this.player2;
			player2.sendReady();
		}
	}
	
	
	
	// Teilt dem Spiel mit, dass der Spieler seine Schiffe gesetzt hat
	// Wird von Player aufgerufen (oder dem Interface)
	public void setReady(Player sender) {
		// Überprüft, ob beide Spieler ready sind und sendet ggf. an das Netzwerk
		readyCount++;
		
		if (sender == player1) {
			if (player2 instanceof NetworkPlayer) {
				NetworkPlayer player2 = (NetworkPlayer) getPlayer2();
				player2.sendReady();
			}
		}
		
		if (readyCount == 2) {
			
			// Gibt dem ersten Spieler den Zug
			if (player2 instanceof NetworkPlayer) {
				NetworkPlayer player2 = (NetworkPlayer) getPlayer2();
				if (player2.isServer) {
					player1.pass();
				} else {
					player2.setMyTurn(true);
					player1.setMyTurn(false);
				}
			} else {
				player1.pass();
			}
		}
	}
	
	
	
	
	
	// Beendet ein Spiel
	public void exit(Object sender, GameExitStatus status) {
		
		// Spiel beenden ...
		if (!(sender instanceof NetworkPlayer)) {
			if (player2 instanceof NetworkPlayer) {
				NetworkPlayer player2 = (NetworkPlayer) getPlayer2();
				player2.endConnection();
			}
		} 
		if (!(sender instanceof JFrame) && status != GameExitStatus.CONNECTION_REFUESED) {
			Main.menuWindow.closeGameWindow();
		}
		
		NotificationCenter.removeAllObservers(player1);
		NotificationCenter.removeAllObservers(player2);
		
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
    
    
    
    
    
    
    
    @Override
	public void processNotification(String type, Object object) {
		if (type.equals("ClientConnected")) {

		}
	}
	
}
