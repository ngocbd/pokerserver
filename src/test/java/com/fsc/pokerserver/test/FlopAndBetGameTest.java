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
import org.junit.Test;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Deck;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

public class FlopAndBetGameTest {
	Player master;
	Room room;

	@Before
	public void setUp() throws Exception {
		master = new Player("Room master");
		room = new Room(master, BlindLevel.BLIND_10_20);
		
	}
	
	/*--------------------- Flop And Bet -----------------------*/
	
	/*
	 * 5 players: master, player2(p2), player3(p3), player4(p4), player5(p5)
	 * Dealer is p5. So, SB is master, BB is p2, UTG is the p3. 
	 * In preflop, the first call is p3.  
	 * Get sum of cards in Flop.
	 * */
	@Test
	public void testGetCardsOnTheTableAfterFlop() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
		
		game.flop();

		assertEquals(game.getListPlayer().get(0).getPlayerHand().getCardNumber(), 2);
	}
	
	
	
	

	/*  
	 * Get current player in Flop.
	 * */
//	

	
	/*
	 * Check sum of Cards in Flop. it's correct if sum of cards is 2. It's not correct if sum of cards is not 2.
	 * */
	@Test(expected = AssertionError.class)
	public void testErrorCardsOnTheTableInFlop() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
		
		game.flop();

		assertEquals(game.getListPlayer().get(0).getPlayerHand().getCardNumber(), 3);
	}
	
	/*
	 * Get current player.
	 * */
	public void testTurnGameInPreFlopPlayer3AndPlayer5() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
//		it's error when player5 bet before player3
		player5.bet(20);
		player3.bet(20);
		player4.bet(20);
		master.bet(10);
		
		game.flop();
		
		assertEquals(game.getCurrentPlayer(),master);

	}
	
	/*
	 * get pot of game from player bet after flop.
	 * */
	public void testPlayerBetAfterFlop() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
		
		game.flop();
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
	
	/*
	 * get pot of game from player bet after flop.
	 * */
	public void testPlayerBetCheckFoldAfterFlop() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
		
		game.flop();
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
	
	

	/*
	 * get current player of game from bet, check, fold after flop.
	 * */
	public void testGetCurrentPlayerFromBetCheckFoldAfterFlop() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
//		player3.bet(10);
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player3.fold();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10);
		//player3 not bet because player3 folded
//		player3.bet(10);
//		player4.bet(10);
		 

		assertEquals(game.getCurrentPlayer(), master);
	}
	
	/*
	 * Player3 still bet although player3 folded after flop.
	 * */
	@Test(expected = AssertionError.class)
	public void testCheckPlayer3BetAlthoughFoldAfterFlop() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
		
		game.flop();
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
	
	
	/*
	 * Change position of player bet after flop.
	 * */
	@Test(expected = AssertionError.class)
	public void testChangePositionPlayerBetAfterFlop() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
		
		game.flop();
		// master need bet before player2.
		player2.bet(20);
		master.bet(20);
		player3.bet(20);
		player4.bet(20);
		player5.bet(30);
		master.bet(10);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
	}
	
	/*
	 * get current pot of game from bet, check, fold after flop.
	 * */
	@Test(expected = AssertionError.class)
	public void testPlayer3ReFoldInFlopAlthoughPlayer3FoldedInPreFlop() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
//		player3.bet(10);
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player3.fold();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10);
	}
	
	/*
	 * check again the situation.
	 * cay nay dang bi loi vi player3 da fold o vong preflop roi. vi vay, o vong flop, se ko dc. fold tiep.
	 * get current pot of game from bet, check, fold after flop.
	 * */
	@Test
	public void testGetPotFromBetCheckFoldAfterFlop() {
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

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.startGame();
		
		game.preflop();
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
//		player3.bet(10);
		player4.bet(10);
		
		game.flop();
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
