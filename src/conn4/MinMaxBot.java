/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conn4;

import ch.hslu.ai.connect4.Player;
import conn4.Utilities.Connect4Utilities;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author wa
 */
public class MinMaxBot extends Player {

    private int MAX_LEVEL=5;
    private int maxCols = 0, maxRows = 0, lastCol = -1;
    private Random ran = new Random();
    private Connect4Utilities utilities;
//    private char[][] currentBoard;

    public MinMaxBot(String name, char symbol) {
        super(name, symbol);

    }

    @Override
    public int play(char[][] board) {
        maxCols = board.length;
        maxRows = board[0].length;
        utilities = new Connect4Utilities(maxRows, maxCols);
        lastCol = requestMove(board.clone());
        return lastCol;
    }
    
    public char[][] getCurrentBoard(){
        return null;//this.currentBoard;
    }

    private int requestMove(char[][] board) {
        if (lastCol == -1) {
            return 3;
        }

        //currentBoard=board;
        int bestScore = Integer.MIN_VALUE;
        ArrayList<Integer> best = new ArrayList<Integer>();

        for (int x = 0; x < maxCols; ++x) {
            if (utilities.isColumnFull(board, x)) {
                continue;
            }

            utilities.move(board, x, getSymbol());

            int score;
            if (utilities.hasPlayerWon(board, getSymbol())) {
                score = 1000;
            } else {
                score = minmax(board, (getSymbol() == 'x' ? 'o' : 'x'), 1);
            }

            if (score > bestScore) {
                best.clear();
                best.add(x);
                bestScore = score;
            } else if (score == bestScore) {
                best.add(x);
            }

            utilities.unmove(board, x);
        }

        if (bestScore == -1) {
            for (int x1 = 0; x1 < maxCols; ++x1) {
                if (utilities.isColumnFull(board, x1)) {
                    continue;
                }

                utilities.move(board, x1, getSymbol());
                int won = 0;
                for (int x2 = 0; x2 < maxCols; ++x2) {
                    if (utilities.isColumnFull(board, x2)) {
                        continue;
                    }

                    ++won;

                    utilities.move(board, x2, (getSymbol() == 'x' ? 'o' : 'x'));
                    if (!utilities.hasPlayerWon(board, 'o')) {
                        --won;
                    }
                    utilities.unmove(board, x2);
                }
                utilities.unmove(board, x1);

                if (won == 0) {
                    best.clear();
                    best.add(x1);
                    break;
                }
            }
        }

        if (best.size() == 1) {
            return best.get(0);
        } else {
            return best.get(ran.nextInt(best.size()));
        }
    }

    private int minmax(char[][] board, char player, int level) {
        if (level <= MAX_LEVEL) {
            int bestScore = (player == getSymbol()) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            char[][] currentBoard = board;//.clone();
            for (int x = 0; x < maxCols; x++) {
                if (utilities.isColumnFull(board, x)) {
                    continue;
                }

                utilities.move(currentBoard, x, player);
                int score;
                // Evaluate move
                if (utilities.hasPlayerWon(currentBoard, player)) {
                    score = 100;
                } else {
                    score = minmax(currentBoard, (player == 'x' ? 'o' : 'x'), level+1);
                }

                if ((player == getSymbol() && score > bestScore)
                        || (player != getSymbol() && score < bestScore)) {
                    bestScore = score;
                }

                utilities.unmove(currentBoard, x);

                if ((player == getSymbol()  && bestScore == 100)
                        || (player != getSymbol()  && bestScore == -100)) {
                    break;
                }
            }

            if (bestScore == Integer.MIN_VALUE || bestScore == Integer.MAX_VALUE) {
                return 0;
            } else {
                return bestScore;
            }
        } else {
            return 0; // or rand?
        }
    }

}
