/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameFiles;

import com.sun.codemodel.util.JavadocEscapeWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.jasper.tagplugins.jstl.ForEach;

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

    // Members
    private List<Card> deck; // Deck of cards for the game.
    private Player[] players;
    

    // Constructor
    public Game() {

	this.deck = new ArrayList<>(52); // Create new card deck for the game.
	this.initDeck(); // put card values and shuffle.
	this.players = new Player[numOfPlayers];
	for (int i = 0; i < this.players.length; i++)
	    this.players[i] = new Player(this.deck.subList(i * handSize, i * handSize + handSize));
    }

    private void initDeck() {

	for (int i = 1; i <= numOfSuits; i++)
	    for (int j = 2; j <= cardsInSuit + 1; j++)
		this.deck.add(new Card(i, j));

	Collections.shuffle(this.deck);

    }

    // return client's hand in string formation
    public String getHand() {
	return this.players[0].handStr();
    }

    
    public static void main(String[] args)
    {
	Game g = new Game();
	System.out.println(g.getHand());
    }

}
