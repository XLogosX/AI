/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conn4.Heuristics;

/**
 *
 * @author wa
 */
public interface IHeuristic {

    EvalScoreInfos evaluate(char[][] board, char playerSymbol);
}
