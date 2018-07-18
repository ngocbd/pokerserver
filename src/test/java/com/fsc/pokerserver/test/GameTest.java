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

public class GameTest {
	Player master;
	Room room;

	@Before
	public void setUp() throws Exception {
		master = new Player("Room master");
		room = new Room(master, BlindLevel.BLIND_10_20);
	}

	@Test
	public void testCreateGame() {
		Game game = room.createNewGame();
		assertNotNull(game);
	}

	@Test
	public void testGameDeck() {
		Game game = room.createNewGame();

		assertNotNull(game.getDeck());
	}

	@Test
	public void testGameDeckShutffed() {
		Game game = room.createNewGame();
		Deck deck = new Deck();
		deck.initDeck();
		assertNotEquals(game.getDeck().exportDeck(), deck.exportDeck());

	}

	@Test
	public void testGameStart() {
		Game game = room.createNewGame();

		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		
		game.setDealer(player2);
		
		game.startGame();

	}

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
	
	
	
	@Test(expected = AssertionError.class)
	public void testDealerNotIngame() {
		Game game = room.createNewGame();

		Player player2 = new Player();
//		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		
		game.setDealer(player2);

	}

	@Test
	public void testSetDealerAndBig() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		
		game.setDealer(master);
		
		game.startGame();

		assertSame("Same Player", game.getBigBlind(), player3);

	}

	@Test
	public void testSetDealerAndSmall() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		
		game.setDealer(master);
		game.startGame();

		assertSame("Same Player", game.getSmallBlind(), player2);

	}
	
	
	@Test
	public void testSetDealerAndSmallCheck() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		
		game.setDealer(player3);
		
		game.startGame();

		assertSame("Same Player", game.getSmallBlind(), master);

	}

	

	@Test
	public void testSetDealerAndSmallCheckFalse() {
		Game game = room.createNewGame();

		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);

		game.setDealer(player3);

		game.startGame();

		assertNotSame(game.getSmallBlind(), player2);

	}

	@Test
	public void testSetDealerAndSmallFourPlayer() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);
		Player player3 = new Player();
		game.addPlayer(player3);
		Player player4 = new Player();
		game.addPlayer(player4);
		
		game.setDealer(player3);
		
		game.startGame();

		assertSame(game.getSmallBlind(), player4);

	}
	
	@Test
	public void tesSmallBlindBet() {
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

		assertEquals(master.getBalance(), 990);

	}

	@Test
	public void testBigBlindBet() {
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

		assertEquals(player2.getBalance(), 980);

	}

	@Test
	public void testPotFreFlop() {
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

		assertEquals(game.getPotBalance(), 30);

	}
	
	@Test
	public void testPotFreFlopAfterUnderTheGunBet() {
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
		
		player3.bet(70);

		assertEquals(game.getPotBalance(), 100);

	}

	@Test
	public void testSetDealerAndSmallFivePlayer() {
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

		assertSame(game.getSmallBlind(), master);

	}

	@Test
	public void testGamePreFlop() {
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
		player5.bet(20);
		master.bet(10);
		
		game.flop();

		assertEquals(game.getListPlayer().get(0).getPlayerHand().getCardNumber(), 2);

	}
	
