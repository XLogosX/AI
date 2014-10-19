/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package conn4;

import ch.hslu.ai.connect4.Player;
import conn4.Utilities.Connect4Utilities;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Simon
 */
public class OverBot extends Player {

    private final int ALPHA__MAX_VALUE;
    private final int BETA__MIN_VALUE;

    private char opponent;
    private boolean xWinFound, oWinFound;
    private int maxCols, maxRows;

    private Connect4Utilities utilities;

    /**
     * Constructor.
     *
     * @param name name The name of this computer player
     * @param symbol The symbol representing this player
     */
    public OverBot(final String name, final char symbol) {
        super(name, symbol);
        ALPHA__MAX_VALUE = Integer.MIN_VALUE;//Integer.MAX_VALUE;
        BETA__MIN_VALUE = Integer.MAX_VALUE;//Integer.MIN_VALUE;
        xWinFound = oWinFound = false;
        // set opponent symbol
        opponent = symbol == 'x' ? 'o' : 'x';
    }

    /**
     * The following method allows you to implement your own game intelligence.
     * At the moment, this is a dumb random number generator. The method must
     * return the column number where the computer player puts the next disc.
     * board[i][j] = cell content at position (i,j), i = column, j = row
     *
     * If board[i][j] = this.getSymbol(), the cell contains one of your discs If
     * board[i][j] = '-', the cell is empty Otherwise, the cell contains one of
     * your opponent's discs
     *
     * @param board The current game board
     * @return The columns number where you want to put your disc
     */
    @Override
    public int play(char[][] board) {
        maxCols = board.length;
        maxRows = board[0].length;
        utilities = new Connect4Utilities(maxRows, maxCols);
        int column = -1;
        do {
            //column = (int) (Math.random() * board.length);
            column = minmaxAlphaBeta(board, 5, ALPHA__MAX_VALUE, BETA__MIN_VALUE, true);
        } while (board[column][0] != '-');

        return column;
    }

    private int minmaxAlphaBeta(char[][] board, int depth, int parentAlpha, int parentBeta, boolean maximizePlayer) {
        // check for valid depth
        if (depth == 0) {
            //When depth == 0 return heuristic/eval of board        
            int moveEvaluation = evaluateMove(board);
            return moveEvaluation;
        }
        char[][] curBoard = board.clone();

        List<Integer> moves = utilities.GetValidMoves(curBoard);
        //ToDo: Calculate each valid move in seperate thread!

        if (maximizePlayer) {
            //Loop through all available moves and choose alpha
            for (int i : moves) {
                utilities.printBoard(board);
                curBoard = utilities.move(curBoard, i, getSymbol());
                utilities.printBoard(board);
                int moveAlpha = minmaxAlphaBeta(curBoard, depth - 1, parentAlpha, parentBeta, false);
                if (moveAlpha > parentAlpha) {
                    curBoard = utilities.unmove(curBoard, i);
                    return moveAlpha;
                }
                if (moveAlpha > parentAlpha) {
                    parentAlpha = moveAlpha;
                }
                curBoard = utilities.unmove(curBoard, i);

            }
            return parentAlpha;
        } else {
            //Loop through all available moves and choose alpha
            for (int i : moves) {
                utilities.printBoard(curBoard);
                char opponentSymbol = getSymbol() == 'x' ? 'o' : 'x';
                curBoard = utilities.move(curBoard, i, opponentSymbol);
                utilities.printBoard(curBoard);
                int moveBeta = minmaxAlphaBeta(curBoard, depth - 1, parentAlpha, parentBeta, true);
                if (moveBeta < parentBeta) {
                    curBoard = utilities.unmove(curBoard, i);
                    return moveBeta;
                }
                if (moveBeta < parentBeta) {
                    parentBeta = moveBeta;
                }
                curBoard = utilities.unmove(curBoard, i);

            }
            return parentBeta;
        }
    }

