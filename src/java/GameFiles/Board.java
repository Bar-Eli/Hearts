/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameFiles;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Alon
 */
public class Board
{

    // Members
    private Card[] table;
    private int roundSuit;
    private int roundPoints;

    // Constructor
    public Board() {

	this.table = new Card[Game.numOfPlayers];
	for (int i = 0; i < this.table.length; i++)
	    this.table[i] = new Card();

	this.roundSuit = 0;
	this.roundPoints = 0;
    }

    // Copy constructor
    public Board(Board b) {

	this.table = new Card[Game.numOfPlayers];
	for (int i = 0; i < this.table.length; i++)
	    this.table[i] = new Card(b.table[i]);

	this.roundSuit = b.roundSuit;
	this.roundPoints = b.roundPoints;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Card[] getTable() {
	return table;
    }

    public List<Card> getTableList() {
	return Arrays.asList(this.table);
    }

    public void setTable(Card[] table) {
	this.table = table;
    }

    public int getRoundSuit() {
	return roundSuit;
    }

    public void setRoundSuit(int roundSuit) {
	this.roundSuit = roundSuit;
    }

    public int getRoundPoints() {
	return roundPoints;
    }

    public void setRoundPoints(int roundPoints) {
	this.roundPoints = roundPoints;
    }
    //</editor-fold>

    @Override
    public String toString() {
	return "Board{" + "table=" + Arrays.toString(table) + ", roundSuit=" + Card.suitsMap.get(roundSuit) + ", roundPoints=" + roundPoints + '}';
    }

    // clear board
    public void clear() {
	for (int i = 0; i < this.table.length; i++)
	    this.table[i].clear();
	this.roundSuit = 0;
	this.roundPoints = 0;

    }

    // find winner of the round
    public int findWinner() {

	int max = 0, maxI = -1;
	for (int i = 0; i < this.table.length; i++)
	{
	    Card c = this.table[i];
	    if (c.getSuit() == this.roundSuit && c.getVal() > max)
	    {
		max = c.getVal();
		maxI = i;
	    }
	    if (c.getSuit() == 4)
		this.roundPoints++;
	    if (c.equals("qs"))
		this.roundPoints += 13;

	}

	return maxI;
    }

    // Board status
    public boolean isEmpty() {
	if (this.roundSuit == 0) // no suit -- board is empty
	    return true;
	for (int i = 0; i < this.table.length; i++)
	    if (!(this.table[i].isEmpty()))
		return false;
	return true;
    }

    public boolean isFull() {
	if (this.roundSuit == 0) // no suit -- board is empty
	    return false;
	for (int i = 0; i < this.table.length; i++)
	    if ((this.table[i].isEmpty()))
		return false;
	return true;
    }
    
    // List of cards played in a round
    public List<Card> playedCards() {
	List<Card> playedCards = Arrays.asList(table);
	playedCards.removeIf((Card c) -> c.isEmpty()); //Removing empty cards
	return playedCards;
    }
    
    public void update(Card c, int playerI) {
	
	if (this.isEmpty())
	    this.roundSuit = c.getSuit();
	
	this.roundPoints += c.points();
	
	this.table[playerI] = new Card(c);
	
    }

    public static void main(String[] args) {

	System.out.println((3 + 2) % 4);
    }

}
