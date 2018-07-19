package com.fsc.pokerserver.test;

import static org.junit.Assert.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fcs.pokerserver.Sender;

/*
 * http://localhost:8080/api/register?username=habogay&password=123456
 * http://localhost:8080/api/login?username=habogay&password=123456
 * http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImRhaWNhIn0.n3ETnk6P6Hw42xjx78iRYGBJc93rbHWCfW3KiZe-LmI&method=put
 * http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImRhaWNhIn0.n3ETnk6P6Hw42xjx78iRYGBJc93rbHWCfW3KiZe-LmI&method=get
 * http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhhYm9nYXkifQ.AkzXqdFjQRpM09hngtx2aP7jyX3OLbdJWPItAPjmBTg&method=join&id=1531887589128
 * 
 * 
 * 
 * **/
@Ignore
public class GameServerClientTest implements MqttCallback {

	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testLogin() {
		
		
	}
	
	@Test
	public void testJoinRoom() {
		
		
	}
	
	@Test
	public void testCreateRoom() {
		
	}
	

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

}
