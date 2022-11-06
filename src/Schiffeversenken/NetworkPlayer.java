package Schiffeversenken;

public class NetworkPlayer extends Player {

	public final boolean isServer;
	
	public NetworkPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);
		this.isServer = true;
	}
	
	public NetworkPlayer(Game game, Player otherPlayer, String ipAddress) {
		super(game, otherPlayer);
		this.isServer = false;
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
