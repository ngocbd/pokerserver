/*
The MIT License (MIT)
Copyright (c) 2018 by habogay
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.fcs.pokerserver.gameserver;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.joda.time.DateTime;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;
import com.fcs.pokerserver.events.GameAction;
import com.fcs.pokerserver.events.GameEvent;
import com.fcs.pokerserver.events.PlayerAction;
import com.fcs.pokerserver.events.PlayerEvent;
import com.fcs.pokerserver.events.RoomAction;
import com.fcs.pokerserver.events.RoomEvent;
import com.fcs.pokerserver.events.RoomListener;
import com.fsc.pokerserver.web.RoomServlet;
import com.fsc.pokerserver.web.GameServlet;
import com.fsc.pokerserver.web.LoginServlet;
import com.fsc.pokerserver.web.ObjectifyWebFilter;
import com.fsc.pokerserver.web.PokerTokenFilter;
import com.fsc.pokerserver.web.RegisterServlet;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

/**
 * The class is tested. It's not a production.
 * @category com > fcs > pokerserver > gameserver
 * */
public class MqttServletGameServer implements MqttCallback, RoomListener {

	
	private static  MqttServletGameServer instance =null;
	
	static Logger logger = Logger.getLogger(MqttServletGameServer.class.getName());
	
	static 
	{
		
		final InputStream inputStream = MqttServletGameServer.class.getResourceAsStream("logging.properties");
		try
		{
		    LogManager.getLogManager().readConfiguration(inputStream);
		}
		catch (final IOException e)
		{
		    Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
		    Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}
	
	private  MqttServletGameServer() {
		ServletHolder loginServlet = new ServletHolder(LoginServlet.class);
		ServletHolder registerServlet = new ServletHolder(RegisterServlet.class);
		ServletHolder roomServlet = new ServletHolder(RoomServlet.class);
		ServletHolder gameServlet = new ServletHolder(GameServlet.class);

		
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        
        NCSARequestLog requestLog = new NCSARequestLog();
        String logFileName=System.getProperty("java.io.tmpdir")+"pokerserver-logs-yyyy_mm_dd.request.log";
        logger.fine("Request log:"+logFileName);
        
        
        requestLog.setFilename(logFileName);
        requestLog.setFilenameDateFormat("yyyy_MM_dd");
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(true);
        requestLog.setLogCookies(false);
        requestLog.setLogTimeZone("GMT");
        
        context.addFilter(ObjectifyWebFilter.class, "/*",
                EnumSet.of(DispatcherType.REQUEST));
        
        context.addFilter(PokerTokenFilter.class, "/api/room",EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(PokerTokenFilter.class, "/api/game",EnumSet.of(DispatcherType.REQUEST));
        
        
        
        context.addServlet(loginServlet, "/api/login");
        context.addServlet(registerServlet, "/api/register");
        context.addServlet(roomServlet, "/api/room");
        context.addServlet(gameServlet, "/api/game");
        
        
        logger.warning("MqttServletGameServer starting..."+ ManagementFactory.getRuntimeMXBean().getName());
        try {
			server.start();
			try {
				this.run();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.warning("MqttServletGameServer started at "+DateTime.now().toLocalDateTime().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Singleton pattern to get the MqttServletGameServer instance. 
	 * @return MqttServletGameServer instance
	 * */
	public static MqttServletGameServer getInstance()
	{
		
		
		if(instance==null)  
		{
			instance = new MqttServletGameServer();
		}
		
		return instance;
	}
	
	/**
	 * The Main method
	 * */
	public static void main(String[] args) {
		MqttServletGameServer mqttServletGameServer = MqttServletGameServer.getInstance(); 
	}
	
	private List<Player> listPlayer = new ArrayList<Player>();
	private List<Room> listRoom = new ArrayList<Room>();
	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	Algorithm algorithm = Algorithm.HMAC256("thisstringisverysecret");
	Sender sender;
	
	/**
	 * The the Player into the ListPlayer.
	 * @param Player p
	 * */
	public void addPlayer(Player p) {
		this.getListPlayer().add(p);

	}
	
	/**
	 * The method to add the room into the RoomListener.
	 * @param Room r
	 * */
	public void addRoom(Room r) {
		this.getListRoom().add(r);
		r.addRoomListener(this);
	}
	
	/**
	 * The method to get the Name of the Player.
	 * @param String name
	 * @return Player playerName
	 * */
	public Player getPlayerByName(String name)
	{
		return this.getListPlayer().stream().filter(x -> name.equals(x.getName())).findFirst().orElse(null);
	}
	
	/**
	 * The method to get the Id of the Room.
	 * @param long id
	 * @return Room roomId
	 * */
	public Room getRoomByID(long id)
	{
		return this.getListRoom().stream().filter(x -> x.getRoomID()==id).findFirst().orElse(null);
	}
	
	/**
	 * The method to get the Query of the Map.
	 * @param String query
	 * @return Map<String, String> map
	 * */
	public static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}
		return map;
	}

	MqttClient myClient;
	MqttConnectOptions connOpt;

	static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
	static final String SERVER_TOPIC = "/pokerserver/server";


	/**
	 * 
	 * The simple example run the main function. Create a MQTT client. Connect  the Broker. Subscribe the server topic.
	 * @throws MqttException
	 */
	public void run() throws MqttException {

		connOpt = new MqttConnectOptions();

		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);

		// Connect to Broker
		try {
			myClient = new MqttClient(BROKER_URL, "pokerserver"+System.currentTimeMillis(), new MemoryPersistence());
			myClient.setCallback(this);
			myClient.connect(connOpt);
			this.sender= new Sender(myClient);
			
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		int subQoS = 0;
		myClient.subscribe(SERVER_TOPIC, subQoS);

	}

	/**
	 * The method to print the exception.
	 * @param Throwable cause
	 * */
	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		cause.printStackTrace(System.out);

	}
	
	/**
	 * The callback function print the messagne when it's called.
	 * @param String topic, MqttMessage message 
	 * */
	public void processingMessage(String topic, MqttMessage message) throws Exception
	{
		// TODO Auto-generated method stub
				String body = new String(message.getPayload(), Charset.forName("UTF-8"));
				System.out.println("topic:" + topic + " msg:" + body);			
				
	}
	
	/**
	 * Override the messageArrived method. 
	 * @param String topic, MqttMessage message
	 * */
	@Override
	public void messageArrived(String topic, MqttMessage message) {
		try {
			processingMessage(topic, message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * The method to get the list of the room.
	 * @return List<Room> listRoom
	 * */
	public List<Room> getListRoom() {
		return listRoom;
	}
	
	/**
	 * The method to set the room list.
	 * @param List<Room> listRoom
	 * */
	public void setListRoom(List<Room> listRoom) {
		this.listRoom = listRoom;
	}
	
	/**
	 * The method to get the list of the player.
	 * @return List<Player> listPlayer
	 * */
	public List<Player> getListPlayer() {
		return listPlayer;
	}
	
	/**
	 * The method to set the player list.
	 * @param List<Player> listPlayer
	 * */
	public void setListPlayer(List<Player> listPlayer) {
		this.listPlayer = listPlayer;
	}
	
	
	/**
	 * Override the actionPerformed to push the message to the MqttServer
	 * @param RoomEvent event
	 * */
	@Override
	public void actionPerformed(RoomEvent event) {
		logger.log(Level.SEVERE,event.toString() );
		
		String content = "cmd="+event.getAction()+"&roomid="+event.getSource().getRoomID();
		if(event.getAction()==RoomAction.GAMEACTION)
		{
			GameEvent ge = (GameEvent) event.agruments.get("gameevent");
			content+="&gameEvent="+ge.getAction()+"&gameid="+ge.getSource().getId();
			if(ge.getAction()==GameAction.PLAYEREVENT)
			{
				PlayerEvent pe = (PlayerEvent) ge.agruments.get("playerEvent");
				
				if(pe.getAction()==PlayerAction.BET)
				{
					long amount = (long) pe.agruments.get("amount");
					Player p = pe.getSource();
					content+="&pid="+p.getId()+"&playeraction=bet&amount="+amount;
				}
				if(pe.getAction()==PlayerAction.FOLD)
				{
					Player p = pe.getSource();
					content+="&pid="+p.getId()+"&playeraction=fold";
				}
				if(pe.getAction()==PlayerAction.CHECK)
				{
					Player p = pe.getSource();
					content+="&pid="+p.getId()+"&playeraction=check";
				}
				if(pe.getAction()==PlayerAction.CALL)
				{
					Player p = pe.getSource();
					content+="&pid="+p.getId()+"&playeraction=call";
				}
			}
			if(ge.getAction()==GameAction.ENDED)
			{
				content +="&playerwin="+ge.agruments.get("playerwin")+"&rank="+ge.agruments.get("rank")+"&besthand="+ge.agruments.get("besthand");
			}
		}else if(event.getAction()==RoomAction.PLAYERJOINEDROOM)
		{
			Player p = (Player) event.agruments.get("player");
			content+="&pid="+p.getId();
			
		}
		
		this.sender.add(MqttServletGameServer.SERVER_TOPIC+"/room/"+event.getSource().getRoomID(), content);
		
	}
	

}