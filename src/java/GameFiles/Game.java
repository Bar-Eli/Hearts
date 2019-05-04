/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.json.JSONObject;

/**
 *
 * @author Alon
 */
public class Game
{

    // Constants
    public static final int numOfSuits = 4;
    public static final int numOfPlayers = 4;
    public static final int cardsInSuit = 13;
    public static final int deckSize = numOfSuits * cardsInSuit;
    public static final int handSize = deckSize / numOfPlayers;
    public static final BidiMap<Integer, String> playesrMap = new TreeBidiMap<Integer, String>() // player's id to location dictionary.
    {
	{
	    put(0, "south");
	    put(1, "west");
	    put(2, "north");
	    put(3, "east");
	}
    };

    // Members
    protected List<Card> deck; // Deck of cards for the game.
    protected Player[] players;
    protected Card clientCard;
    protected Board board;
    protected History history;
    protected Session session;
    protected final Object syncObject = new Object();

    // Constructor
    public Game(Session session) {

	this.deck = new ArrayList<>(52); // Create new card deck for the game.
	this.initDeck(); // put card values.
	Collections.shuffle(this.deck); // Shuffle deck.

	// Split to "initPlayers" ??
	this.players = new Player[numOfPlayers]; // Create players' array
	for (int i = 0; i < this.players.length; i++)
	    this.players[i] = new Player(this.deck.subList(i * handSize, i * handSize + handSize), i); // assign cards for each player.

	// DEBUG hand print
	for (int i = 0; i < this.players.length; i++)
	    System.out.println(this.players[i].handStr());

	this.board = new Board();

	this.session = session;
	this.assignHand();
	this.clientCard = new Card();

	this.history = new History();

    }

    // Constructors to use for inheritance.
    public Game() {

	this.deck = new ArrayList<>(52); // Create new card deck for the game.
	this.initDeck(); // put card values.
	this.players = new Player[numOfPlayers]; // Create players' array
	this.board = new Board();
	this.session = null;

	this.history = new History();

    }

    public Game(History h, Board b) {

	this.deck = new ArrayList<>(52); // Create new card deck for the game.
	this.initDeck(); // put card values.
	this.players = new Player[numOfPlayers]; // Create players' array
	this.board = new Board(b);
	this.session = null;

	this.history = new History(h);

    }

    // Create and shuffle the game's deck of cards
    public void initDeck() {

	for (int i = 1; i <= numOfSuits; i++)
	    for (int j = 2; j <= cardsInSuit + 1; j++)
		this.deck.add(new Card(i, j));

    }

