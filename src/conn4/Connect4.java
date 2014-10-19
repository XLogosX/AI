package conn4;

import ch.hslu.ai.connect4.Game;
import ch.hslu.ai.connect4.GameBoard;
import ch.hslu.ai.connect4.Player;

/**
 * Main class with connect-4 single game and tournament mode.
 * 
 * @author Marc Pouly
 */

public class Connect4 {
	
	/**
	 * Game board dimensions:
	 */
	
	private static final int ROWS = 6;
	private static final int COLUMNS = 7;
	
	/**
	 * Main method:
	 */
	
	public static void main(String[] args) {
		
		// Create players:
		Player player1 = new RandomPlayer("Captain Awesome", 'x');
		Player player2 = new UberBotXtream("Ultra GangstA Fighter X", 'o');
		
		// Start a single game with GUI:
		singleGameMode(player1, player2);
		
		// Start a tournament with many rounds:
		//tournamentMode(player1, player2, 10);
	}
	
	/**
	 * Starts a single game with GUI
	 * @param player1 The first mover
	 * @param player2 The second mover
	 */
	
	public static void singleGameMode(Player player1, Player player2) {
		Game game = new Game(ROWS, COLUMNS, player1, player2);
		GameBoard board = new GameBoard(game, 500, player1, player2);
		board.startGame();
	}
	
	/**
	 * Start blind tournament mode with many rounds
	 * @param player1 The first mover
	 * @param player2 The second mover
	 * @param rounds The number of rounds to be played
	 */
	
	public static void tournamentMode(Player player1, Player player2, int rounds) {
		int counter1 = 0, counter2 = 0;
		Game game = new Game(ROWS, COLUMNS, player1, player2);
		for(int i = 0; i < rounds; i++) {
			int result = game.startGame();
			// Player 1 won the game:
			if(result == 1) {
				counter1++;
			}
			// Player 2 won the game:
			if(result == 2) {
				counter2++;
			}
			// Game is a draw:
			if(result == 0) {
				// do nothing
			}
			game.reset();
		}
		System.out.println(player1.getName()+" won "+counter1+" times.");
		System.out.println(player2.getName()+" won "+counter2+" times.");
	}
}
