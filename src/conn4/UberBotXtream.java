/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conn4;

import ch.hslu.ai.connect4.Player;
import conn4.Heuristics.ConnectedCountScore;
import conn4.Heuristics.EvalScoreInfos;
import conn4.Heuristics.IHeuristic;
import conn4.Utilities.Connect4Utilities;
import java.util.List;

/**
 *
 * @author wa
 */
public class UberBotXtream extends Player {

    private boolean xWinFound;
    private boolean oWinFound;
    private int maxDepth, boardsAnalyzed = 0, maxCols, maxRows, column;
    ;
    private char[][] board;

    private Connect4Utilities utilities;
    private IHeuristic evaluator;

    public UberBotXtream(String name, char symbol) {
        super(name, symbol);
        maxDepth = 5;
        evaluator=new ConnectedCountScore();
    }

    @Override
    public int play(char[][] board) {
        int col = -1;
        this.board = board;
        maxCols = board.length;
        maxRows = board[0].length;
        System.out.println("Open");
        utilities = new Connect4Utilities(maxRows, maxCols);
        utilities.printBoard(board);
        do {
            //column = (int) (Math.random() * board.length);
            col = alphaBeta(getSymbol());
        } while (board[col][0] != '-');

        System.out.println("Close");
        utilities = new Connect4Utilities(maxRows, maxCols);
        return col;
    }

    private int alphaBeta(char player) {
        oWinFound = xWinFound = false;
        //int column = 0;
        if (player == 'x') {
            // check for next move win
            evaluateXMove(0, 1, -1, Integer.MIN_VALUE + 1,
                    Integer.MAX_VALUE - 1);
            if (xWinFound) {
                return column;
            }
            
            // check for opponent next move win
            oWinFound = xWinFound = false;
            evaluateOMove(0, 1, -1, Integer.MIN_VALUE + 1,
                    Integer.MAX_VALUE - 1);
            if (oWinFound) {
                return column;
            }
            
            // check all moves
            evaluateXMove(0, maxDepth, -1, Integer.MIN_VALUE + 1,
                    Integer.MAX_VALUE - 1);
        } else {
            evaluateOMove(0, 1, -1, Integer.MIN_VALUE + 1,
                    Integer.MAX_VALUE - 1);
            if (oWinFound) {
                return column;
            }
            oWinFound = xWinFound = false;
            evaluateXMove(0, 1, -1, Integer.MIN_VALUE + 1,
                    Integer.MAX_VALUE - 1);
            if (xWinFound) {
                return column;
            }
            evaluateOMove(0, maxDepth, -1, Integer.MIN_VALUE + 1,
                    Integer.MAX_VALUE - 1);
        }
        return column;
    }

    private int evaluateOMove(int depth, int maxDepth, int col, int alpha, int beta) {
        boardsAnalyzed++;
        int min = Integer.MAX_VALUE, score = 0;
        //utilities.printBoard(board);
        if (col != -1) {
            EvalScoreInfos scoreInfo = evaluator.evaluate(board, 'x');//board.getHeuristicScore('x', col, depth, maxDepth);
            score=scoreInfo.score;
            if (scoreInfo.hasXWin) {
                xWinFound = true;
                return score;
            }
        }
        if (depth == maxDepth) {
            return score;
        }

        List<Integer> moves = utilities.GetValidMoves(board);

        for (int c : moves) {
            //utilities.printBoard(board);
            utilities.move(board, c, 'o');
            int value = evaluateXMove(depth + 1, maxDepth, c, alpha, beta);
            utilities.unmove(board, c);
          //  utilities.printBoard(board);
            if (value < min) {
                min = value;
                if (depth == 0) {
                    column = c;
                }
            }
            if (value < beta) {
                beta = value;
            }
            if (alpha >= beta) {
                return beta;
            }
        }

        if (min == Integer.MAX_VALUE) {
            return 0;
        }
        return min;
    }

    private int evaluateXMove(int depth, int maxDepth, int col, int alpha, int beta) {
        boardsAnalyzed++;
        int max = Integer.MIN_VALUE, score = 0;
        utilities.printBoard(board);
        if (col != -1) {
            EvalScoreInfos scoreInfo  = evaluator.evaluate(board, 'o');;//board.getHeuristicScore(Board.MARK_RED, col, depth, maxDepth);
            score=scoreInfo.score;
            if (scoreInfo.hasOWin) {
                oWinFound = true;
                return score;
            }
        }
        if (depth == maxDepth) {
            return score;
        }
        List<Integer> moves = utilities.GetValidMoves(board);

        for (int c : moves) {
            utilities.printBoard(board);
            utilities.move(board, c, 'x');
            int value = evaluateXMove(depth + 1, maxDepth, c, alpha, beta);
            utilities.unmove(board, c);
            utilities.printBoard(board);

            System.out.println("EvalVal: "+value+ " max: "+max+" alpha: "+alpha+" beta: "+beta);
            if (value > max) {
                max = value;
                if (depth == 0) {
                    column = c;
                }
            }
            if (value > alpha) {
                alpha = value;
            }
            if (alpha >= beta) {
                return alpha;
            }
        }
        if (max == Integer.MIN_VALUE) {
            return 0;
        }
        return max;
    }

    private boolean checkForXWin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean checkForOWin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
