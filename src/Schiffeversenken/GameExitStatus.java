package Schiffeversenken;

public enum GameExitStatus {
	CONNECTION_REFUESED,	// Es konnte keine Verbindung hergestellt werden
	CONNECTION_CLOSED,		// Die Verbindung wurde unterbrochen
	GAME_FINISHED,			// Das Spiel ist beendet
	GAME_DISCARDED,			// Das Spiel wurde abgebrochen
	FILE_NOT_FOUND;			// Der Spielstand konnte nicht gefunden wurden
}
