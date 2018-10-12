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

import org.junit.Test;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

import static org.junit.Assert.*;

/**
 * The class to test the Preflop and Bet in the game.
 * @category com > fcs > pokerserver > test
 * */

public class PreflopAndBetGameTest {

/*--------------------- Preflop And Bet -----------------------*/
	
	
	/**
	 * Get the pot from the small blind in Preflop
	 * */
	@Test
	public void testGetBalanceOfSmallBlindInFreFlop() {
		Player master = new Player("Room master 1");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);
		
		game.setDealer(player5);

		game.startGame();

		assertEquals(master.getBalance(), 990);
	}
	
	/**
	 * current player in Flop not bet
	 * */
	@Test
	public void testGetCurrentPlayerAfterFreFlopNotBet() {
		Player master = new Player("Room master 2");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();
		
		assertEquals(game.getNextPlayer(player2), player3);

//		assertEquals(game.getCurrentPlayer(), player3);
	}

	@Test
	public void test2PPreflop(){
		Player player1 = new Player();
		player1.setBalance(1000);
		Player player2 = new Player();
		player2.setBalance(1000);
		Player player3 = new Player();
		player3.setBalance(1000);
		Room room = new Room(player2, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();


		room.addPlayer(player1);
		room.addPlayer(player2);
		room.addPlayer(player2);
		game.setDealer(player2);
		System.out.println("SB: "+game.getSmallBlind().getId());
		System.out.println("BB: "+game.getBigBlind().getId());
	}
	/** 
	 * Get current player in Flop.
	 * */
	@Test
	public void testGetCurrentPlayerAfterPlayerFoldAfterPreFlop() {
		Player master = new Player("Room master 3");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);


		game.startGame();
		
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		assertEquals(game.getCurrentPlayer(), player4);
	}
	
	
	/**
	 * Get the balance from the big blind in Preflop
	 * */
	@Test
	public void testGetBalanceOfBigBlindInFreFlop() {
		Player master = new Player("Room master 4");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();
		System.out.println(player2.getBalance());
		assertEquals(player2.getBalance(), 980);
	}
	
	/**
	 * Get the pot from the small blind and big blind in Preflop
	 * */
	@Test
	public void testPotFromSBAndBBInFreFlop() {
		Player master = new Player("Room master 5");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);
		
		game.setDealer(player5);

		game.startGame();

		assertEquals(game.getPotBalance(), 30);
	}
	
	/**
	 * Get the pot after the player is under the gun bet in Preflop
	 * */
	@Test
	public void testGetPotAfterUnderTheGunBetInPreflop() {
		Player master = new Player("Room master 6");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);
		
		game.setDealer(player5);

		game.startGame();

		//player is Dealer. master is smallblind. player2 is bigblind. player3 is underthegun.
		player3.bet(70);

		assertEquals(game.getPotBalance(), 100);
	}
	
	/**
	 * Get the pot from the small blind and big blind. Player3 and player4 bet in Preflop
	 * */
	@Test
	public void testPotIsBetInFreFlop() {
		Player master = new Player("Room master 7");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);
		
		game.setDealer(player5);

		game.startGame();

		//player bet
		player3.bet(70);
		player4.bet(250);
		
		assertEquals(game.getPotBalance(), 350);
	}
	
	/**
	 * 5 players: master, player2(p2), player3(p3), player4(p4), player5(p5)
	 * Dealer is p5. So, SB is master, BB is p2, UTG is the p3. 
	 * In preflop, the first call is p3.  
	 * */
	@Test
	public void testTurnAndBetAndGetPotInPreFlop() {
		Player master = new Player("Room master 8");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);
		game.setDealer(player5);

		game.startGame();
		
		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
		
		assertEquals(game.getPotBalance(), 150);
	}
	
	
	/**
	 * Check turn player bet is correct.
	 * */
	@Test(expected = AssertionError.class)
	public void testTurnGameInPreFlop() {
		Player master = new Player("Room master 9");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();
		
//		it's error when player4 bet before player3
		player4.bet(20);
		player3.bet(20);
		player5.bet(20);
		master.bet(10);

		assertFalse(true);
	}
	
	
	/**
	 * Get current player in preflop.
	 * */
	@Test
	public void testCurrentPlayerInPreFlop() {
		Player master = new Player("Room master 10");
		master.setBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player();
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player();
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player();
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player();
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();
		
		player3.bet(20);
		player4.bet(20);
//		player5.bet(20);
//		master.bet(10);
		
//		game.flop();
		
		assertEquals(game.getCurrentPlayer(), player5);
	}
		
}
