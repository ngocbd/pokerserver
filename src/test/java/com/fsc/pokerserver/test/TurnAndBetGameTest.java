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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

/**
 * The class to test the Turn and Bet in the game.
 * @category com > fcs > pokerserver > test
 * */
@Ignore
public class TurnAndBetGameTest {
	private Player master;
	private Room room;

	@Before
	public void setUp() throws Exception {
		master = new Player("Room master");
		room = new Room(master, BlindLevel.BLIND_10_20);
		
	}	
/*--------------------- Turn And Bet -----------------------*/
	
	/**
	 * 5 players: master, player2(p2), player3(p3), player4(p4), player5(p5)
	 * Dealer is p5. So, SB is master, BB is p2, UTG is the p3.   
	 * Change position order number between player in third.
	 * */
	@Test(expected = AssertionError.class)
	public void testCheckFalseOrderNumberPlayerInThird() {
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
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
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		game.turn();
		master.check();
		player2.bet(20);
//		player5 need bet before master in this turn
		master.fold();
		player5.bet(20);
		
	}
	
	/**  
	 * Get Cards on the Table After Turn
	 * */
	@Test
	public void testCheckOrderNumberPlayerInThird() {
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
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
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		game.turn();
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
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
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
		
//		System.out.println(player3.isSittingOut());
//		System.out.println(player4.isSittingOut());
//		System.out.println(game.getCurrentRoundBet());
//		System.out.println(master.getRoundBet());
//		System.out.println(player2.getRoundBet());
//		System.out.println(player5.getRoundBet());
//		System.out.println(game.isNextRoundReady());
		
		game.turn();
		
		assertEquals(game.getBoard().getCardNumber(),4);
	}
	
	/**  
	 * Get pot on the table in Turn.
	 * */
	@Test
	public void testGetPotAfterTurn() {
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
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
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		game.turn();
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
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
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
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		game.turn();
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
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		game.addPlayer(player2);
		Player player3 = new Player("Player 3");
		game.addPlayer(player3);
		Player player4 = new Player("Player 4");
		game.addPlayer(player4);
		Player player5 = new Player("Player 5");
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
		player4.bet(10);
		
		game.flop();
		master.check();
		player2.check();
		player4.fold();
		player5.bet(10);
		master.bet(10);
		player2.bet(10); 	 

		game.turn();
		master.check();
		player2.bet(20);
		player5.bet(30);
		master.fold();
		player2.bet(10);
		
		assertEquals(game.getPotBalance(),210);
	}
	
}
