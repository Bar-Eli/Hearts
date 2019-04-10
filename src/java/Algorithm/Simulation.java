/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

/**
 *
 * @author Alon
 */
import GameFiles.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Simulation extends Game
{

    private int turn;
    private HashMap<Integer, Card> deckMap;
    private int currentSize;

    public Simulation() {
	super();
	this.currentSize = Game.handSize;
//	this.initDeckMap();
    }

    public void initDeckMap() {
	for (Card c : this.deck)
	    this.deckMap.put(c.getSuit() * c.getVal(), c);

    }

    @Override
    public void doTurn(int starter) {

	for (int i = 0; i < this.players.length; i++)
	{
	    int playerI = (i + starter) % (this.players.length);
	    Card current;
//	    if (playerI == 0)
//	    {
//		monte carlo
//	    }
//	    else
//	    {
	    current = this.players[playerI].play(this.board);
//	    }
	    if (i == 0)
		this.board.setRoundSuit(current.getSuit());
	    this.board.getTable()[playerI] = new Card(current);
	}
    }

    public void simulateGame() {
	
	int starter = this.findStarter();
	for (int i = 0; i < Game.handSize; i++)
	{
	    this.doTurn(starter);
	    starter = this.board.findWinner();
	    System.out.println(this.board);
	    this.players[starter].addScore(this.board.getRoundPoints());
	    this.board.clear();

	}
	System.out.println(Arrays.toString(this.players));
    }

    public static void main(String[] args) {
	Simulation s = new Simulation();
	s.simulateGame();
	
    }
}
