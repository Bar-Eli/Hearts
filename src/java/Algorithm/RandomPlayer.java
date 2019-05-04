/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import GameFiles.Board;
import GameFiles.Card;
import GameFiles.History;
import GameFiles.Player;

/**
 *
 * @author Alon
 */
public class RandomPlayer extends Player
{
    // Play Card randomly
    @Override
    public Card play(Board board, History history) {
	
	return new Card();
    }
}
