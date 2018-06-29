package com.fsc.pokerserver.test;

import static org.junit.Assert.*;

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
		master = new Player();
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
		Deck deck = new Deck();
		deck.initDeck();
		Player player2 = new Player();
		
		game.addPlayer(player2);
		game.startGame();
		

	}
	@Test
	public void testGamePreFlop() {
		Game game = room.createNewGame();
		Deck deck = new Deck();
		deck.initDeck();
		Player player2 = new Player();
		
		game.addPlayer(player2);
		game.startGame();
		
		
		game.preflop();
		
		assertEquals(game.getListPlayer().get(0).getPlayerHand().getCardNumber(), 2);
		
		//game.getListPlayer().get(0).getc

	}

}
