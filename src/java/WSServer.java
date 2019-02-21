import GameFiles.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
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
	
	this.g = new Game();
	
	
	JSONObject obj = new JSONObject();
	obj.put("type", "init");
	obj.put("hand", g.getHand());
//	obj.put("hand", "2c3c9c2dqdad6sjsas3h8hthah");
        
	// send client a message.           
        try 
        {
//	    session.getBasicRemote().sendText(" Connection established");
	    session.getBasicRemote().sendText(obj.toString());
        } 
        catch (IOException ex) {
            Logger.getLogger(WSServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //2. OnMessage
    @OnMessage
    public void onMessage(String message, Session session)
    {
        System.out.println("Message received " + message);
        try {
            for(Session s:clients)
                s.getBasicRemote().sendText(" " + message);
        } catch (IOException ex) {
            Logger.getLogger(WSServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    //3. OnClose
    @OnClose
    public void onClose(Session session)
    {
        System.out.println(session.getId() + " Has closed a connection");
        
        clients.remove(session);
    }
           
      
    
}

