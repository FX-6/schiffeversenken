package Schiffeversenken;

/**
 * Zusammensetzung der beiden Spieler, die gegeneinander spielen sollen.
 */
public enum GameType {
	/**
	 * Multiplayer über das Netzwerk als menschlicher Spieler. Diese Instanz des Spiels agiert als Server.
	 */
	NETWORK_SERVER,
	
	/**
	 * Multiplayer über das Netzwerk als menschlicher Spieler. Diese Instanz des Spiels agiert als Client.
	 */
	NETWORK_CLIENT,
	
	/**
	 * Singleplayer gegen den Computer.
	 */
	AI,
	
	/**
	 * Computer spielt über das Netzwerk. Diese Instanz des Spiels agiert als Server.
	 */
	NETWORK_AI_SERVER,
	
	/**
	 * Computer spielt über das Netzwerk. Diese Instanz des Spiels agiert als Client.
	 */
	NETWORK_AI_CLIENT
}
