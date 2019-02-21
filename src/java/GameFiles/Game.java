/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameFiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Alon
 */
public class Game
{

    public Integer[] deck;

    public Game()
    {

	this.deck = new Integer[52];
	int k = 0;

	for (int i = 1; i <= 4; i++)
	{
	    for (int j = 2; j <= 14; j++)
	    {
		this.deck[k] = i * 100 + j;
		k++;
	    }
	}

	List<Integer> tmp = Arrays.asList(this.deck);
	Collections.shuffle(tmp);
	this.deck = (Integer[]) tmp.toArray();
	this.printDeck();
    }

    public void printDeck()
    {
	String deckStr = "";
	for (Integer card : this.deck)
	{
	    deckStr += cardToStr(card) + ", ";
	}
	System.out.println(deckStr);
    }

    public String cardToStr(int cardNum)
    {
	String card = "";
	int suit = cardNum / 100;
	int val = cardNum % 100;
	if (val > 9)
	{
	    if (val == 10)
		card = "t";
	    if (val == 11)
		card = "j";
	    if (val == 12)
		card = "q";
	    if (val == 13)
		card = "k";
	    if (val == 14)
		card = "a";
	}
	else
	    card = "" + val;

	if (suit == 1)
	    card += "c";
//	    card += "♣";
	if (suit == 2)
	    card += "d";
//	    card += "♦";
	if (suit == 3)
	    card += "s";
//	    card += "♠";
	if (suit == 4)
	    card += "h";
//	    card += "♥";

	return card;

    }

    public String getHand()
    {
	String hand = "";
	int[] handArr = new int[this.deck.length / 4];

	for (int i = 0; i < handArr.length; i++)
	    handArr[i] = this.deck[i];
	Arrays.sort(handArr);
	for (int i = 0; i < handArr.length; i++)
	    hand += cardToStr(handArr[i]);
	return hand;
    }

    public static void main(String[] args)
    {
	Game g = new Game();
	System.out.println(g.getHand());
    }

}
