package Schiffeversenken;

import Notifications.NotificationCenter;

/**
 * Diese Klasse repräsentiert einen menschlichen Spieler, der als Platzhalter für die Eingaben durch das UserInterface in der Klasse {@link Game} genutzt wird.
 */
public class HumanPlayer extends Player {

	/**
	 * Erstellt eine Instanz des Spielers, der durch den Benutzer gesteuert werden kann.
	 * 
	 * @param game Spiel, zu welchem dieser Spieler gehört.
	 * @param otherPlayer Gegner dieses Spielers im Spiel (muss ggf. mit "null" gefüllt werden und später gesetzt werden, falls noch keine Instanz des Gegners existiert).
	 */
	public HumanPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);
	}

	@Override
	public void pass() {
		otherPlayer.setMyTurn(false);
		setMyTurn(true);
		NotificationCenter.sendNotification("UITurn", null);		// Teile der UI mit, dass Spieler dran ist und lasse ihn aktionen ausfuehren!!
	}

}
