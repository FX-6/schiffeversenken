package Schiffeversenken;

import java.io.IOException;
import java.util.Arrays;

import NetzwerkKommunikation.Connection;
import Notifications.Notification;
import Notifications.NotificationCenter;

public class NetworkPlayer extends Player implements Notification  {

	public final boolean isServer;
	private Connection connection;
	
	private boolean isPingPong;
	private String queMessage;						// Warteschlange der Position 1 für Netzwerk-Nachrichten, wenn gerade nicht gesendet werden darf (isPingPong = false)
	
	private String message = ""; 					// Wird gebraucht, um empfangene Nachrichten an Methoden weiterzuleiten, die auf eine Nachricht warten
	
	
	
	// Wenn Netzwerkspiel erstellt wird (Server)
	public NetworkPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);
		this.isPingPong = true;
		this.isServer = true;
		try {
			connection = new Connection();										
			NotificationCenter.addObserver("NewNetworkMessage", this);				// Abboniert das Event "NeweNetworkMessage" (Wird aufgerufen, wenn eine Nachricht über das Netzwerk eintrifft)	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Wenn Netzwerkspiel beigetreten werden soll (Client)
	public NetworkPlayer(Game game, Player otherPlayer, String ipAddress) {
		super(game, otherPlayer);
		this.isPingPong = false;
		this.isServer = false;
		try {
			connection = new Connection(ipAddress);
			NotificationCenter.addObserver("NewNetworkMessage", this);				// Abboniert das Event "NeweNetworkMessage" (Wird aufgerufen, wenn eine Nachricht über das Netzwerk eintrifft)
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Connection to " + ipAddress + " failed.");
			NotificationCenter.sendNotification("ConnectionFailed", null);
			game.exit(this, GameExitStatus.CONNECTION_REFUESED);
		}
 	}

	// Anpassung an die Netzwerkstruktur
	@Override
	protected int shot(Point point) {
		send("shot " + point.x + " " + point.y);
		waitForAnswer();
		
		String[] args = message.split(" ");
		if (args[0].equalsIgnoreCase("answer")) return Integer.parseInt(args[1]);
		else return -1;
	}

	// Teilt dem Netzwerk mit, dass es an der Reihe ist
	@Override
	public void pass() {
		otherPlayer.setMyTurn(false);
		setMyTurn(true);
		send("pass");
	}
	
	// Beendet die Netzwerkverbindung
	public void endConnection() {
		try {
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	// Zum Aufruf aus game
	
	public void sendSave(String id) {
		send("save " + id);
		waitForAnswer();
		// ...
	}
	
	public void sendLoad(String id) {
		send("load " + id);
		waitForAnswer();
		// ...
	}
	
	public void sendSize(int pitchSize) {
		send("size " + pitchSize);
		waitForAnswer();
	}
	
	public void sendShips(int[] ships) {
		String string = "ships";
		for (int i = 5; i >= 2; i--) {
			for (int j = game.getNumberOfShips(i); j > 0; j--) {
				string += " " + i;
			}
		}
		send(string);
		waitForAnswer();
	}
	
	public void sendReady() {
		send("ready");
	}
	
	
	
	
	
	
	// Threading (Wird benoetigt, wenn auf Antworten von Netzwerk gewartet werden muss, um mit ihnen weiter zu rechnen)
	
	private void waitForAnswer() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void continueWithAnswer() {
		synchronized (this) {
			this.notify();
		}
	}
	
	
	
	
	// Nachricht ans Netzwerk senden
	private void send(String message) {
		queMessage = message;
		sendQue();
	}
	
	// Sendet die Warteschlange mit Überprüfung des PingPong-Prinzips
	private void sendQue() {
		if (isPingPong) {
			connection.send(queMessage);
			queMessage = "";
			isPingPong = false;
		}
	}
	
	// Sendet ohne Überprüfung des PingPong-Prinzips und ohne Beachtung der Warteschlange
	private void sendFirst(String message) {
		connection.send(message);
		isPingPong = false;
	}
	
	
	
	
	// Wenn eine Netzwerknachricht eingetroffen ist
	@Override
	public void processNotification(String type, Object object) {
		if (type.equals("NewNetworkMessage")) {
			isPingPong = true;
			
			String message = (String) object;
			
			System.out.println("\t\t-> " + message);
			
			String[] args = message.split(" ");
			this.message = message;
			String cmd = args[0];
			
			switch (cmd) {
			case "save":
				NotificationCenter.sendNotification("SaveGame", args[1]);			// Pop-Up Fenster umd Spielstandspeicherung einen Namen zu geben	
				sendFirst("ok");
				break;
				
			case "load":
				if (game.load(args[1], this)) {
					sendFirst("ok");
				} else {
					game.exit(this, GameExitStatus.FILE_NOT_FOUND);
				}
				break;
				
			case "size":
				int pitchSize = Integer.parseInt(args[1]);
				game.setPitchSize(pitchSize);
				this.updatePointsShot();
				otherPlayer.updatePointsShot();
				sendFirst("done");
				break;
				
			case "ships":
				int[] ships = new int[4];
				Arrays.fill(ships, 0);
				for (int i = 1; i < args.length; i++) {
					int size = Integer.parseInt(args[i]);
					ships[size-2]++;
				}
				game.setShips(ships);
				sendFirst("done");
				break;
				
			case "shot":
				int x = Integer.parseInt(args[1]);
				int y = Integer.parseInt(args[2]);
				int result = this.shoot(new Point(x, y));
				sendFirst("answer "+ result);
				break;
				
			case "exit":
				try {
					connection.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				game.exit(this, GameExitStatus.CONNECTION_CLOSED);
				break;
				
			case "null":
				try {
					connection.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				game.exit(this, GameExitStatus.CONNECTION_CLOSED);
				break;
				
			case "pass":
				otherPlayer.pass();
				break;
				
			case "answer":
				continueWithAnswer();
				break;
				
			case "ok":
				continueWithAnswer();
				break;
			
			case "done":
				continueWithAnswer();
				break;
				
			case "ready":
				continueWithAnswer();
				game.setReady(this);
				break;
			}
			
			if (!queMessage.equals("")) {
				sendQue();
			}
		}
	}

}
