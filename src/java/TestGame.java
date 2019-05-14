/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alon
 */
import Algorithm.DemoPlayer;
import Algorithm.RandomPlayer;
import GameFiles.Card;
import GameFiles.Game;
import static GameFiles.Game.playesrMap;
import GameFiles.History;
import GameFiles.Player;
import java.util.Arrays;

public class TestGame extends Game
{

    public TestGame() {
	super();

//	for (int i = 0; i < this.players.length; i++)
	this.players[0] = new RandomPlayer(this.dealCards(0), 0); // assign cards for each player.
	this.players[1] = new RandomPlayer(this.dealCards(1), 1); // assign cards for each player.
	this.players[2] = new DemoPlayer(this.dealCards(2), 2); // assign cards for each player.
	this.players[3] = new Player(this.dealCards(3), 3); // assign cards for each player.
    }

    @Override
    public boolean isGameOver() {

	for (Player p : this.players)
	    if (p.getScore() >= 500)
		return true;
	return false;
    }

    @Override
    public void playGame() {

	System.out.format("%-13d %-13d %-13d %-13d\n", 0, 1, 2, 3);
//	System.out.println("\t\t 0 \t\t 1 \t\t 2 \t\t 3");
	System.out.println("\t" + players[0].getClass().getSimpleName() +  "\t" + players[1].getClass().getSimpleName()+"\t" + players[2].getClass().getSimpleName() +"\t" + players[3].getClass().getSimpleName());
	System.out.format("%-13s", players[0].getClass().getSimpleName());
	System.out.format("%-13s", players[1].getClass().getSimpleName());
	System.out.format("%-13s", players[2].getClass().getSimpleName());
	System.out.format("%-13s\n", players[3].getClass().getSimpleName());
	System.out.println("");
	while (!this.isGameOver())
	{

	    int starter = this.findStarter();
	    for (int i = 0; i < Game.handSize; i++)
	    {
		this.doTurn(starter);
		starter = this.board.findWinner();
		//System.out.println(this.board);
		this.players[starter].addScore(this.board.getRoundPoints());
		this.board.clear();
	    }
	    // DEBUG
	    for (int i = 0; i < this.players.length; i++)
		//System.out.format("%-13d", this.players[i].getScore());
		System.out.format(this.players[i].getScore() + "\t");
	    System.out.println("");

	    this.newRound();
	}
	System.out.println("Game Over!!!");

    }

    @Override
    public void doTurn(int starter) {

	for (int i = 0; i < this.players.length; i++)
	{
	    int playerI = (i + starter) % (this.players.length);
	    Card current;

	    current = this.players[playerI].play(this.board, this.history);

	    this.board.update(current, playerI);
	    this.history.update(playerI, this.board.getRoundSuit(), current);
	}

    }

    @Override
    public void newRound() {

	this.initDeck();
	this.board.clear();

	for (int i = 0; i < this.players.length; i++)
	    this.players[i].setHand(this.dealCards(i));

	this.history = new History();
    }

    public static void main(String[] args) {

//	System.out.format("%5s, %5s", "abc", "abcd");
	TestGame tg = new TestGame();
	tg.playGame();

    }
}
