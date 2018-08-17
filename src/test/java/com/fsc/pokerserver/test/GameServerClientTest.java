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

package com.fsc.pokerserver.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fcs.pokerserver.gameserver.MqttServletGameServer;

/*
 * Url to get content
 * create player
 * http://localhost:8080/api/register?username=hbg1&password=123456
 * 
 * player login
 * http://localhost:8080/api/login?username=hbg1&password=123456
 * 
 * create room
 * http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhiZzEifQ.czIr3dIp9wMDzKwDzeun_a8eU8LizqA2urjctiUNT4M&method=put
 * 
 * get list room
 * http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhiZzEifQ.czIr3dIp9wMDzKwDzeun_a8eU8LizqA2urjctiUNT4M&method=get
 * 
 * player join room
 * http://localhost:8080/api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhiZzEifQ.czIr3dIp9wMDzKwDzeun_a8eU8LizqA2urjctiUNT4M&method=join&id=1531887589128
 * 
 * player start game in room
 * http://localhost:8080/api/game?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhiZzEifQ.czIr3dIp9wMDzKwDzeun_a8eU8LizqA2urjctiUNT4M&method=start
 * 
 * game preflop
 * http://localhost:8080/api/game?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhiZzEifQ.czIr3dIp9wMDzKwDzeun_a8eU8LizqA2urjctiUNT4M&method=preflop
 * 
 * bet game
 * http://localhost:8080/api/game?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImNoYXUxIn0.nuXZBVlnVBZPHP0IdQ1YcFDIlaNNwQBcvxibgF8qN94&method=bet&value=25
 * 
 * fold game
 * http://localhost:8080/api/game?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImhiZzEifQ.czIr3dIp9wMDzKwDzeun_a8eU8LizqA2urjctiUNT4M&method=fold
 * 
 * 
 * 
 * List token of players.
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
/**
 * The class to test the Server and the Client in the game.
 * @category com > fcs > pokerserver > test
 * */
@Ignore
public class GameServerClientTest implements MqttCallback {

	String host = "http://localhost:8080/";
	@Before
	public void setUp() throws Exception {
//		MqttServletGameServer mqttServletGameServer = MqttServletGameServer.getInstance();
//		Thread.sleep(2000);
	}
	
	/**
	 * Get content from the url
	 * */
	public String getContentFromUrl(String url)  throws ClientProtocolException, IOException
	{
		Document contentDoc = Jsoup.connect(url).get();
		return contentDoc.body().text();
	}
	
	/**
	 * Get token from Player
	 * */
	public String[] getTokenPlayer(String[] arr) throws ClientProtocolException, IOException
	{
//		String arr[]= {"gio1","hbg1","poke1","agru1","kuki1"};
//		String arr[]= {"loi1","xeng1","thuy1","hoan1","lam1"};
//		String arr[]= {"tit1","thoa1","hung1","ngoc1","tuan1"};
//		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		String token[] = new String[arr.length];
	
		for(int i = 0;i<arr.length;i++)
		{
			Document tokenDoc = Jsoup.connect(host+"api/login?username="+arr[i]+"&password=123456").get();
			token[i] = tokenDoc.body().text();
		}
//		System.out.println("length of token: "+token.length);
		return token;
	}
	
	
	/**
	 * Check value is Numeric
	 * */
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	/**
	 * Create player
	 * */
	@Test@Ignore
	public void testCreatePlayer()  throws IOException, ClientProtocolException{
//		String arr[]= {"gio1","hbg1","poke1","agru1","kuki1"};
//		String arr[]= {"loi1","xeng1","thuy1","hoan1","lam1"};
//		String arr[]= {"tit1","thoa1","hung1","ngoc1","tuan1"};
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};

