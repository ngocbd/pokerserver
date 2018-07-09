package com.fcs.pokerserver;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
/*
 * This class is for testing not for production
 * */
public class GameServer implements MqttCallback {

	private List<Player> listPlayer = new ArrayList<Player>();
	private List<Room> listRoom = new ArrayList<Room>();
	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	Algorithm algorithm = Algorithm.HMAC256("thisstringisverysecret");
	Sender sender;
	public void addPlayer(Player p) {
		this.listPlayer.add(p);

	}
	public void addRoom(Room r) {
		this.listRoom.add(r);

	}
	public Player getPlayerByName(String name)
	{
		return this.listPlayer.stream().filter(x -> name.equals(x.getName())).findFirst().orElse(null);
	}
	public Room getRoomByID(long id)
	{
		return this.listRoom.stream().filter(x -> x.getRoomID()==id).findFirst().orElse(null);
	}
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

	static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
	static final String SERVER_TOPIC = "/pokerserver/server";

	/**
	 * 
	 * MAIN
	 * 
	 */
	public static void main(String[] args) {
		GameServer gameServer = new GameServer();

		System.out.println("GameServer starting...");
		try {
			gameServer.run();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("GameServer started at "+DateTime.now().toLocalDateTime().toString());
	}

	/**
	 * 
	 * run The main functionality of this simple example. Create a MQTT client,
	 * connect to broker, pub/sub, disconnect.
	 * 
	 * @throws MqttException
	 * 
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

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		cause.printStackTrace(System.out);

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		String body = new String(message.getPayload(), Charset.forName("UTF-8"));
		System.out.println("topic:" + topic + " msg:" + new String(message.getPayload()));
		Map<String, String> queryMap = getQueryMap(body);
		String username = queryMap.get("username");
		
		
		/*
		 * Login method over mqtt is not safe . replace with other method ( prefer web )
		 * */
		if (queryMap.get("cmd").equals("login")) {
			
			String password = queryMap.get("password");

			Player p = new Player(username);

			Key key = datastore.newKeyFactory().setKind("users").newKey(p.getName());

			Entity retrievedUser = datastore.get(key);
			if (retrievedUser == null) {
				System.out.println("User : "+username +" not found");
				return;
			}
			if (retrievedUser.getString("password").equals(password)) {
				this.addPlayer(p);
				
				
			    String token = JWT.create()
			        .withIssuer("pokerserver")
			        .withJWTId(username)
			        .sign(algorithm);
			    datastore.put(Entity.newBuilder(retrievedUser).set("token", token).build());
			    
				String sendToClient = "cmd=login&token=" + token;
				this.sender.add("/pokerserver/user/" + username, sendToClient);
			}

			return;
		}
		else
		{
			// check token 
			String token = queryMap.get("token");
			
			try {
			    
			    JWTVerifier verifier = JWT.require(algorithm)
			        .withIssuer("pokerserver")
			        .build(); //Reusable verifier instance
			    DecodedJWT jwt = verifier.verify(token);
			    
			    
			    
			    /*
			     * Create room with mqtt maybe not good option
			     * */
			    if (queryMap.get("cmd").equals("createRoom"))
			    {
			    	Player master = this.getPlayerByName(username);
			    	
			    	//TODO fix with blind level // default 10-20
			    	Room room = new Room(master, BlindLevel.BLIND_10_20);
			    	this.addRoom(room);
			    	room.addPlayer(master);
			    	Key key = datastore.newKeyFactory().setKind("rooms").newKey(room.getRoomID());

			    	 Entity entity = Entity.newBuilder(key)
			    		        .set("master", username)
			    		        .build();
			    	 datastore.put(entity);
			    	String sendToClient = "cmd=room&id=" + room.getRoomID();
			    	this.sender.add("/pokerserver/room/" + room.getRoomID(), sendToClient);
			    	return ;
			    	
			    }
			    /*
			     * Create room with mqtt maybe not good option
			     * */
			    if (queryMap.get("cmd").equals("joinRoom"))
			    {
			    	Player player = this.getPlayerByName(username);
			    	long roomID = Integer.parseInt(queryMap.get("id"));
			    	
			    	
			    	//TODO Check room not exists
			    
			    	Room room = this.getRoomByID(roomID);
			    	
			    	
			    	room.addPlayer(player);
			    	
			    	String sendToClient = "cmd=join&player=" +username;
			    	this.sender.add("/pokerserver/room/" + roomID, sendToClient);
			    	
			    	return ;
			    	
			    }
			    
			    
			    
			    
			} catch (JWTVerificationException exception){
				exception.printStackTrace();
				String sendToClient = "cmd=error&msg=" + exception.getMessage();
		    	myClient.publish("/pokerserver/user/" +username, new MqttMessage(sendToClient.getBytes()));
		    	return;
			}
			
			
		}

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

}