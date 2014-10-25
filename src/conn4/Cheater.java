/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package conn4;

import ch.hslu.ai.connect4.Game;
import ch.hslu.ai.connect4.Player;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wa
 */
public class Cheater extends Player{

    public Cheater(String name, char symbol) {
        super(name, symbol);
    }

    @Override
    public int play(char[][] board) {
           
        char x=Game.EMPTY;
        
        
        
        board[0][1]=getSymbol();
        board[0][2]=getSymbol();
        board[0][3]=getSymbol();
        board[0][4]=getSymbol();
        try {
            Field modifiersField = Game.class.getDeclaredField("board");
            modifiersField.setAccessible(true);
            char[][] gameBoard;
            gameBoard = (char[][])modifiersField.get(Game.class);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(Cheater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Cheater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Cheater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Cheater.class.getName()).log(Level.SEVERE, null, ex);
        }
                //Field.class.getClass().getField("board").set(board, board);    
        return 5;
    }
    
}
