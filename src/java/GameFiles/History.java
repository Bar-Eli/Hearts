/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameFiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Alon
 */
public class History
{

    private List<Pair<Integer, Card>> data;
    private boolean[][] cardsOut;
    private boolean[][] playersWithoutSuit;
    private int[] suitsCardsLeft;
    private int pointsOut;

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public History() {

	this.data = new ArrayList<>();
	this.cardsOut = new boolean[5][15]; // All set to false
	this.playersWithoutSuit = new boolean[4][5]; // All set to false;

	this.suitsCardsLeft = new int[5];
	Arrays.fill(this.suitsCardsLeft, Game.cardsInSuit); // 13 cards for each suit.

	this.pointsOut = 0; // no points at the begining of the game.

    }

    public History(History h) {

	this.data = new ArrayList<>(h.data);

	this.cardsOut = h.cardsOut.clone();
	for (int i = 0; i < this.cardsOut.length; i++)
	    this.cardsOut[i] = h.cardsOut[i].clone();

	this.playersWithoutSuit = h.playersWithoutSuit.clone();
	for (int i = 0; i < this.playersWithoutSuit.length; i++)
	    this.playersWithoutSuit[i] = h.playersWithoutSuit[i].clone();

	this.suitsCardsLeft = h.suitsCardsLeft.clone();

	this.pointsOut = h.pointsOut;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<Pair<Integer, Card>> getData() {
	return data;
    }

    public void setData(List<Pair<Integer, Card>> data) {
	this.data = data;
    }

    public boolean[][] getCardsOut() {
	return cardsOut;
    }

    public void setCardsOut(boolean[][] cardsOut) {
	this.cardsOut = cardsOut;
    }

    public boolean[][] getPlayersWithoutSuit() {
	return playersWithoutSuit;
    }

    public void setPlayersWithoutSuit(boolean[][] playersWithoutSuit) {
	this.playersWithoutSuit = playersWithoutSuit;
    }

    public int[] getSuitsOut() {
	return suitsCardsLeft;
    }

    public void setSuitsOut(int[] suitsOut) {
	this.suitsCardsLeft = suitsOut;
    }

    public int getPointsOut() {
	return pointsOut;
    }

    public void setPointsOut(int pointsOut) {
	this.pointsOut = pointsOut;
    }

    //</editor-fold>
    public List<Card> playedCards() {

	List<Card> playedCards = new ArrayList<>();
	
	for (Pair p : this.data)
	    playedCards.add((Card) p.getValue());
	
	return playedCards;
    }

    // chance for point cards being played in a round with given suit (and given player's hand).
    public boolean pointsChance(int suit, List<Card> suitCards) {

	if (this.pointsOut == 26)
	    return false;

	if (suit == Card.suitsMap.getKey('h'))
	    return true;

	if (suit == Card.suitsMap.getKey('s') && !this.isQSplayed())
	    return true;

	for (boolean ok : this.playersWithoutSuit[suit])
	    if (ok)
		return true;

	return this.suitsCardsLeft[suit] - suitCards.size() > 6;

    }

    // whether or not the QS was played
    public boolean isQSplayed() {

	return this.cardsOut[Card.suitsMap.getKey('s')][Card.cardsMap.getKey('q')];
    }

    // risk to win a round when playing specific card
    public int risk(Card c, List<Card> suitCards) {

	int risk = 0;
	int suit = c.getSuit();
	int val = c.getVal();

	for (Card card : suitCards)
	{
	    if(card.getVal() < val)
		risk--;
	    if(card.getVal() > val)
		risk++;
	}
	
	for (int i = 2; i < this.cardsOut[suit].length; i++)
	{
	    if (i < val && !this.cardsOut[suit][i]) // smaller card not out yet.
		risk++;

	    if (i > val && !this.cardsOut[suit][i]) // higher card not out yet.
		risk--;
	}
	
	return risk;

    }

    // update history
    public void update(int playerID, int roundSuit, Card c) {

	this.data.add(new Pair(playerID, new Card(c)));
	this.cardsOut[c.getSuit()][c.getVal()] = true;

	if (c.getSuit() != roundSuit)
	    this.playersWithoutSuit[playerID][roundSuit] = true;

	this.suitsCardsLeft[c.getSuit()]--;

	this.pointsOut += c.points();

    }

    public static void main(String[] args) {

	boolean[] bArr = new boolean[4];
	Arrays.fill(bArr, true);

	System.out.println(Arrays.toString(bArr));
	
	Pair<Integer, Card> p = new Pair(3, new Card("4c"));
	System.out.println(p.getValue());

    }

}
