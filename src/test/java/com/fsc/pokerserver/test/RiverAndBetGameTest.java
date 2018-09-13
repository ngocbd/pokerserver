package com.fsc.pokerserver.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

/**
 * The class to test the River and Bet in the game.
 * @category com > fcs > pokerserver > test
 * */
@Ignore
public class RiverAndBetGameTest {
	private Player master;
	private Room room;

	@Before
	public void setUp() throws Exception {
		master = new Player("Room master");
		room = new Room(master, BlindLevel.BLIND_10_20);
		
	}

	
/*--------------------- River And Bet -----------------------*/
	
	/**
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
	
	/**  
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
	
	/**  
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

}
