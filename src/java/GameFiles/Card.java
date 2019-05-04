/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameFiles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

/**
 *
 * @author Alon
 */
public class Card implements Comparable<Card>
{

    // Constants
    public static final BidiMap<Integer, Character> suitsMap = new TreeBidiMap<Integer, Character>() // Suits' number to char dictionary.
    {
	{
	    put(1, 'c');
	    put(2, 'd');
	    put(3, 's');
	    put(4, 'h');
	}
    };
    public static final BidiMap<Integer, Character> cardsMap = new TreeBidiMap<Integer, Character>() // Card's number to char dictionary.
    {
	{
	    put(2, '2');
	    put(3, '3');
	    put(4, '4');
	    put(5, '5');
	    put(6, '6');
	    put(7, '7');
	    put(8, '8');
	    put(9, '9');
	    put(10, 't');
	    put(11, 'j');
	    put(12, 'q');
	    put(13, 'k');
	    put(14, 'a');
	}
    };

    // Members
    private int suit; // Card's suit (Club / Dimond / Spade / Heart).
    private int val; // Card's vlaue (from 2 to A).

    // Constructors
    public Card() // Reset values
    {
	this.suit = 0;
	this.val = 0;
    }

    public Card(int suit, int val) // Given values as integers.
    {
	if (suit < 1 || suit > 4 || val < 2 || val > 14)
	    System.out.println("Invalid card"); //	    return;

	this.suit = suit;
	this.val = val;
    }

    public Card(String c) // Given values as String (e.g. "3c").
    {
	this.val = cardsMap.getKey(c.charAt(0)); // Assign card's value
	this.suit = suitsMap.getKey(c.charAt(1)); // Assign card's suit

    }

    public Card(Card c) // Copy constructor
    {
	this.suit = c.suit;
	this.val = c.val;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    // Getters and Setters
    public int getSuit() {
	return suit;
    }

    public void setSuit(int suit) {
	this.suit = suit;
    }

    public int getVal() {
	return val;
    }

    public void setVal(int val) {
	this.val = val;
    }
    //</editor-fold>

    @Override
    public String toString() {
	if(this.isEmpty())
	    return "Empty";
	return cardsMap.get(this.val).toString() + suitsMap.get(this.suit);
    }

    // Comparing fuctions
    @Override
    public int compareTo(Card c) {
	return (this.suit * 100 + this.val) - (c.suit * 100 + c.val);
    }

    public boolean equals(Card c) {

	return this.suit == c.suit && this.val == c.val;
    }
    
    
    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Card))
	    return false;
	return this.equals((Card)o);
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 71 * hash + this.suit;
	hash = 71 * hash + this.val;
	return hash;
    }
    
    // ADD TO PROJECT FILE
    // returns the points value of card
    public int points() {
	
	if (this.getSuit() == suitsMap.getKey('h'))
	    return 1;
	if (this.equals("qs"))
	    return 13;
	
	return 0;
    }
    
    public boolean equals(String cStr) {
	Card c = new Card(cStr);
	return this.equals(c);
    }

    public boolean isEmpty() {
	return this.suit == 0 || this.val == 0;
    }

    public void clear() {
	this.suit = 0;
	this.val = 0;
    }

    public static void main(String[] args) {
	Card[] cArr =
	{
	    new Card(1, 11), new Card(2, 13), new Card(4, 5)
	};
	System.out.println(Arrays.toString(cArr));

    }
}
