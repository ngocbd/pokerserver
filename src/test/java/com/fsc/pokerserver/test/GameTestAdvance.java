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

public class GameTestAdvance {
	Player master;
	Room room;

	@Before
	public void setUp() throws Exception {
		master = new Player("Room master");
		room = new Room(master, BlindLevel.BLIND_10_20);
		
	}

	/*
	 * Create new Game
	 * */
	@Test
	public void testCreateGame() {
		Game game = room.createNewGame();
		assertNotNull(game);
	}

	/*
	 * Deck of cards is not null
	 * */
	@Test
	public void testGameDeck() {
		Game game = room.createNewGame();

		assertNotNull(game.getDeck());
	}

	/*
	 * shuffle deck of cards
	 * */
	@Test
	public void testGameShuffleDeck() {
		Game game = room.createNewGame();
		Deck deck = new Deck();
		deck.initDeck();
		assertNotEquals(game.getDeck().exportDeck(), deck.exportDeck());
	}

	/*
	 * Game start
	 * */
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
	
	/*--------------------- Dealer -----------------------*/
	
	/*
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

	/*
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
	
	/*
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
	
	/*
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
	/*
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
	
	/*
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
	
	/*
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
	
	/*
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
	
	
	/*
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
	/*
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
	
	
/*--------------------- Preflop And Bet -----------------------*/
	
	
	/*
	 * Get the pot from the small blind in Preflop
	 * */
	@Test
	public void testGetBalanceOfSmallBlindInFreFlop() {
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
	
	/*
	 * current player in Flop not bet
	 * */
	@Test
	public void testGetCurrentPlayerAfterFreFlopNotBet() {
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
		
		assertEquals(game.getNextPlayer(player2), player3);
		
		
//		assertEquals(game.getCurrentPlayer(), player3);
	}
	
	/*  
	 * Get current player in Flop.
	 * */
	@Test
	public void testGetCurrentPlayerAfterPlayerFoldAfterPreFlop() {
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
		
		
		
		assertEquals(game.getCurrentPlayer(), player4);
	}
	
	
	/*
	 * Get the balance from the big blind in Preflop
	 * */
	@Test
	public void testGetBalanceOfBigBlindInFreFlop() {
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
	
	/*
	 * Get the pot from the small blind and big blind in Preflop
	 * */
	@Test
	public void testPotFromSBAndBBInFreFlop() {
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
	
	/*
	 * Get the pot after the player is under the gun bet in Preflop
	 * */
	@Test
	public void testGetPotAfterUnderTheGunBetInPreflop() {
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
		//player is Dealer. master is smallblind. player2 is bigblind. player3 is underthegun.
		player3.bet(70);

		assertEquals(game.getPotBalance(), 100);

	}
	
	/*
	 * Get the pot from the small blind and big blind. Player3 and player4 bet in Preflop
	 * */
	@Test
	public void testPotIsBetInFreFlop() {
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
		
		//player bet
		player3.bet(70);
		player4.bet(250);
		
		assertEquals(game.getPotBalance(), 350);
	}
	
	/*
	 * 5 players: master, player2(p2), player3(p3), player4(p4), player5(p5)
	 * Dealer is p5. So, SB is master, BB is p2, UTG is the p3. 
	 * In preflop, the first call is p3.  
	 * */
	@Test
	public void testTurnAndBetAndGetPotInPreFlop() {
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
		
//		game.flop();

		assertEquals(game.getPotBalance(), 150);
	}
	
	
	/*
	 * Check turn player bet is correct.
	 * */
	@Test(expected = AssertionError.class)
	public void testTurnGameInPreFlop() {
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
		
//		game.flop();
	}
	
	
	/*
	 * Get current player in preflop.
	 * */
	@Test
	public void testCurrentPlayerInPreFlop() {
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
//		player5.bet(20);
//		master.bet(10);
		
//		game.flop();
		
		assertEquals(game.getCurrentPlayer(), player5);
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
	
	
/*--------------------- Turn And Bet -----------------------*/
	
	/*
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
	
	/*  
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
	
	/*  
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
	
	/*  
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
	
	/*  
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
	
	/*  
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
	
/*--------------------- River And Bet -----------------------*/
	
	/*
	 * 5 players: master, player2(p2), player3(p3), player4(p4), player5(p5)
	 * Dealer is p5. So, SB is master, BB is p2, UTG is the p3.   
	 * Change position order number between player in fourth.
	 * */
	@Test(expected = AssertionError.class)
	public void testCheckFalseOrderNumberPlayerInFourth() {
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
		
		game.river();
		//player2 need bet before player5
		player5.fold();
		player2.bet(20);
		
	}
	
	/*  
	 * Get cards after river.
	 * */
	@Test
	public void testGetCardsAfterRiver() {
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
		
		game.river();
		player2.bet(20);
		player5.fold();
		
		assertEquals(game.getBoard().getCardNumber(), 5);
	}
	
	/*  
	 * Get pot after river in game.
	 * */
	@Test
	public void testGetPotAfterRiver() {
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
		
		game.river();
		player2.bet(20);
		player5.bet(30);
		player2.fold();
		
		assertEquals(game.getPotBalance(), 240);
	}
	
	/*--------------------- End Game -----------------------*/
	
	/*  
	 * Error order number from player Check, bet, fold before end game.
	 * */
	@Test(expected = AssertionError.class)
	public void testCheckBetFoldBeforeEndGame() {
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
		
		game.river();
		player2.bet(20);
//		player5 need bet before player2
		player2.fold();
		player5.bet(30);
		
	}
	
	
	/*  
	 * call end game although current round bet not equal
	 * */
	@Test(expected = AssertionError.class)
	public void testCallEndGameAlthoughCurrentRoundBetNotEqual() {
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
		
		game.river();
		player2.bet(20);
		player5.bet(30);
		player2.bet(20);
		
		game.endGame();
		
	}
	
	/*  
	 * Get pot from game after end game.
	 * */
	@Test
	public void testGetPotAfterEndGame() {
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
		
		game.river();
		player2.bet(20);
		player5.bet(30);
		player2.fold();
		
		game.endGame();
		
		assertEquals(game.getPotBalance(), 240);
	}
	
	/*  
	 * Get cards from game after end game.
	 * */
	@Test
	public void testGetCardsAfterEndGame() {
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
		
		game.river();
		player2.bet(20);
		player5.bet(30);
		player2.fold();
		
		game.endGame();
		
		assertEquals(game.getBoard().getCardNumber(),5);
	} 
	
	
}
