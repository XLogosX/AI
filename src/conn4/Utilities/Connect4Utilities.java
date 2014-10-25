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

    public boolean isColumnFull(char[][] board, int column) {
        return board[column][0] != '-';
    }

    public boolean hasPlayerWon(char[][] board, char playerSymbol) {
        boolean win = false;
        int COL_SIZE = board.length;
        int ROW_SIZE = board[0].length;

        //check for win horizontally
        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < COL_SIZE - 3; col++) { //0 to 3
                if (board[col][row] == board[col + 1][row]
                        && board[col][row] == board[col + 2][row]
                        && board[col][row] == board[col + 3][row]
                        && board[col][row] != '-') {
                    win = true;
                    break;
                }
            }
        }
        //check for win vertically
        for (int row = 0; row < ROW_SIZE - 3; row++) { //0 to 2
            for (int col = 0; col < COL_SIZE; col++) {
                if (board[col][row] == board[col][row + 1]
                        && board[col][row] == board[col][row + 2]
                        && board[col][row] == board[col][row + 3]
                        && board[col][row] != '-') {
                    win = true;
                    break;
                }

            }
        }
        //check for win diagonally (upper left to lower right)
        for (int row = 0; row < ROW_SIZE - 3; row++) { //0 to 2
            for (int col = 0; col < COL_SIZE - 3; col++) { //0 to 3
                if (board[col][row] == board[col + 1][row + 1]
                        && board[col][row] == board[col + 2][row + 2]
                        && board[col][row] == board[col + 3][row + 3]
                        && board[col][row] != '-') {
                    win = true;
                    break;
                }
            }
        }
        //check for win diagonally (lower left to upper right)
        for (int row = 3; row < ROW_SIZE; row++) { //3 to 5
            for (int col = 0; col < COL_SIZE - 3; col++) { //0 to 3
                if (board[col][row] == board[col + 1][row - 1]
                        && board[col][row] == board[col + 2][row - 2]
                        && board[col][row] == board[col + 3][row - 3]
                        && board[col][row] != '-') {
                    win = true;
                    break;
                }
            }
        }

        return win;
    }
}
