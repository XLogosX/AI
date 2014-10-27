package conn4;

import ch.hslu.ai.connect4.Player;
import conn4.Heuristics.ConnectedCountScore;
import conn4.Heuristics.IHeuristic;
import conn4.Utilities.Connect4Utilities;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author wa
 */
public class AlphaBetaBot extends Player {

    Random ran = new Random();
    int maxDepth = 3;

    private int maxCols, maxRows;
    private Connect4Utilities utilities;
    private int lastCol = -1;
    private int ALPHA = Integer.MIN_VALUE;
    private int BETA = Integer.MAX_VALUE;

    public AlphaBetaBot(String name, char symbol) {
        super(name, symbol);
    }

    @Override
    public int play(char[][] board) {

        maxCols = board.length;
        maxRows = board[0].length;
        utilities = new Connect4Utilities(maxRows, maxCols);
        lastCol = requestMove(board);

        return lastCol;
    }

    private int requestMove(char[][] board) {
        if (lastCol == -1) {
            return 3;
        }

        int bestScore = Integer.MIN_VALUE;
        int score = 0;

        ArrayList<Integer> bestMoves = new ArrayList();

        ArrayList<String> Symbols = new ArrayList<>();
        Symbols.add("x");
        Symbols.add("o");
        for (String symbol : Symbols) {
            // test first move for direct win.
            for (int col = 0; col < maxCols; col++) {
                if (utilities.isColumnFull(board, col)) {
                    continue;
                }
                
                // do move
                utilities.move(board, col, symbol.charAt(0));
                // check if player has Won else use minmax
                if (utilities.hasPlayerWon(board, symbol.charAt(0))) {
                    utilities.unmove(board, col);
                    return col;
                }
                // undo move
                utilities.unmove(board, col);
            }
        }

        for (int col = 0; col < maxCols; col++) {
            if (utilities.isColumnFull(board, col)) {
                continue;
            }

            // do move
            utilities.move(board, col, getSymbol());
            // check if player has Won else use minmax          
            score = alphabeta(board, 1, ALPHA, BETA, getSymbol() == 'x' ? 'o' : 'x', false);
            // undo move
            utilities.unmove(board, col);

            // evaluate score
            if (score > bestScore) {
                bestMoves.clear();
                bestMoves.add(col);
                bestScore = score;
            } else if (score == bestScore) {
                bestMoves.add(col);
            }
        }

        // return best move or random if there are more than 1
        if (bestMoves.size() == 1) {
            return bestMoves.get(0);
        } else if (bestMoves.size() > 0) {
            return bestMoves.get(ran.nextInt(bestMoves.size()));
        } else {
            RuntimeException runtimeException = new RuntimeException("WTFFFFFF=======???????");
            throw runtimeException;
        }
    }

    public int alphabeta(char[][] board, int depth, int alpha, int beta, char currentPlayer, boolean maximize) {
        if (depth == 0 || depth > maxDepth) {
            return new ConnectedCountScore().evaluate(board, currentPlayer).score;
        }

        ArrayList<Integer> bestScores = new ArrayList();

        if (maximize) {
            for (int i = 0; i < maxCols; i++) {

                if (utilities.isColumnFull(board, i)) {
                    continue;
                }

                utilities.move(board, i, currentPlayer);
                int curAlpha = alphabeta(board, depth + 1, alpha, beta, currentPlayer == 'x' ? 'o' : 'x', !maximize);
                utilities.unmove(board, i);
                // pruning
                if (beta < curAlpha) {
                    continue;
                }

                if (curAlpha > alpha) {
                    alpha = curAlpha;
                    bestScores.clear();;
                    bestScores.add(curAlpha);
                }
            }
        } else {
            for (int i = 0; i < maxCols; i++) {
                if (utilities.isColumnFull(board, i)) {
                    continue;
                }

                utilities.move(board, i, currentPlayer);
                int curBeta = alphabeta(board, depth + 1, alpha, beta, currentPlayer == 'x' ? 'o' : 'x', !maximize);
                utilities.unmove(board, i);
                // pruning
                if (curBeta < alpha) {
                    continue;
                }

                if (curBeta < beta) {
                    beta = curBeta;
                    bestScores.clear();;
                    bestScores.add(curBeta);
                }
            }
        }

        if (bestScores.size() == 1) {
            return bestScores.get(0);
        } else if (bestScores.size() > 0) {
            return bestScores.get(ran.nextInt(bestScores.size()));
        } else {
            return ran.nextInt(6);
        }
    }
}
