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
import static org.junit.Assert.assertFalse;

import com.fcs.pokerserver.*;
import org.junit.Test;

/**
 * The class to test the Flop and the Player bet in the game.
 * @category com > fcs > pokerserver > test
 * */

public class FlopAndBetGameTest {

	/*--------------------- Flop And Bet -----------------------*/
	
	/**
	 * 5 players: master, player2(p2), player3(p3), player4(p4), player5(p5)
	 * Dealer is p5. So, SB is master, BB is p2, UTG is the p3. 
	 * In preflop, the first call is p3.  
	 * Get sum of cards in Flop.
	 * */
	@Test
	public void testGetCardsOnTheTableAfterFlop() {
		Player master = new Player("Room master 1 ");
		master.setGlobalBalance(5000);
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();

		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);

		assertEquals(game.getListPlayer().get(0).getPlayerHand().getCardNumber(), 2);
	}
	
	
	/**
	 * Check sum of Cards in Flop. it's correct if sum of cards is 2. It's not correct if sum of cards is not 2.
	 * */
	@Test(expected = AssertionError.class)
	public void testErrorCardsOnTheTableInFlop() {
		Player master = new Player("Room master 2");
		master.setGlobalBalance(5000);
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();

		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);

		assertEquals(game.getListPlayer().get(0).getPlayerHand().getCardNumber(), 3);
	}
	
	/**
	 * Get current player.
	 * */
	@Test (expected = AssertionError.class)
	public void testTurnGameInPreFlopPlayer3AndPlayer5() {
		Player master = new Player("Room master 3");
		master.setGlobalBalance(5000);
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();

//		it's error when player5 bet before player3
		player5.bet(20);
		player3.bet(20);
		player4.bet(20);
		master.bet(10);

		assertFalse(true);

	}
	
	/**
	 * get pot of game from player bet after flop.
	 * */
	@Test
	public void testPlayerBetAfterFlop() {
		Player master = new Player("Room master 4");
		master.setGlobalBalance(5000);
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();

		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);

		master.bet(20);
		player2.bet(20);
		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(10);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
		 

		assertEquals(game.getPotBalance(), 300);
	}
	
	/**
	 * get pot of game from player bet after flop.
	 * */
	@Test
	public void testPlayerBetCheckFoldAfterFlop() {
		Player master = new Player("Room master 5");
		master.setGlobalBalance(5000);
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

		master.check();
		player2.check();
		player3.fold();
		player4.check();
		player5.bet(10);
		master.bet(10);
		player2.bet(10);
		//player3 not bet because player3 folded
//		player3.bet(10);
		player4.bet(10);
		 

		assertEquals(game.getPotBalance(), 190);
	}
	
	

	/**
	 * get current player of game from bet, check, fold after flop.
	 * */
	@Test
	public void testGetCurrentPlayerFromBetCheckFoldAfterFlop() {
		Player master = new Player("Room master 6");
		master.setGlobalBalance(5000);
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
//		player3.bet(10);
		player4.bet(10);

		master.check();
		player2.check();
//		player3.fold();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10);
		//player3 not bet because player3 folded
//		player3.bet(10);
//		player4.bet(10);
		 

		assertEquals(game.getCurrentPlayer(), master);
	}
	
	/**
	 * Player3 still bet although player3 folded after flop.
	 * */
	@Test(expected = AssertionError.class)
	public void testCheckPlayer3BetAlthoughFoldAfterFlop() {
		Player master = new Player("Room master 7");
		master.setGlobalBalance(5000);
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

		master.check();
		player2.check();
		player3.fold();
		player4.check();
		player5.bet(10);
		master.bet(10);
		player2.bet(10);
		//player3 not bet because player3 folded
		player3.bet(10);
		player4.bet(10);
	}


	/**
	 * get current pot of game from bet, check, fold after flop.
	 * */
	@Test(expected = AssertionError.class)
	public void testPlayer3ReFoldInFlopAlthoughPlayer3FoldedInPreFlop() {
		Player master = new Player("Room master 8");
		master.setGlobalBalance(5000);
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
//		player3.bet(10);
		player4.bet(10);

		master.check();
		player2.check();
		player3.fold();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10);

		assertFalse(true);
	}
	
	/**
	 * check again the situation.
	 * cay nay dang bi loi vi player3 da fold o vong preflop roi. vi vay, o vong flop, se ko dc. fold tiep.
	 * get current pot of game from bet, check, fold after flop.
	 * */
	@Test
	public void testGetPotFromBetCheckFoldAfterFlop() {
		Player master = new Player("Room master 9");
		master.setGlobalBalance(5000);
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
//		player3.bet(10);
		player4.bet(10);
		
		master.check();
		player2.check();
//		player3 da fold o vong preflop roi. vi vay, o vong flop, se ko dc. fold tiep.
//		player3.fold();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		assertEquals(game.getPotBalance(), 150);
	}
		
}

