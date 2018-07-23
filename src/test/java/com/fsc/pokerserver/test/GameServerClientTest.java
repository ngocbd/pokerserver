package com.fsc.pokerserver.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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


import com.fcs.pokerserver.gameserver.Sender;

import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import com.google.api.client.repackaged.com.google.common.base.Throwables;



/*
 * create player
 * http://localhost:8080/api/register?username=habogay&password=123456
 * 
 * player login
 * http://localhost:8080/api/login?username=habogay&password=123456
 * 
 * create room
 * http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImRhaWNhIn0.n3ETnk6P6Hw42xjx78iRYGBJc93rbHWCfW3KiZe-LmI&method=put
 * 
 * get list room
 * http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImRhaWNhIn0.n3ETnk6P6Hw42xjx78iRYGBJc93rbHWCfW3KiZe-LmI&method=get
 * 
 * player join room
 * http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhhYm9nYXkifQ.AkzXqdFjQRpM09hngtx2aP7jyX3OLbdJWPItAPjmBTg&method=join&id=1531887589128
 * 
 * String arr[]= {"gio1","hbg1","poke1","agru1","kuki1"};
 * gio1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImdpbzEifQ.LUgFtexXVwBXQDPi3acL02tdpXZ4dtlNW7E700jilkI
 * hbg1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhiZzEifQ.czIr3dIp9wMDzKwDzeun_a8eU8LizqA2urjctiUNT4M
 * poke1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InBva2UxIn0.3Hk9VnA32dhgk5sA_LnKENnyTkJ4pQIL1GF1HDetEDc
 * agru1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImFncnUxIn0._NXg4_vRYyu8ntaHROfVdu8snHxGirmzNlMav-96fZ4
 * kuki1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6Imt1a2kxIn0.1ZOy2qKYzZBEutMHFQtxmKoeKRv8DL9Pk90RQq_boOY
 * 
 * String arr[]= {"loi1","xeng1","thuy1","hoan1","lam1"};
 * loi1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImxvaTEifQ.jUv01rg6O6SPHNEVhf1a_J-MGcbpmCpWDH4IClM0RvM
 * xeng1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InhlbmcxIn0.HkyHGW5Ysq2gPml9fip2fWhzTCBdIL1AZKPMUHxTiUk
 * thuy1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InRodXkxIn0.TGcPMGnkg1ju9TFYGNWZflUWUe0CR7Y0BOi1dCuFVxY
 * hoan1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhvYW4xIn0.w5oLx-cK4fZAtNCkbTl76EVBdU9jTVOvO8x-IyGsuak
 * lam1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImxhbTEifQ.t3Acur_pZd3oyWkOjggtdhrfr7_J0iMDRf1wKY_zXuE
 * 
 * String arr[]= {"tit1","thoa1","hung1","ngoc1","tuan1"};
 * tit1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InRpdDEifQ.qpOQ6sLLzhdyX-bl7H4cEi-le-cz2QuZe2ZhVOaH-Ls
 * thoa1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InRob2ExIn0.zI2dgBUrByb4jlrr7e0ckl5D6ap2fETPqJyPcQn_a4Y
 * hung1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6Imh1bmcxIn0.0retLNeTFIaBkiuEwxTHLhxvDODY7XgNcSvDIpoZTRc
 * ngoc1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6Im5nb2MxIn0.x4822cSRQYEhShAGrhMmCQgngOF1gQAcrHh-MQ4yPmI
 * tuan1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InR1YW4xIn0.qmQTFu3pyjSp6NpHPGpMyKgS07h0qV2LWK5YSwrb24I
 * 
 * String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
 * toan1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InRvYW4xIn0.o3GtRxDLak4IOOIZn6bwjn1YhUzzszUC9FhP4uidqrE
 * danh1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImRhbmgxIn0.8OSrcMXE75vDSAr3pd-RfRDaeippbEUqVCMwmcLgl14
 * linh1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImxpbmgxIn0.cOBCa1n6q3QYuUVFgEJBPcRxuKwnJtgV5j_kO3_qBDo
 * chau1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImNoYXUxIn0.nuXZBVlnVBZPHP0IdQ1YcFDIlaNNwQBcvxibgF8qN94
 * nghe1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6Im5naGUxIn0.3Bx_3efkx4UYEuHwVz2CCSNEIS6i6qeRN89o6xafAaY
 * 
 * **/

public class GameServerClientTest implements MqttCallback {

	
	@Before
	public void setUp() throws Exception {
//		MqttServletGameServer mqttServletGameServer = MqttServletGameServer.getInstance();
//		Thread.sleep(2000);
	}
	
