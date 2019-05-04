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
import GameFiles.Board;
import GameFiles.Card;
import GameFiles.Game;
import GameFiles.History;
import GameFiles.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DemoPlayer extends Player
{

    private List<Card> originalHand; // Player's hand when the cards are dealt.
    private Card mustPlay; // If this card isn't empty, the player must play it.

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public DemoPlayer(Player p) {
	super(p);
	this.originalHand = new ArrayList<>(this.hand);
	this.score = 0;
	this.mustPlay = new Card();
    }

    public DemoPlayer(List<Card> hand, int id) {
	super(hand, id);
	this.originalHand = new ArrayList<>(this.hand);
	this.mustPlay = new Card();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<Card> getOriginalHand() {
	return originalHand;
    }

    public void setOriginalHand(List<Card> originalHand) {
	this.originalHand = originalHand;
    }

    public Card getMustPlay() {
	return mustPlay;
    }

    public void setMustPlay(Card mustPlay) {
	this.mustPlay = new Card(mustPlay);
    }

    public void setMustPlay(String mustPlay) {
	this.mustPlay = new Card(mustPlay);
    }
    //</editor-fold>

    public void reset() {
	this.hand = new ArrayList<>(this.originalHand);
	this.score = 0;
    }

    // choose card when doesn't have the board's suit.
    public Card noSuitPlay(Board board, History history) {

	// Aviod QS (play high spade card)
	Card dangerousSpade = this.dangerousSpade(); // maybe use copy constructor
	if (!dangerousSpade.isEmpty() && !history.isQSplayed())
	    return dangerousSpade;

	// get rid of suit with single card.
	Card oneInSuit = this.oneInSuit();
	if (!oneInSuit.isEmpty())
	    return oneInSuit;

	// Get rid of hearts (play highest heart card)
	List<Card> hearts = this.cardsOfSuit(Card.suitsMap.getKey('h'));
	if (hearts.size() > 0)
	{
	    Card highHeart = hearts.get(hearts.size() - 1); // last card of list (max val).
	    if (history.risk(highHeart, hearts) >= 0) // risky heart card.
		return highHeart;
	}

	// Play Card with highest value in hand
	return this.highVal();
    }

    // choose card when holding cards of round suit
    public Card suitPlay(Board board, History history, List<Card> suitCards) {

	int roundSuit = board.getRoundSuit();

	Card highCard = suitCards.get(suitCards.size() - 1);
	int highVal = highCard.getVal();

	List<Card> playedCards = board.playedCards();
	boolean isPoints = false;

	for (Card c : playedCards)
	{
	    if (c.getSuit() == roundSuit && c.getVal() > highVal) // higher card is out
		return highCard;
	    if (c.points() > 0)
		isPoints = true;
	}

	if (!isPoints && playedCards.size() == Game.numOfPlayers - 1) // absolute no chance for points
	    return highCard;

	if (history.pointsChance(roundSuit, suitCards))
	    return suitCards.get(0); // min card of suit 

	return highCard; // low prabability for point chance.

    }

    // Choose card to play on first turn of round.
    public Card firstTurnPlay(Board board, History history) {

	List<List<Card>> cardsBySuit = new ArrayList();

	for (int i = 1; i <= Game.numOfSuits; i++)
	    cardsBySuit.add(this.cardsOfSuit(i)); // OPTIMIZE

	Collections.sort(cardsBySuit, new Comparator<List<Card>>() // Sort cardsBySuit by suits' length
	{
	    @Override
	    public int compare(List<Card> o1, List<Card> o2) {
		return o1.size() - o2.size();
	    }
	});

	for (List<Card> suitCards : cardsBySuit)
	{
	    if (suitCards.size() == 0) // no cards to play of that suit
		continue;
	    if (!history.pointsChance(suitCards.get(0).getSuit(), suitCards)) // play highest card when there's no chance for points
		return suitCards.get(suitCards.size() - 1);
	    
	    for (int i = suitCards.size() - 1; i >= 0; i--)
	    {
		Card c = suitCards.get(i);
		if(history.risk(c, suitCards) <= 0) // return highest non risky card.
		    return c;
	    }
	}

	return this.lowVal();
    }

    @Override
    public Card play(Board board, History history) {

	// force play of a card
	if (!this.mustPlay.isEmpty())
	{
	    Card c = new Card(this.mustPlay);
	    this.hand.remove(this.mustPlay);
	    this.mustPlay.clear();
	    return c;
	}

	// Add strategy play (no monte ccarlo)
	int suit = board.getRoundSuit();

	// plays first
	if (suit == 0)
	{
	    Card c = this.firstTurnPlay(board, history);
	    this.hand.remove(c);
	    return c;
	}

	List<Card> suitCards = this.cardsOfSuit(suit);

	if (suitCards.size() != 0) // has cards of given suit
	{
	    Card c = this.suitPlay(board, history, suitCards);
	    this.hand.remove(c);
	    return c;
	}

	// doesn't have the board's suit
	Card c = this.noSuitPlay(board, history);
	this.hand.remove(c);
	return c;

	// Make sure function removes played card from player's hand
    }

}
