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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Deck;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

/**
 * The class to test the Player's position in the game.
 * @category com > fcs > pokerserver > test
 * */
@Ignore
public class PositionPlayerGameTest {
	Player master;
	Room room;

	@Before
	public void setUp() throws Exception {
		master = new Player("Room master");
		room = new Room(master, BlindLevel.BLIND_10_20);
		
	}

	/*--------------------- Dealer -----------------------*/
	
	/**
	 * set player2 is Dealer for Game
	 * */
	@Test
	public void testDealer() {
		Game game = room.createNewGame();

		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		
		game.setDealer(player2);
		
		game.startGame();

		assertSame("Same Player", game.getDealer(), player2);
	}

	/**
	 * set other player(player5) is Dealer for Game
	 * */
	@Test
	public void testDealerOtherPlayer() {
		Game game = room.createNewGame();

		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		Player player5 = new Player();
		game.addPlayer(player5);
		game.setDealer(player5);
		
		game.startGame();

		assertSame("New Dealer", game.getDealer(), player5);
	}
	
	/**
	 * set the player(player2) is Dealer. But the player(player2) isn't add in game.
	 * */
	@Test(expected = AssertionError.class)
	public void testDealerNotIngame() {
		Game game = room.createNewGame();

		Player player2 = new Player();
//		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		
		game.setDealer(player2);
	}
	
	
	/*--------------------- Get SmallBlind And Big Blind-----------------------*/
	
	/**
	 * Get the SmallBlind from the Dealer
	 * */
	@Test
	public void testGetSmallBlindFromDealer() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		
		game.setDealer(master);
		game.startGame();

		assertSame("Small Blind", game.getSmallBlind(), player2);
	}
	/**
	 * Get the BigBlind from the Dealer(master)
	 * */
	@Test
	public void testGetBigBlindFromSmallBlind() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		
		game.setDealer(master);
		game.startGame();

		assertSame("Big Blind", game.getBigBlind(), player3);
	}
	
	/**
	 * Get the SmallBlind from the other player is Dealer
	 * */
	@Test
	public void testGetSmallBlindFromOtherSituation() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		
		// set other player(player3) is the Dealer
		game.setDealer(player3);
		game.startGame();

		assertSame("Small Blind", game.getSmallBlind(), player4);
	}
	
	/**
	 * Get the total of player in game
	 * */
	@Test
	public void testGetPlayerInGame() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		
		// set other player(player3) is the Dealer
		game.setDealer(player3);
		game.startGame();

		assertSame("Total of player",  4,game.getListPlayer().size());
	}
	
	/**
	 * Get the BigBlind from the SmallBlind (the other player is Dealer)
	 * */
	@Test
	public void testGetBigBlindFromOtherSmallBlind() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		
		// set other player(player3) is the Dealer
		game.setDealer(player3);
		game.startGame();

		assertSame("Big Blind",  master,game.getBigBlind());
	}
	
	
	/**
	 * Get player3 to be error small blind
	 * */
	@Test
	public void testGetErrorSmallBlindFromDealer() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		
		game.setDealer(master);
		game.startGame();

		//The small blind is player2, don't be player3.
		assertNotSame("It's not Small Blind", game.getSmallBlind(), player3);
	}
	/**
	 * Get error big blind
	 * */
	@Test
	public void testGetErrorBigBlindFromDealer() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		
		game.setDealer(master);
		game.startGame();

		//The small blind is player2. So, big blind is player3, is not player4.
		assertNotSame("It's not Big Blind", game.getBigBlind(), player4);
	}	
}
