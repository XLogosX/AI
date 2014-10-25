package conn4;

import ch.hslu.ai.connect4.Game;
import ch.hslu.ai.connect4.Player;



/**
 * Connect-4 tournament mode.
 * 
 * @author Marc Pouly
 */

public class Tournament {
	
	private int rows, columns;
	private int nbWins1, nbWins2, draws;
	private Player player1, player2; 
	
	/**
	 * Constructor:
	 * @param rows The number of rows of the game board
	 * @param columns The number of columns of the game board
	 * @param player1 The first player
	 * @param player2 The second player
	 */
	
	public Tournament(int rows, int columns, Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		this.rows = rows;
		this.columns = columns; 
	}
	
	/**
	 * Plays the specified number of rounds and adds up the number of wins for each player.
	 * @param rounds The total number of rounds played. 
	 */
	
	public void play(int rounds) {
		
		Game game = new Game(rows, columns, player1, player2);
		
		for(int i = 0; i < rounds; i++) {
			
			int result = game.startGame();
			
			// Player 1 won the game:
			if(result == 1) {
				nbWins1++;
			}
			
			// Player 2 won the game:
			if(result == 2) {
				nbWins2++;
			}
			
			// Game is a draw:
			if(result == 0) {
				draws++;
			}
			
			game.reset();
		}
	}
	
	/**
	 * @return The number of wins of player 1.
	 */
	
	public int getWinsOfPlayer1() {
		return nbWins1;
	}
	
	/**
	 * @return The number of wins of player 2.
	 */
	
	public int getWinsOfPlayer2() {
		return nbWins2;
	}
	
	/**
	 * @return The number of draws.
	 */
	
	public int getDraws() {
		return draws;
	}

}
