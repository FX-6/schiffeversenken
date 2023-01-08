package Schiffeversenken;

public class AIPlayer extends Player {

	public AIPlayer(Game game, Player otherPlayer) {
		super(game, otherPlayer);
	}

	@Override
	public void pass() {
		// Teile der KI mit, dass sie einen weitereun Zug ausüben darf
		otherPlayer.setMyTurn(false);
		setMyTurn(true);
	}

}



/* Schiffe dürfen sich nicht berühren -> Felder neben Schiffen prio 0
 * Schiffe sind mindestens 2 Felder lang und in einer Linie -> sobald 2 benachbarte Teile bekannt sind ist entweder:
 * 			  an einem Ende answer 0 -> an anderem Ende prio max
 * 				  für answer 1: offenes Ende prio max
 * 				  für answer 2: Schiff zerstört
 * 		oder: an einem Ende answer 1 -> auf dieses Ende prio max
 * 				  für answer 0: auf anderes Ende prio max
 * 					für answer 1: offenes Ende prio max
 * 					für answer 2: Schiff zerstört
 * 				  für answer 2: Schiff zerstört
 * Schiffanzahl pro Länge bekannt -> Lücken die kleiner als größtes noch vorhandenes Schiff sind -> Felder prio 0
 * 
 * Bei answer 0: direkte Nachbarfelder: prio norm -10
 * 				 diagonale Nachbarfelder: prio norm -5
 * 
 * Schuss immer auf höchste prio
 * 
 */