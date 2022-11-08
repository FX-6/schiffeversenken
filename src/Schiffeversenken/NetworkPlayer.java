package Schiffeversenken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkPlayer extends Player {

	public final boolean isServer;
   public final int portNumber = 1337;
   private Socket socket;
   private PrintWriter out;
   private BufferedReader in;

	public NetworkPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);
      try (ServerSocket serverSocket = new ServerSocket(this.portNumber)) {
         this.socket = serverSocket.accept();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
		this.isServer = true;
      try {
         this.out = new PrintWriter(socket.getOutputStream(), true);
         this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } catch (IOException e) {
         e.printStackTrace();
      }
	}

	public NetworkPlayer(Game game, Player otherPlayer, String ipAddress) {
		super(game, otherPlayer);
		this.isServer = false;
      try {
         this.socket = new Socket(ipAddress, this.portNumber);
         this.out = new PrintWriter(socket.getOutputStream(), true);
         this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
	}

	// Anpassung an die Netzwerkstruktur
	@Override
	protected int shot(Point point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void pass() {
		// Teile über das Netzwerk mit, dass anderer Spieler dran ist und warte auf Antwort
	}





	public void sendReady() {
		// Wenn Schiffsanzahl oder Feldgröße empfangen und gespeichert wurden
	}

	public void sendOK() {
		// Wenn Spiel Speichern oder Laden erfolgreich war
	}

	public void sendExit() {
		// Wenn spiel Laden nicht erfolgreich war oder Spiel verlassen werden soll
	}



	// Netzwerkspezifisch als Server
	public boolean sendSize(int pitchSize) {
		// true, falls "done" als Antwork kam, andernfalls false
		return false;
	}

	public boolean sendShips(int[] ships) {
		// true, falls "done" als Antwork kam, andernfalls false
		return false;
	}

	public boolean sendSave(String id) {
		// true, falls "ok" als Antwork kam, andernfalls false
		return false;
	}

	public boolean sendLoad(String id) {
		// true, falls "ok" als Antwort kam, andernfalls false
		return false;
	}
}
