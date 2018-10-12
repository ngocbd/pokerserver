package com.fsc.pokerserver.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

/**
 * The class to test the River and Bet in the game.
 * @category com > fcs > pokerserver > test
 * */

public class RiverAndBetGameTest {

	
/*--------------------- River And Bet -----------------------*/
	
	/**
	 * 5 players: master, player2(p2), player3(p3), player4(p4), player5(p5)
	 * Dealer is p5. So, SB is master, BB is p2, UTG is the p3.   
	 * Change position order number between player in fourth.
	 * */
	@Test(expected = AssertionError.class)
	public void testCheckFalseOrderNumberPlayerInFourth() {
		Player master=new Player("Rooms master");
		master.setGlobalBalance(1000);
		Room room = new  Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player("Player 2");
		player2.setGlobalBalance(1000);
		room.addPlayer(player2);
		Player player3 = new Player("Player 3");
		player3.setGlobalBalance(1000);
		room.addPlayer(player3);
		Player player4 = new Player("Player 4");
		player4.setGlobalBalance(1000);
		room.addPlayer(player4);
		Player player5 = new Player("Player 5");
		player5.setGlobalBalance(1000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();
		
		player3.fold();
		player4.bet(20);
		player5.bet(30);
		master.bet(20);
		player2.bet(10);
		player4.bet(10);
		player2.check();
		
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
		
		//player2 need bet before player5
		player5.fold();
		player2.bet(20);

		assertFalse(true);
	}
	
	/**  
	 * Get cards after river.
	 * */
	@Test
	public void testGetCardsAfterRiver() throws InterruptedException {
		Player master=new Player("Rooms2 master");
		master.setGlobalBalance(1000);
		Room room = new  Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player("Players2 2");
		player2.setGlobalBalance(1000);
		room.addPlayer(player2);
		Player player3 = new Player("Players2 3");
		player3.setGlobalBalance(1000);
		room.addPlayer(player3);
		Player player4 = new Player("Players2 4");
		player4.setGlobalBalance(1000);
		room.addPlayer(player4);
		Player player5 = new Player("Players2 5");
		player5.setGlobalBalance(1000);
		room.addPlayer(player5);

		game.setDealer(player5);

		game.startGame();

		//round 1 - PREFLOP
        player3.bet(20);
		player4.bet(20);
		player5.bet(20);
		master.bet(10);
		player2.check();

		//round 2 - FLOP
		master.check();
		player2.bet(20);
		player3.bet(20);
		player4.bet(20);
		player5.fold();
		master.bet(20);

		//round 3- TURN
		System.out.println("Game Status: "+game.getStatus());
		System.out.println("current: "+game.getCurrentPlayer()+" current bet: "+game.getCurrentRoundBet());
		System.out.println("roundbet: "+master.getRoundBet());
		master.check();
		System.out.println("current: "+game.getCurrentPlayer()+" current bet: "+game.getCurrentRoundBet());
		player2.fold();
		System.out.println(player2.getCurrentGame().getStatus());
		player3.bet(10);
		player4.fold();
		player5.fold();
		master.bet(10);

		//round 4
		master.bet(10);
		player3.fold();

		assertEquals(game.getBoard().getCardNumber(), 5);

	}
	
	/**  
	 * Get pot after river in game.
	 * */
	@Test
	public void testGetPotAfterRiver() {
		Player master=new Player("Rooms3 master");
		master.setGlobalBalance(1000);
		Room room = new  Room(master, BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();

		Player player2 = new Player("Playerss 2");
		player2.setGlobalBalance(1000);
		room.addPlayer(player2);
		Player player3 = new Player("Playerss 3");
		player3.setGlobalBalance(1000);
		room.addPlayer(player3);
		Player player4 = new Player("Playerss 4");
		player4.setGlobalBalance(1000);
		room.addPlayer(player4);
		Player player5 = new Player("Playerss 5");
		player5.setGlobalBalance(1000);
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
		
		player2.bet(20);
		player5.bet(30);
		player2.fold();
		
		assertEquals(game.getPotBalance(), 240);
	}

}