    private int evaluateMove(char[][] board) {

        Situation curSit = checkForXinARow(board);

        int evalValue = 0;

        evalValue += curSit.count2inArowX * 50 * (getSymbol() == 'x' ? 1 : -1);
        evalValue += curSit.count2inArowO * 50 * (getSymbol() == 'o' ? 1 : -1);

        evalValue += curSit.count3InARowX * 75 * (getSymbol() == 'x' ? 1 : -1);
        evalValue += curSit.count3InARowO * 75 * (getSymbol() == 'o' ? 1 : -1);

        evalValue += curSit.count3InARowX_Special * 99 * (getSymbol() == 'x' ? 1 : -1);
        evalValue += curSit.count3InARowO_Special * 99 * (getSymbol() == 'x' ? 1 : -1);

        return evalValue;
        // ToDo: Check for xWin oWin or 2 in a row 3 in a row!
        /*
         myWin=+100 (4in a Row horizontal, diagonal, vertical)!
         my 3 in a row S = +99 (-xxx-)
         my 3 in a Row = +75 (xx-x,-xxx-,x-xx)
         my 2 in a row = +50 (xx,x x)
         1and0=+0
         op 2 in a row = -50 (xx,x x)
         op 3 in a Row = -75 (xx x,xxx,x xx)
         myLoss=-100 (4in a Row horizontal, diagonal, vertical)!        
         */
        //return (int) (Math.random() * board.length);
    }

    private Situation checkForXinARow(char[][] board) {
        Situation sit = new Situation();
        int count = 0;

        StringBuilder sbuilder = new StringBuilder();
        // horizontal check
        for (int i = 0; i < maxCols; i++) {
            sbuilder = new StringBuilder();
            for (int j = 0; j < maxRows; j++) {
                sbuilder.append(board[i][j]);
            }
            checkPatterns(sbuilder.toString(), sit);
        }

        sbuilder = new StringBuilder();
        // vertical check
        for (int row = 0; row < maxRows; row++) {
            sbuilder = new StringBuilder();
            for (int col = 0; col < maxCols; col++) {
                sbuilder.append(board[col][row]);
            }
            checkPatterns(sbuilder.toString(), sit);
        }

        return sit;
    }

    private void checkPatterns(String rowColDiag, Situation sit) {
        List<String> twoInARowPatterns = new ArrayList<String>();
        twoInARowPatterns.add("-xx-");
        twoInARowPatterns.add("-x-x");
        twoInARowPatterns.add("x-x-");

        List<String> threeInARowPatterns = new ArrayList<String>();
        twoInARowPatterns.add("-xxx");
        twoInARowPatterns.add("xxx-");
        twoInARowPatterns.add("xx-x");
        twoInARowPatterns.add("x-xx");

        for (String pat : twoInARowPatterns) {
            for (int i = 0; i < 2; i++) {
                Pattern p;
                if (i != 0) {
                    p = Pattern.compile(pat.replace('x', 'o'));
                } else {
                    p = Pattern.compile(pat);
                }

                Matcher matcher = p.matcher(rowColDiag);
                if (matcher.find()) {
                    if (i == 0) {
                        sit.count2inArowX++;
                    } else {
                        sit.count2inArowO++;
                    }
                }
            }
        }

        for (String pat : threeInARowPatterns) {
            for (int i = 0; i < 2; i++) {
                Pattern p;
                if (i != 0) {
                    p = Pattern.compile(pat.replace('x', 'o'));
                } else {
                    p = Pattern.compile(pat);
                }

                Matcher matcher = p.matcher(rowColDiag);
                if (matcher.find()) {
                    if (i == 0) {
                        sit.count3InARowX++;
                    } else {
                        sit.count3InARowO++;
                    }
                }
            }
        }

        char curSymbol = 'x';
        do {
            final String bestVariation = "-xxx-"; // best var
            final String win = "xxxx-"; // win/loss
            Pattern p = Pattern.compile(bestVariation.replace('x', curSymbol));

            Matcher matcher = p.matcher(rowColDiag);
            if (matcher.find()) {
                if (curSymbol == 'x') {
                    sit.count3InARowX_Special++;
                } else {
                    sit.count3InARowO_Special++;
                }
            }

            p = Pattern.compile(win.replace('x', curSymbol));

            Matcher matcherWin = p.matcher(rowColDiag);
            if (matcherWin.find()) {
                if (curSymbol == 'x') {
                    sit.xWin = true;
                } else {
                    sit.oWin = true;
                }
            }
            curSymbol = 'o';
        } while (curSymbol == 'x');

        /*for (int i = 0; i < 2; i++) {
         final String bestVariation = "-xxx-"; // best var
         Pattern p;
         if (i != 0) {
         p = Pattern.compile(bestVariation.replace('x', 'o'));
         } else {
         p = Pattern.compile(bestVariation);
         }

         Matcher matcher = p.matcher(rowColDiag);
         if (matcher.find()) {
         if (i == 0) {
         sit.count3InARowX_Special++;
         } else {
         sit.count3InARowO_Special++;
         }
         }
         }*/
    }

}
