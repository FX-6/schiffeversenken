package Schiffeversenken;

import java.util.Arrays;

public abstract class Player {

	protected Game game;
	protected Player otherPlayer;
	protected Ship[] ships;
	protected int[][] pointsShot;
	
	public Player(Game game, Player otherPlayer) {
		this.game = game;
		this.otherPlayer = otherPlayer;
		this.pointsShot = new int[game.getPitchSize()][game.getPitchSize()];
		this.ships = new Ship[game.getNumberOfShips(2) + game.getNumberOfShips(3) + game.getNumberOfShips(4) + game.getNumberOfShips(5)];
		
		for (int[] array : pointsShot) {
			Arrays.fill(array, 0);
		}
	}
	
	public abstract int shot(Point point);
	
}
