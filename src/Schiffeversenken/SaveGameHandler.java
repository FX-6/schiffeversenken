package Schiffeversenken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Timestamp;
import java.util.Collection;

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
	public SaveGameHandler(String name) {
		String fileName = System.currentTimeMillis() + "." + name + ".json";
		
		file = new File(SettingsHandler.saveGamesPath + File.separator + fileName);
		
		
		
		
		
		// Testing
		String[] array = {"eins", "zwei", "drei"};
		int[] array2 = {10, 5, 9765, 28368};
				
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(writeAttribute("test0", writer.toString(), "\t"));
			writer.write(writeArray("test1", array, ""));
			writer.write(writeArray("test2", array2, "\t"));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	// Um ein gespeichertes Spiel zu laden
	public SaveGameHandler(Timestamp id) {
		
	}
	
	
	
	private String writeAttribute(String key, Object value, String indentation) {
		if (value instanceof String) {
			return indentation + "\"" + key + "\":\"" + value + "\"";
		}
		return indentation + "\"" + key + "\":" + value;
	}
	
	
	private String writeArray(String key, Object[] values, String indentation) {
		String string = indentation + "\"" + key + "\":[\n";
		if (values instanceof String[]) {
			for (Object obj : values) {
				string += indentation + "\t\"" + obj + "\",\n";
			}
		}
		else {
			for (Object obj : values) {
				string += indentation + "\t" + obj + ",\n";
			}
		}
		string = string.substring(0, string.length() - 2);
		string += "\n" + indentation + "]";
		return string;
	}
	
	private String writeArray(String key, int[] values, String indentation) {
		String string = indentation + "\"" + key + "\":[\n";
		for (Object obj : values) {
			string += indentation + "\t" + obj + ",\n";
		}
		string = string.substring(0, string.length() - 2);
		string += "\n" + indentation + "]";
		return string;
	}
	
}
