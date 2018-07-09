package com.fsc.pokerserver.test;

import static org.junit.Assert.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Test;

import com.fcs.pokerserver.Sender;

public class GameServerClientTest implements MqttCallback {

	MqttClient myClient;
	MqttConnectOptions connOpt;

	static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
	static final String SERVER_TOPIC = "/pokerserver/server";
	static final String USER_TOPIC = "/pokerserver/user/";
	static final String USERNAME = "daica";
	static final String PASSWORD = "123456";
	static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImRhaWNhIn0.n3ETnk6P6Hw42xjx78iRYGBJc93rbHWCfW3KiZe-LmI";
	String lastUserResponse ="";
	Sender sender;
	@Before
	public void setUp() throws Exception {
		connOpt = new MqttConnectOptions();

		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		

		// Connect to Broker
		try {
			myClient = new MqttClient(BROKER_URL, "pokerclient"+System.currentTimeMillis());
			myClient.setCallback(this);
			myClient.connect(connOpt);
			this.sender= new Sender(myClient);
			
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		int subQoS = 0;
		myClient.subscribe(USER_TOPIC+"#", subQoS);
	}

	@Test
	public void testLogin() {
		sender.add(SERVER_TOPIC, "cmd=login&username="+USERNAME+"&password="+PASSWORD);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(lastUserResponse.startsWith("cmd=login&token="));
		
	}
	
	
	@Test
	public void testCreateRoom() {
		sender.add(SERVER_TOPIC, "cmd=createRoom&username="+USERNAME+"&token="+TOKEN);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(lastUserResponse.startsWith("cmd=room&id="));
		
	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		if(topic.equals(USER_TOPIC+USERNAME))
		{
			lastUserResponse=new String(message.getPayload());
		}
		System.out.println(new String(message.getPayload()));
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

}
