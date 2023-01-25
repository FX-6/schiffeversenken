package Schiffeversenken;

import java.io.IOException;
import java.util.Arrays;

import NetzwerkKommunikation.Connection;
import Notifications.Notification;
import Notifications.NotificationCenter;


/**
 * Stellt eine Instanz eines Spielers mit einer Verbindung zum Netzwerk dar. Hier sind alle Methoden untergebracht, die vom Spiel benötigt werden, um über das Netzwerk
 * mit einem anderen Spiel kommunizieren zu können.
 */
public class NetworkPlayer extends Player implements Notification  {

	/**
	 * Speichert, ob dieses Spiel als Server agiert.
	 */
	public final boolean isServer;
	
	/**
	 * Speichert die Netzwerkverbindung
	 */
	private Connection connection;
	
	/**
	 * Speichert, ob dieser Spieler gerade eine Nachricht über das Netzwerk senden darf (-> PingPong Prinzip)
	 */
	private boolean isPingPong;
	
	/**
	 * Speichert die zuletzt angeforderte Nachricht, die über das Netzwerk versendet werden soll,
	 * aufgrund des PingPong Prinzips jedoch nicht versendet werden darf.
	 */
	private String queMessage = "";						// Warteschlange der Position 1 für Netzwerk-Nachrichten, wenn gerade nicht gesendet werden darf (isPingPong = false)
	
	/**
	 * Übergibt die über das Netzwerk empfangene Nachricht an {@link #shoot(Point)}, um mit der Nachricht weiterarbeiten zu können.
	 */
	private String message = ""; 					// Wird gebraucht, um empfangene Nachrichten an Methoden weiterzuleiten, die auf eine Nachricht warten
	
	
	
	
	