//	
//	@Test(expected = AssertionError.class)
//	public void testGamePreFlopNotPlayerInGame() {
//		Game game = room.createNewGame();
//
//		Player player2 = new Player();
//		game.addPlayer(player2);
//		Player player3 = new Player();
////		game.addPlayer(player3);
//		Player player4 = new Player();
//		game.addPlayer(player4);
//		Player player5 = new Player();
//		game.addPlayer(player5);
//
//		game.setDealer(player5);
//
//		master.setBalance(1000);
//		player2.setBalance(1000);
//		player3.setBalance(1000);
//		player4.setBalance(1000);
//		player5.setBalance(1000);
//
//		game.startGame();
//		
//		game.preflop();
//		
//		player3.bet(20);
//		player4.bet(20);
//		player5.bet(20);
//		master.bet(10);
//		
//		game.flop();
//
////		assertEquals(game.getListPlayer().get(0).getPlayerHand().getCardNumber(), 2);
//
//	}
	
	/*
	 * 5 players: master, player2(p2), player3(p3), player4(p4), player5(p5)
	 * Dealer is p5. So, SB is master, BB is p2, UTG is the p3. 
	 * In preflop, the first call is p3.  
	 * 
	 * */
	@Test(expected = AssertionError.class)
	public void testTurnGameInPreFlopPlayer3AndPlayer4() {
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
//		it's error when player4 bet before player3
		player4.bet(20);
		player3.bet(20);
		player5.bet(20);
		master.bet(10);
		
		game.flop();

	}

	
	@Test(expected = AssertionError.class)
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

	}
	
	@Test
	public void testBetEqualtBetweenPlayersMoreTimesInPreFlop() {
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
		player5.bet(20);
		//master bet more 10 than players. Not equals. So, can't call flop function
		master.bet(20);
		player2.bet(10);
		player3.bet(10);
		player4.bet(10);
		player5.bet(10);
		
		game.flop();
//		After flop, the SB is starter if SB don't fold. So, the current player is player2
		assertEquals(game.getCurrentPlayer(), master);
	}

	@Test(expected = AssertionError.class)
	public void testBetNotEqualtBetweenPlayersAndMaster() {
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
		player5.bet(20);
		//master bet more 10 than players. Not equals. So, can't call flop function
		master.bet(20);
		
		game.flop();

	}
	
	@Test(expected = AssertionError.class)
	public void testBetEqualtBetweenPlayersAndMasterMoreTimes() {
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
//		it's error when player4 bet before player3
		player4.bet(20);
		player3.bet(20);
		player5.bet(20);
		master.bet(20);
		
		game.flop();

	}
	
	
	
	// TODO Write more method to test other cases for current and next Player
	// !!!+-
	 

	@Test
	public void testGameAfterPreFlopCurrentPlayer() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);

		Player player3 = new Player();
		game.addPlayer(player3);

		Player player4 = new Player();
		game.addPlayer(player4);

		Player player5 = new Player();
		game.addPlayer(player5);

		game.setDealer(master);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);
		
		game.startGame();
		game.preflop();

		/*
		 * If Player 1 is dealer then : - Player 2 is SmallBlind - Player 3 is
		 * BigBlind - Player 4 is UNDER THE GUN , he must be start bet first
		 */

		assertEquals(game.getCurrentPlayer(), player4);

	}
	
	@Test
	public void testGameAfterPreFlopCurrentPlayerEndRound() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);

		Player player3 = new Player();
		game.addPlayer(player3);

		Player player4 = new Player();
		game.addPlayer(player4);

		Player player5 = new Player();
		game.addPlayer(player5);

		game.setDealer(master);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);
		
		game.startGame();
		game.preflop();
		//set player4 bet more coin => current player have to be player5
		player4.bet(20);
		/*
		 * If Player 1 is dealer then : - Player 2 is SmallBlind - Player 3 is
		 * BigBlind - Player 4 is UNDER THE GUN , he must be start bet first
		 */

		assertSame(game.getCurrentPlayer(), player5);

	}

	@Test
	public void testGameAfterPreFlopBet() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);

		Player player3 = new Player();
		game.addPlayer(player3);

		Player player4 = new Player();
		game.addPlayer(player4);

		Player player5 = new Player();
		game.addPlayer(player5);

		game.setDealer(master);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);
		game.startGame();
		game.preflop();
		player4.bet(200);

		/*
		 * If Player 1 is dealer then : - Player 2 is SmallBlind - Player 3 is BigBlind
		 * - Player 4 is UNDER THE GUN , he must be start bet first
		 */

		assertEquals(player4.getBalance(), 800);

	}

	@Test
	public void testGameBetPotBalance() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);

		Player player3 = new Player();
		game.addPlayer(player3);

		Player player4 = new Player();
		game.addPlayer(player4);

		Player player5 = new Player();
		game.addPlayer(player5);

		game.setDealer(master);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);
		game.startGame();
		game.preflop();
		player4.bet(200);

		/*
		 * If Player 1 is dealer then : - Player 2 is SmallBlind - Player 3 is BigBlind
		 * - Player 4 is UNDER THE GUN , he must be start bet first pot must be equal
		 * small + big + bet = 230
		 */

		assertEquals(230,game.getPotBalance() );

	}

	/* Test action next player turn when previous player bet */
	@Test
	public void testGameNextBet() {
		Game game = room.createNewGame();
		
		Player player2 = new Player();
		game.addPlayer(player2);

		Player player3 = new Player();
		game.addPlayer(player3);

		Player player4 = new Player();
		game.addPlayer(player4);

		Player player5 = new Player();
		game.addPlayer(player5);

		game.setDealer(master);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.preflop();
		player4.bet(200);

		/*
		 * If Player 1 is dealer then : - Player 2 is SmallBlind - Player 3 is BigBlind
		 * - Player 4 is UNDER THE GUN , he must be start bet first Then Player 5 will
		 * next turn after Player 4
		 */

		assertEquals(game.getCurrentPlayer(), player5);

	}

	/* Test action next player turn when previous player bet */
	
	//TODO need fix
	@Test
	public void testGameNextBet2Time() {
		Game game = room.createNewGame();

		Player player2 = new Player();
		game.addPlayer(player2);

		Player player3 = new Player();
		game.addPlayer(player3);

		Player player4 = new Player();
		game.addPlayer(player4);

		Player player5 = new Player();
		game.addPlayer(player5);

		game.setDealer(master);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);
		game.startGame();
		game.preflop();
		player4.bet(200);
		player5.bet(200);

		/*
		 * If Player 1 is dealer then : - Player 2 is SmallBlind - Player 3 is BigBlind
		 * - Player 4 is UNDER THE GUN , he must be start bet first Then Player 5 will
		 * next turn after Player 4 Then master will next turn after Player 5
		 */

		//assertEquals(game.getCurrentPlayer(), master);

	}

	/* Test bet lower than current bet */
	@Test(expected = AssertionError.class)
	public void testGameNextBetFail() {
		Game game = room.createNewGame();
		game.startGame();
		Player player2 = new Player();
		game.addPlayer(player2);

		Player player3 = new Player();
		game.addPlayer(player3);

		Player player4 = new Player();
		game.addPlayer(player4);

		Player player5 = new Player();
		game.addPlayer(player5);

		game.setDealer(master);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);

		game.preflop();
		player4.bet(200);

		player5.bet(100);



	}
	
	@Test
	public void testGameNextRoundReady() {
		Game game = room.createNewGame();
		
		Player player2 = new Player("player 2");
		game.addPlayer(player2);

		Player player3 = new Player("player 3");
		game.addPlayer(player3);

		Player player4 = new Player("player 4");
		game.addPlayer(player4);

		Player player5 = new Player("player 5");
		game.addPlayer(player5);

		game.setDealer(master);

		master.setBalance(1000);
		player2.setBalance(1000);
		player3.setBalance(1000);
		player4.setBalance(1000);
		player5.setBalance(1000);
		game.startGame();
		game.preflop();
		player4.bet(20);

		player5.bet(20);
		
		
		master.bet(20);
		player2.bet(10);
		//System.out.println(game.dumpListPlayer());
		assertTrue(game.isNextRoundReady());
		

		

	}

}
