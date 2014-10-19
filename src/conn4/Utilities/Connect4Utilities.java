/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conn4.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wa
 */
public class Connect4Utilities {

    int maxRow, maxCol;
    char[][] curBoard;

    public Connect4Utilities(int maxR, int maxC) {
        maxRow = maxR;
        maxCol = maxC;
    }

    public void printBoard(char[][] board) {
        StringBuilder sbuilder;
        for (int j = 0; j < maxRow; j++) {
            sbuilder = new StringBuilder();
            sbuilder.append("|");
            for (int i = 0; i < maxCol; i++) {
                sbuilder.append(board[i][j]).append("|");
            }
            System.out.println(sbuilder.toString());
        }
        System.out.println("");
    }

    public List<Integer> GetValidMoves(char[][] curSit) {
        List<Integer> ValidMoves = new ArrayList<Integer>();
        for (int col = 0; col <= maxCol - 1; col++) {
            if (curSit[col][0] == '-') {
                ValidMoves.add(col);
            }
        }
        curSit = null;
        return ValidMoves;
    }

    private boolean IsMoveValid(char[][] board, int col) {
        // Row=0 couse maxRow=bottom
        return board[col][0] == '-';
    }

    //removes the last move from the specified column.
    public char[][] unmove(char[][] board, int curCol) {
        for (int i = 0; i <= maxRow - 1; i++) {
            if (board[curCol][i] != '-') {
                board[curCol][i] = '-';
                break;
            }
        }
        return board;
    }

    //places a piece in the specified column for the player.
    public char[][] move(char[][] board, int curCol, char playerSymbol) {
        for (int i = maxRow - 1; i >= 0; i--) {
            if (board[curCol][i] == '-') {
                board[curCol][i] = playerSymbol;
                break;
            }
        }
        return board;
    }

}
