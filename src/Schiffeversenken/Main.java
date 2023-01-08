package Schiffeversenken;

import UserInterface.Menu;

public class Main {

	public static Game currentGame = null;
	public static String hostAddress = null;

	public static Menu menuWindow = null;

	public static void main(String[] args) {

      // creates game folder and settings file
		SettingsHandler.initSettings();

		menuWindow = new Menu();

		
		
		

//		createGame(10, GameType.NETWORK_SERVER);
//
//		int[] ships = new int[4];
//		Arrays.fill(ships, 0);
//		ships[2] = 1;
//		currentGame.setShips(ships);

		/*
		System.out.println("1.\tcreate [server | client <ipAddress>] pitchSize \t//if Client then just any number for pitchSize");
		System.out.println("2.\tships <size 2> <size 3> <size 4> <size 5> \t//if Server");
		System.out.println("3.\tstart \t//if Server");
		System.out.println("4.\t[place <length> <orientation> | get] <x> <y>");
		System.out.println("5.\tready");
		System.out.println("6.\tshoot <x> <y>");
		System.out.println("7.\tquit");

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();

		// Create a new Game
		while(!input.equals("create")) {
			input = scanner.next();
		}

		while(!input.equals("server") && !input.equals("client")) {
			input = scanner.next();
		}

		if (input.equals("server")) {
			createGame(scanner.nextInt(), GameType.NETWORK_SERVER);

			// Set Ships if server
			while(!input.equals("ships") && hostAddress == null) {
				input = scanner.next();
			}

			int[] ships = new int[4];
			ships[0] = scanner.nextInt();
			ships[1] = scanner.nextInt();
			ships[2] = scanner.nextInt();
			ships[3] = scanner.nextInt();

			currentGame.setShips(ships);

			while(!input.equals("start")) {
				input = scanner.next();
			}

			currentGame.transmittSizeAndShips();
		}
		else if (input.equals("client")) {
			hostAddress = scanner.next();
			createGame(scanner.nextInt(), GameType.NETWORK_CLIENT);
		}


		while (!input.equals("ready")) {
			// place <length> <orientation> <x> <y>	--> Plaziert Schiff
			if (input.equals("place")) {
				System.out.println(currentGame.getPlayer1().placeShipAt(new Ship(scanner.nextInt(), scanner.nextInt()), new Point(scanner.nextInt(), scanner.nextInt())));
			}

			// get <x> <y> --> Gibt Schiff zurueck
			if (input.equals("get")) {
				System.out.println(currentGame.getPlayer1().getShipAt(new Point(scanner.nextInt(), scanner.nextInt())));
			}

			input = scanner.next();
		}

		currentGame.transmittReady();


		while (!input.equals("quit") && currentGame != null) {

			// shoot <x> <y>	--> Schiesst auf gegnerisches Schiff
			if (input.equals("shoot")) {
				int result = currentGame.getPlayer1().shoot(new Point(scanner.nextInt(), scanner.nextInt()));
				if (result == 0) currentGame.getPlayer1().otherPlayer.pass();
			}

			// get <x> <y> --> Gibt Schiff zurueck
			if (input.equals("get")) {
				System.out.println(currentGame.getPlayer1().getShipAt(new Point(scanner.nextInt(), scanner.nextInt())));
			}

			input = scanner.next();
		}

		currentGame.exit();
		*/

	}



	public static void createGame(int pitchSize, GameType type) {
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

		// Wird benötigt, da beim erfolglosen Verbindungsaufbau im Netzwerkspiel das Spiel in anderem Thread bereits beendet sein könnte (und damit currentGame = null ist)
		if (currentGame != null) {
			currentGame.setPlayer1(player1);
			currentGame.setPlayer2(player2);
		}
	}

}
