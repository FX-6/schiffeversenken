package Schiffeversenken;

/**
 * Grund für das Beenden eines Spiels.
 */
public enum GameExitStatus {
	/**
	 * Es konnte keine Verbindung hergestellt werden.
	 */
	CONNECTION_REFUESED,
	/**
	 * Die Verbindung wurde unterbrochen.
	 */
	CONNECTION_CLOSED,
	/**
	 * Das Spiel ist zu Ende gespielt.
	 */
	GAME_FINISHED,
	/**
	 * Das Spiel wurde abgebrochen.
	 */
	GAME_DISCARDED,
	/**
	 * Der Spielstand konnte nicht gefunden werden.
	 */
	FILE_NOT_FOUND;
}
