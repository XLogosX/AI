/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    int bestScore = 0;
    private int maxCols;
    private IHeuristic heuristicEvaluator;
    private Connect4Utilities utilities;
    private int lastCol = -1;
    private int ALPHA = Integer.MIN_VALUE;
    private int BETA = Integer.MAX_VALUE;

    public AlphaBetaBot(String name, char symbol) {
        super(name, symbol);
        heuristicEvaluator = new ConnectedCountScore();
    }

    @Override
    public int play(char[][] board) {

        maxCols = board.length;
        int maxRows = board[0].length;

        utilities = new Connect4Utilities(maxRows, maxCols);
        //int col = alphabeta(board, maxDepth, maxDepth, maxDepth, currentPlayer, true)
        lastCol = requestMove(board);

        return lastCol;
    }

    private int requestMove(char[][] board) {
        if (lastCol == -1) {
            return 3;
        }
        ArrayList<Integer> bestMoves = new ArrayList();

        //int ALPHA = Integer.MIN_VALUE;
        //int BETA = Integer.MAX_VALUE;
        int score = 0;

        for (int col = 0; col < maxCols; col++) {

            if (utilities.isColumnFull(board, col)) {
                continue;
            }

            // do move
            utilities.move(board, col, getSymbol());
            // check if player has Won else use minmax
            if (utilities.hasPlayerWon(board, getSymbol())) {
                return col;
            } else {
                score = alphabeta(board, 1, ALPHA, BETA, getSymbol() == 'x' ? 'o' : 'x', false);
            }
            // undo move
            utilities.unmove(board, col);

            // evaluate score
            if (score > bestScore) {
                bestMoves.clear();
                bestMoves.add(col);
            } else if (score == bestScore) {
                bestMoves.add(col);
            }

        }

        // return best move or random if there are more than 1
        if (bestMoves.size() == 1) {
            return bestMoves.get(0);
        } else {
            return bestMoves.get(ran.nextInt(bestMoves.size()));
        }
    }

    public int alphabeta(char[][] board, int depth, int alpha, int beta, char currentPlayer, boolean maximize) {
        if (depth == 0 || depth > maxDepth) {
            return new ConnectedCountScore().evaluate(board, currentPlayer).score;
        }

        ArrayList<Integer> bestMoves = new ArrayList();

        if (maximize) {
            for (int i = 0; i < maxCols; i++) {

                if (utilities.isColumnFull(board, i)) {
                    continue;
                }

                utilities.move(board, i, currentPlayer);
                int curAlpha = alphabeta(board, depth + 1, alpha, beta, currentPlayer == 'x' ? 'o' : 'x', !maximize);
                //heuristicEvaluator.evaluate(board, currentPlayer).score;

                utilities.unmove(board, i);

                // tested <
                if (beta < curAlpha) {
                    continue;
                }

                if (curAlpha > alpha) {
                    alpha = curAlpha;
                    bestMoves.clear();;
                    bestMoves.add(curAlpha);
//                    bestMoves.add(i);
                }/*else if(curAlpha==alpha){
                 bestMoves.add(i);
                 }                */

            }
        } else {
            for (int i = 0; i < maxCols; i++) {
                utilities.move(board, i, currentPlayer);
                int curBeta = alphabeta(board, depth + 1, alpha, beta, currentPlayer == 'x' ? 'o' : 'x', !maximize);
                //heuristicEvaluator.evaluate(board, currentPlayer).score;
                utilities.unmove(board, i);

                // tested >
                if (curBeta < alpha) {
                    continue;
                }

                if (curBeta < beta) {
                    beta = curBeta;
                    bestMoves.clear();;
                    bestMoves.add(curBeta);
                    // bestMoves.add(i);
                }/*else if(curBeta==beta){
                 bestMoves.add(i);
                 }                */

            }
        }

        if (bestMoves.size() == 1) {
            return bestMoves.get(0);
        } else if(bestMoves.size()>0) {
            return bestMoves.get(ran.nextInt(bestMoves.size()));
        }else{
            return ran.nextInt(6);
        }
    }

}
