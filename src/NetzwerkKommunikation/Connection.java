package NetzwerkKommunikation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import Notifications.NotificationCenter;

/**
 * Ein Objekt dieser Klasse stellt eine Netzwerkverbindung her. Wird der Konstruktor für den Server aufgerufen, wartet er asynchron,
 * bis sich ein Client verbunden hat, und startet dann einen {@link NetworkListener}, der auf einkommende Nachrichten reagieren kann. Außerdem wird bei
 * erfolgreicher Herstellung einer Verbindung das Event "ClientConnected" ausgelöst.
 * Wird der Konstruktor für den Client aufgerufen, verbindet sich das Objekt mit einem Server mit der übergebenen IP Adresse.
 */

public class Connection {

	/**
	 * Port, des Servers. 
	 */
	private int port = 50000;			// Durch Prof. Grambow festgelegt
	
	/**
	 * Socket der Verbindung.
	 */
	private Socket socket;
	
	/**
	 * OutputStream der Netzwerkverbindung.
	 */
	private PrintWriter out;
	
	/**
	 * InputStream der Netzwerkverbindung.
	 */
	private BufferedReader in;
	
	/**
	 * Listener, der auf einkommende Nachrichten wartet.
	 */
	private NetworkListener listener;
	
	
	/**
	 * Socket, der den Server bereitstellt.
	 */
	private ServerSocket serverSocket;
	
	/**
	 * Thread, der asynchron auf den Verbindungsaufbau mit einem Client wartet.
	 */
	private Thread waitingThread;
	
	
	
	
	/**
	 * Verbindet sich als Client zu einem Server.
	 * 
	 * @param ipAddress IP-Adresse des Servers.
	 * @throws UnknownHostException Es konnte kein Server unter angegebener IP-Adresse auf Port {@link #port} gefunden werden.
	 * @throws IOException Socket konnte nicht mit Input- und OutputStream verbunden werden.
	 */
	public Connection(String ipAddress) throws UnknownHostException, IOException {
		System.out.println("[NET] Try to connect to " + ipAddress);
		socket = new Socket(ipAddress, port);																	// Stellt eine Verbindung zum Server her
		out = new PrintWriter(socket.getOutputStream(), true);													// Speichert den OutputStream der Verbindung
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));								// Speichert den InputStream der Verbindung
		System.out.println("[NET] Connection with " + socket.getRemoteSocketAddress() + " established.");
		NotificationCenter.sendNotification("ServerConnected", null);											// Sendet Event, dass der Client sich zu einem Server verbunden hat (Wichtig für UI)
		startListening();																						// Startet den Listener für einkommende Nachrichten
	}
	
	
	/**
	 * Hostet einen Server, zu welchem sich auf Port {@link #port} verbunden werden kann.
	 * 
	 * @throws IOException Hosten des Servers schlgt fehl.
	 */
	public Connection() throws IOException {
		serverSocket = new ServerSocket(port);																	// Hostet den Server
		System.out.println("[NET] Server started, waiting for connection ...");
		
		
		// Wartet asynchron auf den Verbindungsaufbau zu einem Client
		waitingThread = new Thread(new Runnable() {
			public void run() {
				try {
					socket = serverSocket.accept();																			// Bestätigt die Verbindung zum Client, sobald dieser versucht eine solche aufzubauen
					System.out.println("[NET] Connection with " + socket.getRemoteSocketAddress() + " established.");
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));								// Speichert den InputStream der Verbindung
					out = new PrintWriter(socket.getOutputStream(), true);													// Speichert den OutputStream der Verbindung
					startListening();																						// Startet den Listener für einkommende Nachrichten
					
					NotificationCenter.sendNotification("ClientConnected", null);											// Sendet Event, dass sich ein Client mit dem Server verbunden hat (Wichtig für UI)
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "ConnectionWaiter");
		waitingThread.start();																					// Startet das Warten auf einen Client
	}
	
	
	
	
	/**
	 * Sendet eine Nachricht über das Netzwerk.
	 * 
	 * @param message Nachricht, die versendet werden soll.
	 */
	public void send(String message) {
		System.out.println("[NET] " + message);
		out.write(message + "\n");
		out.flush();
	}
	
	/**
	 * Beendet die alle Netzwerkaktivitäten.
	 * 
	 * @throws IOException Verbindung kann nicht beendet werden (da sie bereits beendet ist)
	 */
	public void disconnect() throws IOException {
		if (waitingThread != null && waitingThread.isAlive()) waitingThread.stop();		// Falls Server noch auf eine Verbindung wartet, wird dies gestoppt
		if (socket != null && socket.isConnected()) {
			socket.close();																// Verbindung beenden, falls es eine gibt
		}
		if (serverSocket != null) serverSocket.close();									// Server stoppen, falls dies der Server ist
		listener = null;																// Listener beenden
		socket = null;																	// Socket schließen
	}
	
	
	
	/**
	 * Startet den Listener für einkommende Nachrichten.
	 */
	private void startListening() {
		listener = new NetworkListener(this.in);		// Neuen Thread mit Listener erzeugen
		listener.start();								// Thread mit Listener starten
	}
	
}