	/*
	 * Get content from the url
	 * */
	public String getContentFromUrl(String url)  throws ClientProtocolException, IOException
	{
		String content="";
		
		HttpClient client = new DefaultHttpClient();
		
		HttpGet request = new HttpGet(url);
		HttpResponse response  = client.execute(request);
		
		DataInputStream rd = new DataInputStream(
				response.getEntity().getContent());
		
		
		content = rd.readLine();
		return content;
	}
	
	/*
	 * Get token from Player
	 * */
	public String[] getTokenPlayer() throws ClientProtocolException, IOException
	{
//		String arr[]= {"gio1","hbg1","poke1","agru1","kuki1"};
//		String arr[]= {"loi1","xeng1","thuy1","hoan1","lam1"};
//		String arr[]= {"tit1","thoa1","hung1","ngoc1","tuan1"};
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};

		String token[]= new String[5];
	
		for(int i = 0;i<arr.length;i++)
		{
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet("http://localhost:8080/api/login?username="+arr[i]+"&password=123456");
//			HttpGet request = new HttpGet("http://localhost:8080/api/login?username=hbg1&password=123456");
			HttpResponse response  = client.execute(request);
			
			DataInputStream rd = new DataInputStream(
					response.getEntity().getContent());
			
			token[i]=rd.readLine();
		}
//		System.out.println("length of token: "+token.length);
		return token;
	}
	
	/*
	 * Create player
	 * */
//	@Test
//	public void testCreatePlayer()  throws IOException, ClientProtocolException{
////		String arr[]= {"gio1","hbg1","poke1","agru1","kuki1"};
////		String arr[]= {"loi1","xeng1","thuy1","hoan1","lam1"};
////		String arr[]= {"tit1","thoa1","hung1","ngoc1","tuan1"};
//		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
//
//		for(int i=0;i<arr.length;i++)
//		{
//			String url = "http://localhost:8080/api/register?username="+arr[i]+"&password=123456";
//			this.getContentFromUrl(url);
//		}
//		
//	}

	
	
	/*
	 * Player login
	 * */
	@Test
	public void testLogin() throws ClientProtocolException, IOException  {
//		create player array to get token array of player to join room.
//		String arr[]= {"gio1","hbg1","poke1","agru1","kuki1"};
//		String arr[]= {"loi1","xeng1","thuy1","hoan1","lam1"};
//		String arr[]= {"tit1","thoa1","hung1","ngoc1","tuan1"};
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};

		for(int i = 0;i<arr.length;i++)
		{
			String url = "http://localhost:8080/api/login?username="+arr[0]+"&password=123456";
			this.getContentFromUrl(url);
		}
	}
	
	/*
	 * Create room
	 * */
//	@Test
//	public void testCreateRoom()   throws IOException, ClientProtocolException{
//		try {
//			String url = "http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhiZzEifQ.czIr3dIp9wMDzKwDzeun_a8eU8LizqA2urjctiUNT4M&method=put";
//			System.out.println(this.getContentFromUrl(url));
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
//		
//	}
	
	
	/*
	 * create room and join room.
	 * */
	@Test
	public void testJoinRoom()  throws IOException, ClientProtocolException{
//		String arr[] = {"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImdpbzEifQ.LUgFtexXVwBXQDPi3acL02tdpXZ4dtlNW7E700jilkI","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhiZzEifQ.czIr3dIp9wMDzKwDzeun_a8eU8LizqA2urjctiUNT4M","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InBva2UxIn0.3Hk9VnA32dhgk5sA_LnKENnyTkJ4pQIL1GF1HDetEDc","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImFncnUxIn0._NXg4_vRYyu8ntaHROfVdu8snHxGirmzNlMav-96fZ4","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6Imt1a2kxIn0.1ZOy2qKYzZBEutMHFQtxmKoeKRv8DL9Pk90RQq_boOY"};
		String token[] = this.getTokenPlayer();
//		for(int i=0;i<token.length;i++)
//		{
//			System.out.println(token[i]);
//		}
//		System.out.println("Length: "+token.length);
		String urlCreateRoom = "http://localhost:8080/api/room?token="+token[0]+"&method=put";
		String id = this.getContentFromUrl(urlCreateRoom);
		for(int i=0;i<token.length;i++)
		{
			String url = "http://localhost:8080/api/room?token="+token[i]+"&method=join&id="+id;
			this.getContentFromUrl(url);
		}
		
	}
	
	/*
	 * Get list of room
	 * */
	@Test
	public void testGetListRooms()  throws IOException, ClientProtocolException{
		String token[] = this.getTokenPlayer();
		String urlGetListRoom = "http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImdpbzEifQ.LUgFtexXVwBXQDPi3acL02tdpXZ4dtlNW7E700jilkI&method=get";
//		System.out.println(this.getContentFromUrl(urlGetListRoom));
		this.getContentFromUrl(urlGetListRoom);
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
