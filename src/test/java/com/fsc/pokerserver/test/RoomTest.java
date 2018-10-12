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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;
import com.fcs.pokerserver.events.AbstractRoomEvent;
import com.fcs.pokerserver.events.RoomListener;
import com.fcs.pokerserver.events.VisitRoomEvent;

/**
 * The class to test to create a new Room in the game.
 * @category com > fcs > pokerserver > test
 * */

public class RoomTest {

	/**
	 * candidateRoom is temporary object holder for equality check in testcase
	 * 
	 * */
	Room candidateRoom ;
	
	/**
	 * candidateRoom is temporary object holder for equality check in testcase
	 * 
	 * */
	Player candidatePlayer ;


	@Before
	public void setUp()
	{
		candidateRoom = null;
	}
	/**
	 * The method to create new the room
	 * */
	@Test
	public void testCreateRoom() {
		Player master = new Player();
		master.setGlobalBalance(5000);
		Room room = new Room(master,BlindLevel.BLIND_10_20);
		
		assertEquals(room.getBlindLevel(),BlindLevel.BLIND_10_20);
	}
	
	/**
	 * The method to create new the game
	 * */
	@Test
	public void testCreateNewGame() {
		Player master = new Player();
		master.setGlobalBalance(5000);
		Room room = new Room(master,BlindLevel.BLIND_10_20);
		Game game = room.createNewGame();
		
		
		assertEquals(game.getListPlayer().size(),1);
	}
	
	/**
	 * The method to test same room from origin and event source
	 * */
	@Test
	public void testJoinRoom1() {
		Player master = new Player("master");
		Player player2 = new Player("Player 2");
		master.setGlobalBalance(5000);
        player2.setGlobalBalance(5000);
		Room room = new Room(master,BlindLevel.BLIND_10_20);
		
		// for sure candidate is null
		candidateRoom =null;
		
		room.addRoomListener(new RoomListener() {
			
			@Override
			public void actionPerformed(AbstractRoomEvent event) {
				VisitRoomEvent vrEvent = (VisitRoomEvent)event;
				candidateRoom = vrEvent.getSrc();
				
			}
		});
		
		room.addPlayer(player2);
		
		Assert.assertSame(room, candidateRoom);

	}
	
	/**
	 * The method to test same room from origin and event source
	 * */
	@Test
	public void testJoinRoom2() {
		Player master = new Player("master");
		Player player2 = new Player("Player 2");
		master.setGlobalBalance(5000);
		player2.setGlobalBalance(5000);
		Room room = new Room(master,BlindLevel.BLIND_10_20);
		
		// for sure candidate is null
		candidatePlayer =null;
		
		room.addRoomListener(new RoomListener() {
			
			@Override
			public void actionPerformed(AbstractRoomEvent event) {
				VisitRoomEvent vrEvent = (VisitRoomEvent)event;
				candidatePlayer = vrEvent.getP();
				
			}
		});
		
		room.addPlayer(player2);
		
		Assert.assertSame(player2, candidatePlayer);

	}
}
