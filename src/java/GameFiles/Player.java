/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameFiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Alon
 */
public class Player
{

//    private int id;
    private int score;
    private List<Card> hand;

    public Player() {
	//this.id = -1;
	this.score = 0;
	this.hand = new ArrayList<>();
    }

    public Player(List<Card> hand) {
	this.hand = new ArrayList<>(hand);
	Collections.sort(this.hand);
	System.out.println(this.hand);
    }

    
    public Player(Player p) {
	this.hand = new ArrayList<>(p.hand);
	this.score = p.score;
    }

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
    //</editor-fold>

    public String handStr() {
	String hand = "";
	hand = this.hand.stream().map((c) -> c.toString()).reduce(hand, String::concat);
	//for (Card c : this.hand)
	//hand += c.toString();
	return hand;
    }

    public Card play(Board board) {

	int suit = board.getRoundSuit();

	if (suit == 0)
	    return this.hand.remove(0);
	for (int i = 0; i < this.hand.size(); i++)
	    if (this.hand.get(i).getSuit() == suit)
		return this.hand.remove(i);

	return this.hand.remove(0);
    }

    @Override
    public String toString() {
	return "Player{" + "score=" + score + ", hand=" + this.handStr() + '}';
    }

}
