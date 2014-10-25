package conn4;

import ch.hslu.ai.connect4.Player;


/**
 * A random computer player for illustration how to implement the Player interface.
 * Also, you may use this player to test your own implementation.
 * 
 * @author Marc Pouly
 */

public class RandomPlayer extends Player {
	
	/**
	 * Constructor:
	 * @name name The name of this computer player
	 * @param symbol The symbol representing this player
	 */
	
	public RandomPlayer(String name, char symbol) {
		super(name, symbol);
	}
	
    /**
     * The following method allows you to implement your own game intelligence.
     * At the moment, this is a dumb random number generator.
     * The method must return the column number where the computer player puts the next disc. 
     * board[i][j] = cell content at position (i,j), i = column, j = row
     * 
     * If board[i][j] = this.getSymbol(), the cell contains one of your discs
     * If board[i][j] = '-', the cell is empty
     * Otherwise, the cell contains one of your opponent's discs
     * @param board The current game board
     * @return The columns number where you want to put your disc
     */

	@Override
    public int play(char[][] board) {
    	int column = -1;	
    	do {
    		column = (int)(Math.random() * board.length);
    	} while(board[column][0] != '-');
    	return column;
    }
}