		for(int i=0;i<arr.length;i++)
		{
			String url = host+"api/register?username="+arr[i]+"&password=123456";
			this.getContentFromUrl(url);
		}
		
	}

	
	/**
	 * Create player but player is exist
	 * */
	@Test(expected = AssertionError.class)@Ignore
	public void testCreatePlayerExist()  throws IOException, ClientProtocolException{
		String username = "hbg1";
		String url = host+"api/register?username="+username+"&password=123456";
		assertEquals(this.getContentFromUrl(url), username);
		
	}
	
	
	/**
	 * Player login
	 * */
	@Test @Ignore
	public void testLogin() throws ClientProtocolException, IOException  {
//		create player array to get token array of player to join room.
//		String arr[]= {"gio1","hbg1","poke1","agru1","kuki1"};
//		String arr[]= {"loi1","xeng1","thuy1","hoan1","lam1"};
//		String arr[]= {"tit1","thoa1","hung1","ngoc1","tuan1"};
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};

		for(int i = 0;i<arr.length;i++)
		{
			String url = host+"api/login?username="+arr[i]+"&password=123456";
			this.getContentFromUrl(url);
		}
	}
	
	/**
	 * Player login error
	 * */
	@Test(expected = AssertionError.class) @Ignore
	public void testLoginWithPlayerNotRegister() throws ClientProtocolException, IOException  {

		String username = "mai";
		String url = host+"api/login?username="+username+"&password=123456";
		this.getContentFromUrl(url);
		assertEquals(this.getContentFromUrl(url), "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImFncnUxIn0._NXg4_vRYyu8ntaHROfVdu8snHxGirmzNlMav-96fZ4");
	}
	
	/**
	 * Create room
	 * */
	@Test @Ignore
	public void testCreateRoom()   throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		//create room
		String url = host+"api/room?token="+token[0]+"&method=put";
		this.getContentFromUrl(url);
	}
	
	
	/**
	 * Create room With Token not login or Not Exist
	 * */
	@Test(expected = AssertionError.class)@Ignore
	public void testCreateRoomTokenError()   throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		
		//Player login
		for(int i = 0;i<arr.length;i++)
		{
			String url = host+"api/login?username="+arr[i]+"&password=123456";
			this.getContentFromUrl(url);
		}
		
		//create room
		// Player have token that Player dont login
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InRpdDEifQ.qpOQ6sLLzhdyX-bl7H4cEi-le-cz2QuZe2ZhVOaH-Ls";
		String url = host+"api/room?token="+token+"&method=put";
		assertEquals(isNumeric(this.getContentFromUrl(url)), true);	
	}
	
	
	/**
	 * Join room.
	 * */
	@Test @Ignore
	public void testJoinRoom()  throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		//create room
		String urlCreateRoom = host+"api/room?token="+token[0]+"&method=put";
		String roomId = this.getContentFromUrl(urlCreateRoom);
		
		//join room
		System.out.println("length of token: "+token.length);
		
		for(int i=1;i<token.length;i++)
		{
			System.out.println("token at "+i+": "+token[i]);
			String url = host+"api/room?token="+token[i]+"&method=join&id="+roomId;
			System.out.println("Url join: "+url);
//			this.getContentFromUrl(url);
			Document d = Jsoup.connect(url).get();
			System.out.println("Player"+i+" join game: "+d.text());
		}
	
	}
	
	/**
	 * Join room With token error.
	 * */
	@SuppressWarnings("deprecation")
	@Test(expected = AssertionError.class)@Ignore
	public void testJoinRoomWithTokenError()  throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
	
		//create room
		String urlCreateRoom = host+"api/room?token="+token[0]+"&method=put";
		String id = this.getContentFromUrl(urlCreateRoom);
		
		//join room
		String titToken= "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6InRpdDEifQ.qpOQ6sLLzhdyX-bl7H4cEi-le-cz2QuZe2ZhVOaH-Ls";
		String urlJoin = host+"api/room?token="+titToken+"&method=join&id="+id;
		this.getContentFromUrl(urlJoin);
		float lengthUrlJoinFirst = this.getContentFromUrl(urlJoin).length();
		float sumLengthUrlJoin = 0;
		for(int i=1;i<token.length;i++)
		{
			String url = host+"api/room?token="+token[i]+"&method=join&id="+id;
			this.getContentFromUrl(url);
			sumLengthUrlJoin+=this.getContentFromUrl(url).length();
		}
		float value = sumLengthUrlJoin/(token.length-1);
		assertEquals(value, lengthUrlJoinFirst);
		
	}
	

	/**
	 * Join room with room id error.
	 * */
	@Test(expected = AssertionError.class)@Ignore
	public void testJoinRoomWithRoomIdError()  throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		
		//create room to get room id
		//room id error
		String id = "1532594321491";
		int sumOfLength=0;
		//join room
		for(int i=1;i<token.length;i++)
		{
			String url = host+"api/room?token="+token[i]+"&method=join&id="+id;
			this.getContentFromUrl(url);
			sumOfLength+= this.getContentFromUrl(url).length();
		}
		int value = sumOfLength/token.length;
		assertNotEquals(value, 6);
	}
	
	/**
	 * Get list of room
	 * */
	@Test @Ignore
	public void testGetListRooms()  throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		
		//create room
		String urlCreateRoom = host+"api/room?token="+token[0]+"&method=put";
		String id = this.getContentFromUrl(urlCreateRoom);
		
		//join room
		for(int i=1;i<token.length;i++)
		{
			String url = host+"api/room?token="+token[i]+"&method=join&id="+id;
			this.getContentFromUrl(url);
		}
		
		String urlGetListRoom = host+"api/room?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImdpbzEifQ.LUgFtexXVwBXQDPi3acL02tdpXZ4dtlNW7E700jilkI&method=get";
		this.getContentFromUrl(urlGetListRoom);
	}
	
	/**
	 * Start Game in Room
	 * */
	@Test@Ignore
	public void testStartGame()  throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		//create room
		String urlCreateRoom = host+"api/room?token="+token[0]+"&method=put";
		String roomId = this.getContentFromUrl(urlCreateRoom);
		
		//join room
		for(int i=1;i<token.length;i++)
		{
			String url = host+"api/room?token="+token[i]+"&method=join&id="+roomId;
			this.getContentFromUrl(url);
		}
		
		//startgame
		String startGame = host+"api/game?token="+token[0]+"&method=start";
		this.getContentFromUrl(startGame);

	}
	
	
	/**
	 * Start Game in Room but player dont login
	 * */
	@Test(expected = AssertionError.class)@Ignore
	public void testStartGameWithPlayerNotLogin() throws ClientProtocolException, IOException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		//create room
		String urlCreateRoom = host+"api/room?token="+token[0]+"&method=put";
		String roomId = this.getContentFromUrl(urlCreateRoom);
		
		//join room
		for(int i=1;i<token.length;i++)
		{
			String url = host+"api/room?token="+token[i]+"&method=join&id="+roomId;
			this.getContentFromUrl(url);
		}
		
		//startgame
		/*
		 * Gio don't login acc.
		 * gio1: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImdpbzEifQ.LUgFtexXVwBXQDPi3acL02tdpXZ4dtlNW7E700jilkI
		 * */
		
		String gioToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwb2tlcnNlcnZlciIsImp0aSI6ImdpbzEifQ.LUgFtexXVwBXQDPi3acL02tdpXZ4dtlNW7E700jilkI";
		String startGame = host+"api/game?token="+gioToken+"&method=start";
		this.getContentFromUrl(startGame);
	
	}
	
	/**
	 * Game Preflop
	 * */
	@Test @Ignore
	public void testGamePreflop()  throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		//create room
		String urlCreateRoom = host+"api/room?token="+token[0]+"&method=put";
		String roomId = this.getContentFromUrl(urlCreateRoom);
		
		//join room
		for(int i=1;i<token.length;i++)
		{
			String url = host+"api/room?token="+token[i]+"&method=join&id="+roomId;
			this.getContentFromUrl(url);
		}
		
		//startgame
		String startGame = host+"api/game?token="+token[0]+"&method=start";
		this.getContentFromUrl(startGame);
		
		//preflop
		String preFlop = host+"api/game?token="+token[0]+"&method=preflop";
		this.getContentFromUrl(preFlop);
	}
	
	
	
	/**
	 * Game Flop
	 * */
	@Test@Ignore
	public void testGameFlop()  throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		//create room
		String urlCreateRoom = host+"api/room?token="+token[0]+"&method=put";
		String roomId = this.getContentFromUrl(urlCreateRoom);
		
		//join room
		for(int i=1;i<token.length;i++)
		{
			String url = host+"api/room?token="+token[i]+"&method=join&id="+roomId;
			this.getContentFromUrl(url);
		}
		
		//startgame
		String startGame = host+"api/game?token="+token[0]+"&method=start";
		this.getContentFromUrl(startGame);
		
		//preflop
		String preFlop = host+"api/game?token="+token[0]+"&method=preflop";
		this.getContentFromUrl(preFlop);
		
		//Bet
		String utg = host+"api/game?token="+token[3]+"&method=bet&value=20";
		this.getContentFromUrl(utg);
		String ngheBet = host+"api/game?token="+token[4]+"&method=bet&value=20";
		this.getContentFromUrl(ngheBet);
		String toanBet = host+"api/game?token="+token[0]+"&method=bet&value=20";
		this.getContentFromUrl(toanBet);
		String danhBet = host+"api/game?token="+token[1]+"&method=bet&value=10";
		this.getContentFromUrl(danhBet);
		
		//flop
		String flop = host+"api/game?token="+token[0]+"&method=flop";
		this.getContentFromUrl(flop);
		
	}
	
	
	/**
	 * Game Turn
	 * */
	@Test@Ignore
	public void testGameTurn()  throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		//create room
		String urlCreateRoom = host+"api/room?token="+token[0]+"&method=put";
		String roomId = this.getContentFromUrl(urlCreateRoom);
		
		//join room
		for(int i=1;i<token.length;i++)
		{
			String url = host+"api/room?token="+token[i]+"&method=join&id="+roomId;
			this.getContentFromUrl(url);
		}
		
		//startgame
		String startGame = host+"api/game?token="+token[0]+"&method=start";
		this.getContentFromUrl(startGame);
		
		//preflop
		String preFlop = host+"api/game?token="+token[0]+"&method=preflop";
		this.getContentFromUrl(preFlop);
		
		//Bet
		String utgChauBet = host+"api/game?token="+token[3]+"&method=bet&value=20";
		this.getContentFromUrl(utgChauBet);
		String ngheBet = host+"api/game?token="+token[4]+"&method=bet&value=20";
		this.getContentFromUrl(ngheBet);
		String toanBet = host+"api/game?token="+token[0]+"&method=bet&value=20";
		this.getContentFromUrl(toanBet);
		String danhBet = host+"api/game?token="+token[1]+"&method=bet&value=10";
		this.getContentFromUrl(danhBet);
		
		//flop
		String flop = host+"api/game?token="+token[0]+"&method=flop";
		this.getContentFromUrl(flop);
		

		//Bet and Fold
		String sbDanhBet1 = host+"api/game?token="+token[1]+"&method=bet&value=20";
		this.getContentFromUrl(sbDanhBet1);
		String bbLinhBet1 = host+"api/game?token="+token[2]+"&method=bet&value=20";
		this.getContentFromUrl(bbLinhBet1);
		String utgChauBet1 = host+"api/game?token="+token[3]+"&method=bet&value=20";
		this.getContentFromUrl(utgChauBet1);
		String ngheBet1 = host+"api/game?token="+token[4]+"&method=fold";
		this.getContentFromUrl(ngheBet1);
		String dealerToanBet1 = host+"api/game?token="+token[0]+"&method=bet&value=20"; 
		this.getContentFromUrl(dealerToanBet1);
		
		//turn
		String turn = host+"api/game?token="+token[1]+"&method=turn";
		this.getContentFromUrl(turn);
	}
	
	
	/**
	 * Game River
	 * */
	@Test//@Ignore
	public void testGameRiver()  throws IOException, ClientProtocolException{
		//Array of Players
		String arr[]= {"toan1","danh1","linh1","chau1","nghe1"};
		// Players login and return Array Token of Players
		String token[] = this.getTokenPlayer(arr);
		
		//Create room
		String urlCreateRoom = host+"api/room?token="+token[0]+"&method=put";
		String roomId = this.getContentFromUrl(urlCreateRoom);
		
		//Join room
		for(int i=1;i<token.length;i++)
		{
			String url = host+"api/room?token="+token[i]+"&method=join&id="+roomId;
			this.getContentFromUrl(url);
		}
		
		//Start game
		String startGame = host+"api/game?token="+token[0]+"&method=start";
		this.getContentFromUrl(startGame);
//		System.out.println("start Game: "+this.getContentFromUrl(startGame));
		
		//Preflop
		String preFlop = host+"api/game?token="+token[0]+"&method=preflop";
		this.getContentFromUrl(preFlop);
		
		//Bet
		String utgChauBet = host+"api/game?token="+token[3]+"&method=bet&value=20";
		this.getContentFromUrl(utgChauBet);
		String ngheBet = host+"api/game?token="+token[4]+"&method=bet&value=20";
		this.getContentFromUrl(ngheBet);
		String toanBet = host+"api/game?token="+token[0]+"&method=bet&value=20";
		this.getContentFromUrl(toanBet);
		String danhBet = host+"api/game?token="+token[1]+"&method=bet&value=10";
		this.getContentFromUrl(danhBet);
		
		//Flop
		String flop = host+"api/game?token="+token[0]+"&method=flop";
		this.getContentFromUrl(flop);
		

		//Bet and Fold
		String sbDanhBet2 = host+"api/game?token="+token[1]+"&method=bet&value=20";
		this.getContentFromUrl(sbDanhBet2);
		String bbLinhBet2 = host+"api/game?token="+token[2]+"&method=bet&value=20";
		this.getContentFromUrl(bbLinhBet2);
		String utgChauBet2 = host+"api/game?token="+token[3]+"&method=bet&value=20";
		this.getContentFromUrl(utgChauBet2);
		String ngheBet2 = host+"api/game?token="+token[4]+"&method=fold";
		this.getContentFromUrl(ngheBet2);
		String dealerToanBet2 = host+"api/game?token="+token[0]+"&method=bet&value=20"; 
		this.getContentFromUrl(dealerToanBet2);
		
		//Turn
		String turn = host+"api/game?token="+token[0]+"&method=turn";
		this.getContentFromUrl(turn);
		
		//Bet and Fold
		String sbDanhBet3 = host+"api/game?token="+token[1]+"&method=bet&value=20";
		this.getContentFromUrl(sbDanhBet3);
		String bbLinhBet3 = host+"api/game?token="+token[2]+"&method=bet&value=20";
		this.getContentFromUrl(bbLinhBet3);
		String utgChauBet3 = host+"api/game?token="+token[3]+"&method=bet&value=20";
		this.getContentFromUrl(utgChauBet3);
		String ngheBet3 = host+"api/game?token="+token[4]+"&method=fold";
		this.getContentFromUrl(ngheBet3);
		String dealerToanBet3 = host+"api/game?token="+token[0]+"&method=bet&value=20"; 
		this.getContentFromUrl(dealerToanBet3);
		
		//River
		String river = host+"api/game?token="+token[0]+"&method=river";
		this.getContentFromUrl(river);
		
		//End game
		String endGame = host+"api/game?token="+token[0]+"&method=end";
		this.getContentFromUrl(endGame);
	}
	

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stubs
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

}
