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
import java.util.List;
import java.util.Random;

/**
 *
 * @author Alon
 */
public class RandomPlayer extends Player
{

    public RandomPlayer(Player p) {
	super(p);
    }
    
    public RandomPlayer(List<Card> hand, int id) {
	super(hand, id);
    }
    
    
    
    
    // Play (legal) Card randomly.
    @Override
    public Card play(Board board, History history) {
	
	//System.out.println("RandomPlayer");
	
	if (this.hand.get(0).equals("2c"))
	    return this.hand.remove(0);
	
	Random rand = new Random();
	int cardI;
	List<Card> legalMoves = this.hand;
	
	if (!board.isEmpty())
	{
	   List<Card> suitCards = this.cardsOfSuit(board.getRoundSuit());
	    if (suitCards.size() > 0)
		legalMoves = suitCards;
	   
	}
	
	cardI = rand.nextInt(legalMoves.size());
	Card c = legalMoves.get(cardI);
	this.hand.remove(c);
	return c;
    }
}
