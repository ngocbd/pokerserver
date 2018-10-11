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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.fcs.pokerserver.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The class to test the connect to the game.
 *
 * @category com > fcs > pokerserver > test
 */

public class ConnectGameTest {


    /**
     * Create new Game
     */
    @Test
    public void testCreateGame() {
        Player master;
        master = new Player("Room master");
        master.setGlobalBalance(1000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();
        assertNotNull(game);
    }

    /**
     * Deck of cards is not null
     */
    @Test
    public void testGameDeck() {
        Player master;
        master = new Player("Room master 1");
        master.setGlobalBalance(1000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        assertNotNull(game.getDeck());
    }

    /**
     * shuffle deck of cards
     */
    @Test
    public void testGameShuffleDeck() {
        Player master;
        master = new Player("Room master 2");
        master.setGlobalBalance(1000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();
        Deck deck = new Deck();
        deck.initDeck();
        assertNotEquals(game.getDeck().exportDeck(), deck.exportDeck());
    }

    /**
     * Game start
     */
    @Test
    public void testGameStart() throws InterruptedException {
        Player master;
        master = new Player("Room master 3");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        Player player2 = new Player("p2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("p3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        game.setDealer(player2);
        game.startGame();
		assertEquals(game.getStatus(), GameStatus.PREFLOP);
    }

}
