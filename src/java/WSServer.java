
import GameFiles.Card;
import GameFiles.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@ServerEndpoint("/HeartsProject")
public class WSServer
{

    private static ArrayList<Session> clients = new ArrayList();
    private Game g;

    //1. OnOpen
    @OnOpen
    public void onOpen(Session session)
    {
	// print message

	System.out.println(session.getId() + " Has opened a connection");
	clients.add(session);

	this.g = new Game(session);
	this.g.playGame();
	
    }

    //2. OnMessage
    @OnMessage
    public void onMessage(String message, Session session)
    {
	//System.out.println("Message received " + message);
	String c = message.toLowerCase();
	this.g.setClientCard(new Card(c));

    }

    //3. OnClose
    @OnClose
    public void onClose(Session session)
    {
	System.out.println(session.getId() + " Has closed a connection");

	clients.remove(session);
    }
    
    public void playCard(String player, String card) {
	
	JSONObject obj = new JSONObject();
	obj.put("type", "play");
	obj.put("player", player);
	obj.put("card", card);

	try
	{
	    for (Session s : clients)
		s.getBasicRemote().sendText(obj.toString());
	} catch (IOException ex) {
	    Logger.getLogger(WSServer.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

}