    // Assign hand to client (send hand's string formation)
    public void assignHand() {

	JSONObject obj = new JSONObject();
	obj.put("type", "init");
	obj.put("hand", this.players[0].handStr());

	//<editor-fold defaultstate="collapsed" desc="Send to client">
	try
	{
	    this.session.getBasicRemote().sendText(obj.toString());
	} catch (IOException ex)
	{
	    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
	}
	//</editor-fold>

    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    // Gettesr and Setters
    public List<Card> getDeck() {
	return deck;
    }

    public void setDeck(List<Card> deck) {
	this.deck = deck;
    }

    public Player[] getPlayers() {
	return players;
    }

    public void setPlayers(Player[] players) {
	this.players = players;
    }

    public Card getClientCard() {
	return clientCard;
    }

    public Board getBoard() {
	return board;
    }

    public void setBoard(Board board) {
	this.board = board;
    }

    public History getHistory() {
	return history;
    }

    public void setHistory(History history) {
	this.history = history;
    }
    //</editor-fold>

    // Set the card played by cliend and notify waiting thread.
    public void setClientCard(Card clientCard) {
	this.clientCard = new Card(clientCard);
	System.out.println(this.clientCard);
	synchronized (this.syncObject)
	{
	    this.syncObject.notifyAll();
	}

    }

    public void playGame() {

	Runnable r = new Runnable()
	{
	    @Override
	    public void run() {

		int starter = Game.this.findStarter();
		for (int i = 0; i < Game.handSize; i++)
		{
		    Game.this.doTurn(starter);
		    Game.this.clearBoardGUI();
		    starter = Game.this.board.findWinner();
		    System.out.println(Game.this.board);
		    Game.this.players[starter].addScore(Game.this.board.getRoundPoints());
		    Game.this.clientCard.clear();
		    Game.this.board.clear();

		}
		sendRes();
	    }
	};

	Thread gameThread = new Thread(r);
	gameThread.start();

    }

    public void checkCard() {

	if (this.clientCard.isEmpty())
	    //<editor-fold defaultstate="collapsed" desc="Stop thread">
	    try
	    {
		synchronized (this.syncObject)
		{
		    //System.out.println("Test");
		    this.syncObject.wait();
		    //System.out.println("Waited");
		}

	    } catch (Exception ex)
	    {
		Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
	    }
	//</editor-fold>
    }

    public int findStarter() {
	for (int i = 0; i < this.players.length; i++)
	    if (this.players[i].getHand().get(0).equals("2c"))
		return i;
	return -1;
    }

    public void doTurn(int starter) {

	for (int i = 0; i < this.players.length; i++)
	{
	    int playerI = (i + starter) % (this.players.length);
	    Card current;
	    if (playerI == 0) // GUI player's turn
	    {
		this.checkCard();
		current = this.clientCard;
	    }
	    else
	    {
		current = this.players[playerI].play(this.board, this.history);
		this.playCard(playesrMap.get(playerI), current.toString());
	    }

	    this.board.update(current, playerI);
	    this.history.update(playerI, this.board.getRoundSuit(), current);
	    
	    if (i == 0)
		this.assignSuitGUI(this.board.getRoundSuit());
	}

    }

    public void assignSuitGUI(int suitNum) {
	char suit = Card.suitsMap.get(suitNum);
	JSONObject obj = new JSONObject();
	obj.put("type", "suit");
	obj.put("val", suit + "");

	//<editor-fold defaultstate="collapsed" desc="Send to client">
	try
	{
	    this.session.getBasicRemote().sendText(obj.toString());

	} catch (IOException ex)
	{
	    Logger.getLogger(Game.class
		    .getName()).log(Level.SEVERE, null, ex);
	}
	//</editor-fold>
    }

    public void playCard(String player, String card) {

	JSONObject obj = new JSONObject();
	obj.put("type", "play");
	obj.put("player", player);
	obj.put("card", card);

	//<editor-fold defaultstate="collapsed" desc="Send to client">
	try
	{
	    this.session.getBasicRemote().sendText(obj.toString());

	} catch (IOException ex)
	{
	    Logger.getLogger(Game.class
		    .getName()).log(Level.SEVERE, null, ex);
	}
	//</editor-fold>
    }

    public void clearBoardGUI() {

	//<editor-fold defaultstate="collapsed" desc="Wait 2 seconds">
	try
	{
	    Thread.sleep(2000);
	} catch (InterruptedException ex)
	{
	    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
	}
	//</editor-fold>

	JSONObject obj = new JSONObject();
	obj.put("type", "clear");

	//<editor-fold defaultstate="collapsed" desc="Send to client">
	try
	{
	    this.session.getBasicRemote().sendText(obj.toString());

	} catch (IOException ex)
	{
	    Logger.getLogger(Game.class
		    .getName()).log(Level.SEVERE, null, ex);
	}
	//</editor-fold>
    }

    public void sendRes() {
	String res = "";
	for (int i = 0; i < numOfPlayers; i++)
	    res += "\n" + playesrMap.get(i) + " " + i + " " + this.players[i].getScore();
	System.out.println(res);

	JSONObject obj = new JSONObject();
	obj.put("type", "res");
	obj.put("res", res);

	//<editor-fold defaultstate="collapsed" desc="Send to client">
	try
	{
	    this.session.getBasicRemote().sendText(obj.toString());

	} catch (IOException ex)
	{
	    Logger.getLogger(Game.class
		    .getName()).log(Level.SEVERE, null, ex);
	}
	//</editor-fold>
    }

}
