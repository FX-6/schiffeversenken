package Schiffeversenken;

public class Main {

	public static Game currentGame = null;
	public static String hostAddress = null;
	
	public static void main(String[] args) {
		
	}
	
	
	
	public static void createGame(GameType type, int pitchSize) {
		Player player1 = null;
		Player player2 = null;
		currentGame = new Game(pitchSize);
		
		switch (type) {
		case NETWORK_SERVER:
			player1 = new HumanPlayer(currentGame, null);
			player2 = new NetworkPlayer(currentGame, player1);
			player1.setOtherPlayer(player2);
			break;
		case NETWORK_CLIENT:
			player1 = new HumanPlayer(currentGame, null);
			player2 = new NetworkPlayer(currentGame, player1, hostAddress);
			player1.setOtherPlayer(player2);
			break;
		case AI:
			player1 = new HumanPlayer(currentGame, null);
			player2 = new AIPlayer(currentGame, player1);
			player1.setOtherPlayer(player2);
			break;
		case NETWORK_AI_SERVER:
			player1 = new AIPlayer(currentGame, null);
			player2 = new NetworkPlayer(currentGame, player1);
			player2.setOtherPlayer(player2);
			break;
		case NETWORK_AI_CLIENT:
			player1 = new AIPlayer(currentGame, null);
			player2 = new NetworkPlayer(currentGame, player1, hostAddress);
			player2.setOtherPlayer(player2);
			break;
		}
		
		currentGame.setPlayer1(player1);
		currentGame.setPlayer2(player2);
	}
	
}
