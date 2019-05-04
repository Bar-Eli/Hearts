/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameFiles;

import Algorithm.MonteCarlo;
import Algorithm.Simulation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Alon
 */
public class Player
{

    protected int id; // location on board
    protected int score;
    protected List<Card> hand;

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public Player(int id) {
	this.id = id;
	this.score = 0;
	this.hand = new ArrayList<>();
    }

    public Player() {
	this.id = -1;
	this.score = 0;
	this.hand = new ArrayList<>();
    }

    public Player(List<Card> hand, int id) {
	this.hand = new ArrayList<>(hand);
	Collections.sort(this.hand);
	this.id = id;
    }

    public Player(Player p) {
	this.hand = new ArrayList<>(p.hand);
	this.score = p.score;
	this.id = p.id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getScore() {
	return score;
    }

    public void setScore(int score) {
	this.score = score;
    }

    public void addScore(int score) {
	this.score += score;
    }

    public List<Card> getHand() {
	return hand;
    }

    public void setHand(List<Card> hand) {
	this.hand = hand;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }
    //</editor-fold>

    public boolean hasSuit(int suit) {
	for (Card c : this.hand)
	    if (c.getSuit() == suit)
		return true;
	return false;
    }

    // return a card which is the only one of its suits in player's hand (if exsists).
    public Card oneInSuit() {

	// Add -- return card with max val of when many oneInSuit cards.
	
	Card c = this.hand.get(0);
	int suitCount = 1;

	for (int i = 1; i < this.hand.size(); i++)
	{
	    if (this.hand.get(i).getSuit() != c.getSuit())
	    {
		if (suitCount == 1)
		    return c;
		suitCount = 1;
	    }
	    suitCount++;
	    c = this.hand.get(i);
	}

	return new Card();
    }

    // return all cards in hand of a specified suit.
    public List<Card> cardsOfSuit(int suit) {

	List<Card> suitCards = new ArrayList();

	for (Card c : this.hand)
	    if (c.getSuit() == suit)
		suitCards.add(c);

	return suitCards;
    }

    // return queen spade or higher if exsists in hand.
    public Card dangerousSpade() {

	int qVal = Card.cardsMap.getKey('q');
	List<Card> spades = cardsOfSuit(Card.suitsMap.getKey('s'));
	
	for (Card c : spades)
	    if (c.getVal() >= qVal)
		return c;
	
	return new Card();

    }

    public Card highVal() {
	
	Card max = new Card();
	
	for(Card c : this.hand)
	    if(c.getVal() > max.getVal())
		max = c;
	
	if(max.isEmpty())
	    System.out.println("Max Card is empty");
	
	return max;
    }
    
    public Card lowVal() {
	
	return new Card();
    }
    
    public Card play(Board board, History history) {

	// Remove selected card from player's hand
	Card bestCard = MonteCarlo.findBestCard(this, board, history);
	this.hand.remove(bestCard);
	return bestCard;

	/*
	
	int suit = board.getRoundSuit();
	Card c = new Card();

	if (suit == 0)
	    c = this.hand.get(0);
	else
	    for (int i = 0; i < this.hand.size(); i++)
		if (this.hand.get(i).getSuit() == suit)
		{
		    c = this.hand.get(i);
		    break;
		}

	if (c.isEmpty())
	    c = this.hand.get(0);

	History h = new History();
	Simulation s = new Simulation(this, h, board);
	s.simulateGame(c);

	this.hand.remove(c);
	return c;
	 */
    }

    public String handStr() {
	String hand = "";
	hand = this.hand.stream().map((c) -> c.toString()).reduce(hand, String::concat);
	//for (Card c : this.hand)
	//hand += c.toString();
	return hand;
    }

    @Override
    public String toString() {
	return "Player{" + "id=" + id + ", score=" + score + ", hand=" + this.handStr() + '}';
    }

}
