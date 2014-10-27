/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conn4.Heuristics;

import conn4.Situation;
import conn4.Utilities.Connect4Utilities;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wa
 */
public class ConnectedCountScore implements IHeuristic {

    char playerSymbol;
    int maxCol, maxRow;
    EvalScoreInfos scoreInfo;
    char[][] curBoard;
    Connect4Utilities utilities;

    @Override
    public EvalScoreInfos evaluate(char[][] curBoard, char playerSymbol) {
        scoreInfo = new EvalScoreInfos();
        this.playerSymbol = playerSymbol;
        maxCol = curBoard.length;
        maxRow = curBoard[0].length;
        this.curBoard = curBoard;
        utilities = new Connect4Utilities(maxRow, maxCol);
        return evaluateMove();

    }

    private EvalScoreInfos evaluateMove() {

        Situation curSit = checkForXinARow();

        int evalValue = 0;

        evalValue = curSit.xWin ? 100 * (playerSymbol == 'x' ? 1 : -1) : 0;
        evalValue = curSit.oWin ? 100 * (playerSymbol == 'x' ? 1 : -1) : 0;

        if (evalValue == 0) {
            evalValue = curSit.count3InARowX_Special * 99 * (playerSymbol == 'x' ? 1 : -1);
            evalValue += curSit.count3InARowO_Special * 99 * (playerSymbol == 'x' ? 1 : -1);
        }

        if (evalValue == 0) {
            evalValue = curSit.count3InARowX * 75 * (playerSymbol == 'x' ? 1 : -1);
            evalValue += curSit.count3InARowO * 75 * (playerSymbol == 'o' ? 1 : -1);
        }

        if (evalValue == 0) {
            evalValue = curSit.count2inArowX * 50 * (playerSymbol == 'x' ? 1 : -1);
            evalValue += curSit.count2inArowO * 50 * (playerSymbol == 'o' ? 1 : -1);
        }

        scoreInfo.score = evalValue;
        scoreInfo.hasXWin = curSit.xWin;
        scoreInfo.hasOWin = curSit.oWin;

        //System.out.println("Score: " + scoreInfo.score);
        //utilities.printBoard(curBoard);

        return scoreInfo;        
    }

    private Situation checkForXinARow() {
        //System.out.println("Start checkForXinARow: " + new Date().getTime());
        Situation sit = new Situation();
        int count = 0;

        StringBuilder sbuilder = new StringBuilder();
        // horizontal check
        for (int i = 0; i < maxCol; i++) {
            sbuilder = new StringBuilder();
            for (int j = 0; j < maxRow; j++) {
                sbuilder.append(curBoard[i][j]);
            }
            checkPatterns(sbuilder.toString(), sit);
        }

        
        // vertical check
        for (int row = 0; row < maxRow; row++) {
            sbuilder = new StringBuilder();
            for (int col = 0; col < maxCol; col++) {
                sbuilder.append(curBoard[col][row]);
            }
            checkPatterns(sbuilder.toString(), sit);
        }
             
        // Left Top right bottom
        //First loop extracts top half of diagonals:
        for (int col = 0; col < 3; col++) {
            sbuilder = new StringBuilder(); 
            int tempCol = col;
            for (int row = 0; row < maxRow; row++) {
                if(tempCol>= maxCol)
                    break;
                sbuilder.append(curBoard[tempCol][row]);
                tempCol++;
            }
            //System.out.println(sbuilder.toString());
            checkPatterns(sbuilder.toString(), sit);
        }
        //Second loop iterates on bottom half of diagonals:
        //First loop extracts top half of diagonals:
        for (int row = 1; row < 3; row++) {
            sbuilder = new StringBuilder(); 
            int tempRow = row;
            for (int col = 0; col < maxCol; col++) {
                if(tempRow>= maxRow)
                    break;
                sbuilder.append(curBoard[col][tempRow]);
                tempRow++;
            }
            //System.out.println(sbuilder.toString());
            checkPatterns(sbuilder.toString(), sit);
        }
        
                        
        // right Top left bottom
        //First loop extracts top half of diagonals:
        for (int col = 6; col > 3; col--) {
            sbuilder = new StringBuilder(); 
            int tempCol = col;
            for (int row = 0; row < maxRow; row++) {
                if(tempCol < 0)
                    break;
                sbuilder.append(curBoard[tempCol][row]);
                tempCol--;
            }
            //System.out.println(sbuilder.toString());
            checkPatterns(sbuilder.toString(), sit);
        }

        //Second loop iterates on bottom half of diagonals:
        //First loop extracts top half of diagonals:
        for (int row = 1; row < 3; row++) {
            sbuilder = new StringBuilder(); 
            int tempRow = row;
            for (int col = 6; col >= 0; col--) {
                if(tempRow>= maxRow)
                    break;
                sbuilder.append(curBoard[col][tempRow]);
                tempRow++;
            }            
            checkPatterns(sbuilder.toString(), sit);
        }
        
        
        //System.out.println("End checkForXinARow: " + new Date().getTime());
        return sit;
    }

    private void checkPatterns(String rowColDiag, Situation sit) {
        List<String> twoInARowPatterns = new ArrayList<String>();
        twoInARowPatterns.add("-xx-");
        twoInARowPatterns.add("xx--");
        twoInARowPatterns.add("--xx");
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
