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

import org.junit.Test;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

/**
 * The class to test the Turn and Bet in the game.
 * @category com > fcs > pokerserver > test
 * */

public class TurnAndBetGameTest {

/*--------------------- Turn And Bet -----------------------*/
	
	/**
	 * 5 players: master, player2(p2), player3(p3), player4(p4), player5(p5)
	 * Dealer is p5. So, SB is master, BB is p2, UTG is the p3.   
	 * Change position order number between player in third.
	 * */
	@Test(expected = AssertionError.class)
	public void testCheckFalseOrderNumberPlayerInThird() {
		Player master = new Player("Room master");
		master.setGlobalBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player("Player 3");
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player("Player 4");
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player("Player 5");
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();
		
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);
		
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		master.check();
		player2.bet(20);
//		player5 need bet before master in this turn
		master.fold();
		player5.bet(20);

		assertFalse(true);
	}
	
	/**  
	 * Get Cards on the Table After Turn
	 * */
	@Test
	public void testCheckOrderNumberPlayerInThird() {
		Player master = new Player("Room master 1");
		master.setGlobalBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player("Player1 2");
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player("Player1 3");
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player("Player1 4");
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player("Player1 5");
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();
		

		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);

		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		master.check();
		player2.bet(20);
		player5.bet(20);
		master.fold();
		
		assertEquals(game.getCurrentPlayer(), player2);
	}
	
	/**  
	 * Get Cards on the Table After Turn
	 * */
	@Test
	public void testGetCardsOnTableFromPlayerAfterTurn() {
		Player master = new Player("Room master 2");
		master.setGlobalBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player("Player2 2");
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player("Player2 3");
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player("Player2 4");
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player("Player2 5");
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
		
//		System.out.println(player3.isSittingOut());
//		System.out.println(player4.isSittingOut());
//		System.out.println(game.getCurrentRoundBet());
//		System.out.println(master.getRoundBet());
//		System.out.println(player2.getRoundBet());
//		System.out.println(player5.getRoundBet());
//		System.out.println(game.isNextRoundReady());
		
		assertEquals(game.getBoard().getCardNumber(),4);
	}
	
	/**  
	 * Get pot on the table in Turn.
	 * */
	@Test
	public void testGetPotAfterTurn() {
		Player master = new Player("Room master 3");
		master.setGlobalBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player("Player3 2");
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player("Player3 3");
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player("Player3 4");
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player("Player3 5");
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();

		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);

		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		master.check();
		player2.bet(20);
		player5.bet(20);
		master.bet(20);
		
		assertEquals(game.getPotBalance(),210);
	}
	
	/**  
	 * Player bet, check, fold in Preflop, flop, turn
	 * */
	@Test
	public void testPlayerCheckBetFoldInThird() {
		Player master = new Player("Room master 4");
		master.setGlobalBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player("Player4 2");
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player("Player4 3");
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player("Player4 4");
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player("Player4 5");
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();

		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);

		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		master.check();
		player2.bet(20);
		player5.bet(20);
		master.fold();
		
		assertEquals(game.getPotBalance(),190);
	}
	
	/**  
	 * Get pot from Player bet, check, fold after turn
	 * */
	@Test
	public void testGetPotFromPlayerCheckBetFoldAfterTurn() {
		Player master = new Player("Room master 5");
		master.setGlobalBalance(5000);
		Room room = new Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player("Player5 2");
		player2.setGlobalBalance(5000);
		room.addPlayer(player2);
		Player player3 = new Player("Player5 3");
		player3.setGlobalBalance(5000);
		room.addPlayer(player3);
		Player player4 = new Player("Player5 4");
		player4.setGlobalBalance(5000);
		room.addPlayer(player4);
		Player player5 = new Player("Player5 5");
		player5.setGlobalBalance(5000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();

		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);

		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		master.check();
		player2.bet(20);
		player5.bet(30);
		master.fold();
		player2.bet(10);
		
		assertEquals(game.getPotBalance(),210);
	}
	
}
