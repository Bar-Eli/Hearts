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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alon
 */
public class MonteCarlo
{

    public final static int maxNumOfOptions = 3;
    public static final int numOfSimulations = 3;

    // find best card (based on options and simulations)
    public static Card findBestCard(Player p, Board b, History h) {

	
	List<Card> options = findSimOptions(p, b, h);
	
	// DEBUG
	//System.out.println("Player " + p.getId() + " scores: ");
	//System.out.println(options);
	
	if (options.size() == 1)
	    return options.get(0);

	int[] scores = new int[options.size()];

	for (int i = 0; i < numOfSimulations; i++)
	{
	    Simulation s = new Simulation(p, h, b);
	    for (int j = 0; j < options.size(); j++)
	    {
		s.simulateGame(options.get(j));
		scores[j] += s.getPlayerScore();
		s.reset();
	    }
	}

	// DEBUG
	/*
	for (int i = 0; i < options.size(); i++)
	    System.out.println(options.get(i) + " res: " + scores[i]);
	*/
	
	// find index for card with minimal score
	int minIndex = 0;
	for (int i = 1; i < scores.length; i++)
	    if (scores[i] < scores[minIndex])
		minIndex = i;
	return options.get(minIndex);

    }

    public static List<Card> possibleCards(List<Card> hand, int suit) {

	List<Card> legal = new ArrayList();

	// make sure starter playes 2c
	if (hand.get(0).equals("2c"))
	{
	    legal.add(new Card(hand.get(0)));
	    return legal;
	}

	if (suit == 0)
	    return legal;

	// shorter loop assuming the hand is sorted ??
	for (Card c : hand)
	    if (c.getSuit() == suit)
		legal.add(new Card(c));

	// Hand without card of suit round
	//if (legal.size() == 0)
	    //return new ArrayList(hand);
	
	return legal;

    }

    public static List<Card> findSimOptions(Player p, Board b, History h) {
	
	List<Card> legalOptions = possibleCards(p.getHand(), b.getRoundSuit());
	
	if(!legalOptions.isEmpty() && legalOptions.size() <= maxNumOfOptions)
	    return legalOptions;
	
	if (legalOptions.isEmpty() && p.getHand().size() <= maxNumOfOptions)
	    return new ArrayList(p.getHand());
	
	List<Card> simOptions = new ArrayList();
	
	DemoPlayer dp = new DemoPlayer(p);
	simOptions.add(dp.play(b, h));
	dp.reset();
	
	RandomPlayer rp = new RandomPlayer(p);
	simOptions.add(rp.play(b, h));
	
	if (legalOptions.isEmpty())
	    simOptions.add(dp.lowVal()); // minimal value of entire hand
	else
	    simOptions.add(legalOptions.get(0)); // minimal value of suit
	
	
	return simOptions;
    }
    
    
    // find best options for playing in player's hand (to simulate)
    public static List<Card> findOptions(List<Card> moves, Board b, History h) {

	List<Card> options = new ArrayList<>();

	for (int i = 0; i < maxNumOfOptions && i < moves.size(); i++)
	    options.add(moves.get(i));

	return options;
    }

    
    
    
    
    public static void main(String[] args) {

    }
}
