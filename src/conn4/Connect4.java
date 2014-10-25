package conn4;


import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

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
		Player[] players = {
			new AlphaBetaBot("Captain Awesome", 'x'),
			new RandomPlayer("Chuck Norris",    'o'),
			new RandomPlayer("Sheldon Cooper",  'z'),
			new RandomPlayer("Homer Simpson",   'w'),
		};
		
		// Start a single game with GUI:	
		singleGameMode(players[0], players[1]);
		
		// Start a tournament with many rounds:
		//tournamentMode(players, 1000);
	}
	
	/**
	 * Starts a single game with GUI. Remember that Connect-4 has a first mouver advantage. 
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
	 * One round consists of two matches such that each player has the first move in one match
	 * This is important because Connect-4 has a first mover advantage. 
	 * @param players All players participating in the tournament
	 * @param rounds The number of rounds (= 2 * number of matches) to be played
	 */
	
	public static void tournamentMode(Player[] players, int rounds) {
		
		HashMap<Player, Integer> ranking = new HashMap<Player, Integer>();
		for(Player p : players) {
			ranking.put(p, 0);
		}
		
		/*
		 * Start Tournament:
		 */
		
		System.out.println("***********************************************");
		System.out.println("* Tournament started ...");
		System.out.println("***********************************************\n");
		
		for(int i = 0; i < players.length; i++) {
			for(int j = 0; j < players.length; j++) {
	
				if(i != j) {
					// Let Player i play Player j
					Tournament tournament = new Tournament(ROWS, COLUMNS, players[i], players[j]);
					tournament.play(rounds);
					
					// Add number of wins of player 1
					int v1 = ranking.get(players[i]) + tournament.getWinsOfPlayer1();
					ranking.put(players[i], v1);
					
					// Add number of wins of player 2
					int v2 = ranking.get(players[j]) + tournament.getWinsOfPlayer2();
					ranking.put(players[j], v2);				
					
					System.out.println(players[i].getName()+" vs. "+players[j].getName()+": "
							+tournament.getWinsOfPlayer1()+" - "
							+tournament.getWinsOfPlayer2()+" (draws: "+tournament.getDraws()+")");
										
				}
			}
		}
		
		System.out.println();
		System.out.println("***********************************************");
		System.out.println("* Final Ranking ...");
		System.out.println("***********************************************\n");
		
		/*
		 * Produce Ranking:
		 */
		
		TreeSet<Integer> points = new TreeSet<Integer>(ranking.values()); 
		Iterator<Integer> iter = points.descendingIterator();
		
		int rank = 1;
		
		while(iter.hasNext()) {
			
			int p = iter.next();
			
			System.out.println("Rank "+rank+" with "+p+" points:");
			for(Player player : ranking.keySet()) {
				if(ranking.get(player) == p) {
					System.out.println("\t- "+player.getName());
				}
			}
			
			System.out.println();
			
			rank++;
		}
		
	}
}
