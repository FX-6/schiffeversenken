package Schiffeversenken;

import UserInterface.Menu;

public class Main {

	/**
	 * Speichert das aktuelle Spiel ("null", falls keins gespielt oder vorbereitet wird) 
	 */
	public static Game currentGame = null;
	
	/**
	 * Speicherung der IP-Adresse des Servers, falls ein Spiel über das Netzwerk gespielt werden soll, in welchem diese Instanz des Spiels als Client agieren wird
	 */
	public static String hostAddress = null;

	
	/**
	 * Speicherung des Menü-Fensters
	 */
	public static Menu menuWindow = null;

	public static void main(String[] args) {

		// Erstellt Datei mit Einstellungen und lädt benötigte Assets herunter
		// HINWEIS: Beim ersten Start des Spiels wird folglich eine aktive Verbindung zum Internet benötigt!
		SettingsHandler.initSettings();

		// Erzeugt das Menü-Fenster, über welches alles gesteuert wird
		menuWindow = new Menu();
	}


	/**
	 * Kreiert eine neue Instanz eines Spiels mit allen benötigten Klassen.
	 * 
	 * @param pitchSize Seitenlänge in Feldern des Spielfelds für das Spiel.
	 * @param type Zusammensetzung der beiden Spieler, die gegeneinander spielen sollen.
	 */
	public static void createGame(int pitchSize, GameType type) {
		Player player1 = null;
		Player player2 = null;
		currentGame = new Game(pitchSize);

		switch (type) {
		case NETWORK_SERVER:
			player1 = new HumanPlayer(currentGame, null);
			player2 = new NetworkPlayer(currentGame, player1);
			player1.setOtherPlayer(player2);
			break;
		case NETWORK_CLIENT:
			player1 = new HumanPlayer(currentGame, null);
			player2 = new NetworkPlayer(currentGame, player1, hostAddress);
			player1.setOtherPlayer(player2);
			break;
		case AI:
			player1 = new HumanPlayer(currentGame, null);
			player2 = new AIPlayer(currentGame, player1);
			player1.setOtherPlayer(player2);
			break;
		case NETWORK_AI_SERVER:
			player1 = new AIPlayer(currentGame, null);
			player2 = new NetworkPlayer(currentGame, player1);
			player1.setOtherPlayer(player2);
			break;
		case NETWORK_AI_CLIENT:
			player1 = new AIPlayer(currentGame, null);
			player2 = new NetworkPlayer(currentGame, player1, hostAddress);
			player1.setOtherPlayer(player2);
			break;
		}

		// Wird benötigt, da beim erfolglosen Verbindungsaufbau im Netzwerkspiel das Spiel in anderem Thread bereits beendet sein könnte (und damit currentGame = null ist)
		if (currentGame != null) {
			currentGame.setPlayer1(player1);
			currentGame.setPlayer2(player2);
		}
	}

}
