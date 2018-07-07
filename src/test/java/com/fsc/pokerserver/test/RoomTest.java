package com.fsc.pokerserver.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

public class RoomTest {

	
	@Test
	public void testCreateRoom() {
		Player master = new Player();
		Room room = new Room(master,BlindLevel.BLIND_10_20);
		
		assertEquals(room.getBlindLevel(),BlindLevel.BLIND_10_20);
		
	}
	@Test
	public void testCreateNewGame() {
		Player master = new Player();
		Room room = new Room(master,BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();
		
		
		
		assertEquals(game.getListPlayer().size(),1);
		
		
		
	}

}
