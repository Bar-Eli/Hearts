/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameFiles;

import java.util.Arrays;

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

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Card[] getTable() {
	return table;
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

    public void clear() {
	for (int i = 0; i < this.table.length; i++)
	    this.table[i].clear();
	this.roundSuit = 0;
	this.roundPoints = 0;

    }

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

    public static void main(String[] args) {

	System.out.println((3 + 2) % 4);
    }

}
