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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//import java.util.List;

public class Simulation extends Game
{

    private int currentSize;
    private int simPlayerID; //************
    private Board originalBoard;
    private History originalHistory;

    public Simulation(Player p, History h, Board b) {
	super(h, b);
	this.originalBoard = new Board(b);
	this.originalHistory = new History(h);
	this.currentSize = p.getHand().size();
	this.simPlayerID = p.getId();

	// Remove irrelevant cards from deck.	
	//this.deck.removeAll(p.getHand());
	Collections.shuffle(this.deck); // Shuffle deck.

	this.players[p.getId()] = new DemoPlayer(p); // Demo player for checking

	// Init demo players
	this.initPlayers();

	System.out.println("Simulation for player " + this.simPlayerID);

	// DEBUG hand print
	//for (int i = 0; i < this.players.length; i++)
	//{
	//String s = (this.players[i]).handStr();
	//System.out.println("Sim " + i + " hand: " + s);
	//}
    }

    public void initPlayers() {

	// Remove already played cards from deck.
	this.deck.removeAll(this.history.playedCards());
	// Remove simulation player cards from deck.
	this.deck.removeAll(this.players[this.simPlayerID].getHand());

	for (int i = 0; i < this.players.length; i++)
	{
	    if (i == this.simPlayerID)
		continue;
	    this.players[i] = new DemoPlayer(this.dealCards(i), i); // assign cards for each player.
	}

    }

    // Deal cards to demo players based on history
    public List<Card> dealCards(int id) {

	int size = this.currentSize; // number of cards to deal this player
	if (!this.board.getTable()[id].isEmpty()) // one less to deal on case player already played this round
	    size--;

	boolean[] missingSuits = this.history.getPlayersWithoutSuit()[id];
	List<Card> hand = new ArrayList();

	
	while (size > 0)
	{
	    Card c = this.deck.get(0);
	    if (!missingSuits[c.getSuit()])
	    {
		hand.add(c);
		this.deck.remove(0);
		size--;
	    }
	}
	
	return hand;
    }

    // Reset simulation to original state (simulate game again)
    public void reset() {

	this.board = new Board(this.originalBoard);
	this.history = new History(this.originalHistory);
	for (int i = 0; i < this.players.length; i++)
	    ((DemoPlayer) this.players[i]).reset();
    }

    @Override
    public int findStarter() {

	int start = super.findStarter();
	if (start != -1)
	    return start;

	Card[] table = this.board.getTable();
	for (int i = 0; i < table.length; i++)
	    if (!(table[i].isEmpty()))
		return i;

	return -1;
    }

    @Override
    public void doTurn(int starter) {

	for (int i = 0; i < this.players.length; i++)
	{

	    int playerI = (i + starter) % (this.players.length);
	    Card current;

	    if (!this.board.getTable()[playerI].isEmpty()) // partly played round
		break;

	    current = this.players[playerI].play(this.board, this.history);

	    if (i == 0)
		this.board.setRoundSuit(current.getSuit());
	    this.board.getTable()[playerI] = new Card(current);
	}
    }

    public void simulateGame(Card c) {

	int starter = this.simPlayerID;
	if (this.players[this.simPlayerID] instanceof DemoPlayer)
	    ((DemoPlayer) this.players[this.simPlayerID]).setMustPlay(c);

	for (int i = 0; i < this.currentSize; i++)
	{
	    this.doTurn(starter);
	    starter = this.board.findWinner();
//	    System.out.println(this.board);
	    this.players[starter].addScore(this.board.getRoundPoints());
	    this.board.clear();

	}
	//System.out.println(Arrays.toString(this.players));

	// DEBUG print
	System.out.println("Simulating Card " + c + ", Result: " + this.players[this.simPlayerID].getScore());
    }

    public int getPlayerScore() {
	return this.players[this.simPlayerID].getScore();
    }

    public static void main(String[] args) {
	Game g = new Game();
	Collections.shuffle(g.getDeck());
	ArrayList<Card> hand = new ArrayList<>();
	for (int i = 0; i < 13; i++)
	    hand.add(new Card(g.getDeck().get(i)));
	Player p = new Player(hand, 0);

//	Simulation s = new Simulation(p);
//	s.simulateGame();
    }
}