	/**
	 * Erstellt eine neuen Instanz eines Spielers mit einer Verbindung zum Netzwerk, welche als Server agiert. Der Server ist ab Aufruf dieses Konstruktors unter dem Port 4000
	 * erreichbar. Das Spiel wird beendet (Siehe {@link Game#exit(Object, GameExitStatus)}), falls das Hosten des Servers fehlschlägt.
	 * 
	 * @param game Spiel, zu welchem dieser Spieler gehören soll.
	 * @param otherPlayer Gegner dieses Spielers (ggf. "null", falls der Gegner noch nicht initialisiert wurde).
	 */
	public NetworkPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);
		this.isPingPong = true;														// Da dieses Spiel der Server ist, darf er als Erstes eine Nachricht senden
		this.isServer = true;
		try {
			connection = new Connection();											// Öffnet den Server und wartet auf eine Verbindung
			NotificationCenter.addObserver("NewNetworkMessage", this);				// Abboniert das Event "NeweNetworkMessage" (Wird aufgerufen, wenn eine Nachricht über das Netzwerk eintrifft)	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Erstellt eine neue Instanz eines Spielers mit einer Verbindung zum Netzwerk, welche als Client agiert. Der Verbindungsaufbau startet mit dem Aufruf dieses Konstruktors auf Port 4000.
	 * Das Spiel wird beendet (Siehe {@link Game#exit(Object, GameExitStatus)}), falls der Verbindungsaufbau fehlschlägt.
	 * 
	 * @param game Spiel, zu welchem dieser Spieler gehören soll.
	 * @param otherPlayer Gegner dieses Spielers (ggf. "null", falls der Gegner noch nicht initialisiert wurde).
	 * @param ipAddress IP-Adresse des Servers.
	 */
	public NetworkPlayer(Game game, Player otherPlayer, String ipAddress) {
		super(game, otherPlayer);
		this.isPingPong = false;													// Da dieses Spiel als Client agiert, darf es zu Beginn keine Nachricht üner das Netzwerk senden
		this.isServer = false;
		try {
			connection = new Connection(ipAddress);
			NotificationCenter.addObserver("NewNetworkMessage", this);				// Abboniert das Event "NeweNetworkMessage" (Wird aufgerufen, wenn eine Nachricht über das Netzwerk eintrifft)
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Connection to " + ipAddress + " failed.");
			NotificationCenter.sendNotification("ConnectionFailed", null);			// Nachricht an das UI, dass der Verbindungsaufbau fehlgeschlagen ist
			game.exit(this, GameExitStatus.CONNECTION_REFUESED);
		}
 	}
	

	@Override
	protected int shot(Point point) {
		send("shot " + point.y + " " + point.x);									// Sende an das Netzwerk
		waitForAnswer();															// Warte, bis das Netzwerk eine Antwort auf diese Anfrage schickt.
		
		String[] args = message.split(" ");											// Vearrbeite die Antwort
		if (args[0].equalsIgnoreCase("answer")) return Integer.parseInt(args[1]);
		else return -1;
	}

	/**
	 * Teilt dem Netzwerk mit, dass es an der Reihe ist.
	 */
	@Override
	public void pass() {
		otherPlayer.setMyTurn(false);
		setMyTurn(true);
		send("pass");
	}
	
	
	
	/**
	 * Beendet die Netzwerkverbindung.
	 */
	public void endConnection() {
		try {
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	/**
	 * Sendet die Aufforderung zum Speichern des Spiels an das Netzwerk.
	 * 
	 * @param id Eindeute ID, welche diesen Spielstand identifizieren soll.
	 */
	public void sendSave(String id) {
		send("save " + id);
		waitForAnswer();
		// ...
	}
	
	/**
	 * Sendet die Aufforderung zum Laden eines Spiels an das Netzwerk.
	 * 
	 * @param id Eindeutige ID, welche den Spielstand, der geladen werden soll, identifiziert.
	 */
	public void sendLoad(String id) {
		send("load " + id);
		waitForAnswer();
		// ...
	}
	
	/**
	 * Sendet die Spielfeldgröße an das Netzwerk.
	 * 
	 * @param pitchSize Größe des Spielfelds.
	 */
	public void sendSize(int pitchSize) {
		send("size " + pitchSize);
		waitForAnswer();
	}
	
	/**
	 * Sendet die Anzahl an Schiffen für die Schiffsgrößen an das Netzwerk.
	 * 
	 * @param ships Anzahl an Schiffen für die Schiffsgrößen. Index = Schiffsgröße - 2.
	 */
	public void sendShips(int[] ships) {
		String string = "ships";
		// Umbau der Speicherung von "[Anzahl Größe 2] [ Anzahl Größe 3] [Anzahl Größe 4] [Anzahl Größe 5]" zu "Größe2 Größe2 ... Größe3 Größe3 ... Größe4 Größe4 ... Größe5 Größe5 ..."
		for (int i = 5; i >= 2; i--) {
			for (int j = game.getNumberOfShips(i); j > 0; j--) {
				string += " " + i;
			}
		}
		send(string);
		waitForAnswer();
	}
	
	/**
	 * Teilt dem Netzwerk mit, dass {@link Game#getPlayer1()} alle seine Schiffe gesetzt hat.
	 */
	public void sendReady() {
		send("ready");
	}
	
	
	
	
	
	
	// Threading (Wird benoetigt, wenn auf Antworten von Netzwerk gewartet werden muss, um mit ihnen weiter zu rechnen)
	
	/**
	 * Hält den aufrufenden Thread an, damit auf eine Nachricht, die in einem anderen Thread empfangen wird, gewartet werden kann, da diese zur Weiterverarbeitung benötigt wird.
	 */
	private void waitForAnswer() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Setzt den durch {@link #waitForAnswer()} angehaltenen Thread fort.
	 */
	private void continueWithAnswer() {
		synchronized (this) {
			this.notify();
		}
	}
	
	
	
	
	/**
	 * Setzt eine Nachricht auf die Warteschlange {@link #queMessage}, damit sie als nächstes gesendet wird.
	 * 
	 * @param message Nachricht, die als nächstes gesendet werden soll.
	 */
	private void send(String message) {
		queMessage = message;
		sendQue();
	}
	
	/**
	 * Sendet die Nachricht, die in der Warteschlange {@link #queMessage} steht, an das Netzwerk, falls diese Instanz des
	 * Spiels gerade eine Nachricht senden darf.
	 */
	private void sendQue() {
		if (isPingPong) {
			connection.send(queMessage);
			queMessage = "";
			isPingPong = false;
		}
	}
	
	/**
	 * Sendet eine Nachricht an das Netzwerk, ohne die Warteschlange {@link #queMessage} zu berücksichtigen.
	 * Dies ist hilfreich für Antworten an das Netzwerk wie z.B. die Befehle "answer", "ok", "done", etc.
	 * 
	 * @param message Nachricht, welche an das Netzwerk gesendet werden soll.
	 */
	private void sendFirst(String message) {
		connection.send(message);
		isPingPong = false;
	}
	
	
	
	
	/**
	 * Verarbeitet eine Nachricht, die über das Netzwerk empfangen wurde.
	 * 
	 * @param object Nachricht, welche empfangen wurde.
	 */
	@Override
	public void processNotification(String type, Object object) {
		if (type.equals("NewNetworkMessage")) {
			isPingPong = true;														// Da eine Nachricht empfangen wurde, darf nun wieder eine Nachricht gesendet werden
			
			String message = (String) object;
			
			System.out.println("[NET]\t\t-> " + message);
			
			// Befehl aus der Nachricht lesen (erstes Argument)
			String[] args = message.split(" ");
			this.message = message;
			String cmd = args[0];
			
			// Befehl verarbeiten
			switch (cmd) {
			case "save":														// save <id>
				NotificationCenter.sendNotification("SaveGame", args[1]);			// Pop-Up Fenster umd Spielstandspeicherung einen Namen zu geben	
				sendFirst("ok");
				break;
				
			case "load":														// load <id>
				// Wenn Spiel-Laden erfolgreich war
				if (game.load(args[1], this)) {
					sendFirst("ok");
				} else {
					game.exit(this, GameExitStatus.FILE_NOT_FOUND);
				}
				break;
				
			case "size":														// size <pitchSize>
				int pitchSize = Integer.parseInt(args[1]);
				game.setPitchSize(pitchSize);
				sendFirst("done");
				NotificationCenter.sendNotification("ReceivedGameData", null);
				break;
				
			case "ships":														// ships <ships ...>
				int[] ships = new int[4];
				Arrays.fill(ships, 0);
				// Nachricht auf lokale Speicherung übersetzen
				for (int i = 1; i < args.length; i++) {
					int size = Integer.parseInt(args[i]);
					ships[size-2]++;
				}
				game.setShips(ships);
				sendFirst("done");
				NotificationCenter.sendNotification("ReceivedGameData", null);
				break;
				
			case "shot":														// shot <row | y> <col | x>
				int x = Integer.parseInt(args[2]);
				int y = Integer.parseInt(args[1]);
				int result = this.shoot(new Point(x, y));
				sendFirst("answer "+ result);
				break;
				
			case "exit":														// exit
				try {
					connection.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				game.exit(this, GameExitStatus.CONNECTION_CLOSED);
				return;
				
			case "null":														// null -> Verbindung beendet
				try {
					connection.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				game.exit(this, GameExitStatus.CONNECTION_CLOSED);
				return;
				
			case "pass":														// pass
				otherPlayer.pass();
				break;
				
			case "answer":														// answer <damage>
				continueWithAnswer();
				break;
				
			case "ok":															// ok -> Spiel gespeichert/geladen
				continueWithAnswer();
				break;
			
			case "done":														// done -> Spielfeldgröße oder Schiffsgrößen
				continueWithAnswer();
				break;
				
			case "ready":														// ready -> Alle Schiffe plaziert
				continueWithAnswer();
				game.setReady(this);
				break;
			}
			
			// Que leeren, falls nicht bereits leer
			if (!queMessage.equals("")) {
				sendQue();
			}
		}
	}

}
