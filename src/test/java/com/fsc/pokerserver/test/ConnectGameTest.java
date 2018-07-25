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

public class ConnectGameTest {
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
	
}
