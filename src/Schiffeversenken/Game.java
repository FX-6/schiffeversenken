package Schiffeversenken;

public class Game {
	
	
	
	
	
	
	
	
	
	
	
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
    public int[] getShipsLeft(int fieldSize, int[] shipsInUse) {
    	double occupacityRate = 0.3;
    	double occupacityPerShip = 0.4;													// Für 0.25 ist der Returnwert shipsLeft konstant für jede Eingabe shipsInUse
    	
        int[] shipsLeft = new int[shipsInUse.length];
        int maxFields = (int) Math.ceil(fieldSize * fieldSize * occupacityRate);    	// Anzahl an Feldern, die maximal von Schiffen belegt werden dürfen (30% aller Felder)
        int fieldsUsed = 0;                                                         	// Anzahl an Feldern, die bereits belegt sind
        for (int i = 0; i < shipsInUse.length; i++) {                               	//      -"-
            fieldsUsed = fieldsUsed + shipsInUse[i]*(i+2);                          	//      -"-
        }                                                                           	//      -"-
        int fieldsFree = maxFields - fieldsUsed;                                    	// Anzahl an Feldern, die unter Berücksichtigung der 30% Obergrenze noch belegt werden dürfen

        for (int i = 0; i < shipsInUse.length; i++) {
            int amountShipsFields = shipsInUse[i] * (i+2);                          	// Anzahl an Feldern, die von Schiffen dieser Sorte belegt sind
            int maxAmountShipsFields = (int) Math.floor(maxFields * occupacityPerShip); // Anzahl an Feldern, die von dieser Schiffssorte maximal belegt werden dürfen (40% der 30% aller Felder)

            int amountLeft = (maxAmountShipsFields - amountShipsFields) / (i+2);    	// Anzahl an Feldern, die von dieser Schiffssorte noch belegt werden dürften (ohne 30% Obergrenze aller Felder)

            // Sucht größte Anzahl an Feldern, die von dieser Schiffssorte noch belegt werden dürfen, unter Berücksichtigung der 30% Obergrenze aller Felder
            while (amountLeft*(i+2) > fieldsFree) {
                amountLeft--;
            }

            shipsLeft[i] = amountLeft;
        }
        
        return shipsLeft;
    }
	
}
