package NetzwerkKommunikation;

import java.io.BufferedReader;
import java.io.IOException;

import Notifications.NotificationCenter;
import Schiffeversenken.GameExitStatus;
import Schiffeversenken.Main;


/*
 * Ein Objekt dieser Klasse wartet kontinuierlich auf eine Nachricht aus dem Netzwerk (asynchron zum Rest des Programms).
 * Um das PingPong-Prinzip einzuhalten muss also die UI das Senden weiterer Nachrichten unterbinden!
 * 
 * Wenn eine Nachricht eingetroffen ist, wird das Event "NewNetworkMessage" ausgelöst und alle Objekte, die dieses Event abboniert 
 * haben, bekommen die Nachricht übermittelt (Siehe auch Notifications.NotificationCenter).
 */

public class NetworkListener extends Thread {
	
	private BufferedReader in;
		
	public NetworkListener(BufferedReader in) {
		super("Listener");
		this.in = in;
	}
	
	public void run() {
		while(true) {
			String message = "";
			try {
				message = "" + in.readLine();
			} catch (IOException e) {
				if (Main.currentGame != null) Main.currentGame.exit(Main.currentGame.getPlayer2(), GameExitStatus.CONNECTION_CLOSED);
				//e.printStackTrace();
				break;
			}
			
			NotificationCenter.sendNotification("NewNetworkMessage", message);			// Loest Event "NewNetworkMessageEvent" aus (Neue Nachricht über das Netzwerk empfangen)
			
			if (message.equals("null")) {
				break;
			}
		}
	}

}
