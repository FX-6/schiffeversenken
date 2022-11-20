package NetzwerkKommunikation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import Notifications.NotificationCenter;

/*
 * Ein Objekt dieser Klasse stellt eine Netzwerkverbindung her. Wird der Konstruktor für den Server aufgerufen, wartet er asynchron,
 * bis sich ein Client verbunden hat, und startet dann einen NetworkListener, der auf einkommende Nachrichten reagieren kann. Außerdem wird bei
 * erfolgreicher Herstellung einer Verbindung das Event "ClientConnected" ausgelöst.
 * Wird der Konstruktor für den Client aufgerufen, verbindet sich das Objekt mit einem Server mit der übergebenen IP Adresse.
 */

public class Connection {

	private int port = 4000;			// Durch Prof. Grambow festgelegt
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private NetworkListener listener;
	
	private ServerSocket serverSocket;
	private Thread waitingThread;
		
	public Connection(String ipAddress) throws UnknownHostException, IOException {
		socket = new Socket(ipAddress, port);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Connection with " + socket.getRemoteSocketAddress() + " established.");
		NotificationCenter.sendNotification("ServerConnected", null);
		startListening();	
	}
	
	public Connection() throws IOException {
		serverSocket = new ServerSocket(port);
		System.out.println("Server started, waiting for connection ...");
		
		
		waitingThread = new Thread(new Runnable() {
			public void run() {
				try {
					socket = serverSocket.accept();
					System.out.println("Connection with " + socket.getRemoteSocketAddress() + " established.");
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintWriter(socket.getOutputStream(), true);
					startListening();
					
					NotificationCenter.sendNotification("ClientConnected", null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		waitingThread.start();
	}
	
	
	
	
	// Sendet eine Nachricht über das Netzwerk
	public void send(String message) {
		out.write(message + "\n");
		out.flush();
	}
	
	// Beendet die Netzwerkverbindung
	public void disconnect() throws IOException {
		if (listener != null) listener.stop();
		if (waitingThread != null && waitingThread.isAlive()) waitingThread.stop();
		if (socket != null && socket.isConnected()) {
			send("exit");
			socket.close();
		}
		if (serverSocket != null) serverSocket.close();
		listener = null;
		socket = null;
	}
	
	
	
	// Startet den Listener für einkommende Nachrichten
		private void startListening() throws IOException {
			listener = new NetworkListener(this.in);
			listener.start();
		}
	
}
