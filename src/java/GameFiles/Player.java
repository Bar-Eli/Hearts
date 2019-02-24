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

    private int score;
    private List<Card> hand;

    public Player() {
	this.score = 0;
	this.hand = new ArrayList<>();
    }

    public Player(List<Card> hand) {
	this.hand = new ArrayList<>(hand);
	Collections.sort(this.hand);
	System.out.println(this.hand);
    }

    public int getScore() {
	return score;
    }

    public void setScore(int score) {
	this.score = score;
    }

    public List<Card> getHand() {
	return hand;
    }

    public void setHand(List<Card> hand) {
	this.hand = hand;
    }

    public String handStr() {
	String hand = "";
	hand = this.hand.stream().map((c) -> c.toString()).reduce(hand, String::concat);
//	for (Card c : this.hand)
//	hand += c.toString();
	return hand;
    }

    public Card play() {

	return this.hand.remove(0);
    }

}
