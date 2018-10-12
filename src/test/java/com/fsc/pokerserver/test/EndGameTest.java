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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fcs.pokerserver.*;
import com.fcs.pokerserver.holder.Board;
import com.fcs.pokerserver.holder.Hand;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The class to test the end game.
 *
 * @category com > fcs > pokerserver > test
 */

public class EndGameTest {

    /*--------------------- End Game -----------------------*/

    /**
     * Error order number from player Check, bet, fold before end game.
     */
    @Test
    public void testCheckBetFoldBeforeEndGame() throws Exception {
        Player master = new Player("Room master");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        Player player2 = new Player("Player 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player 5");
        player5.setGlobalBalance(5000);
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
//
        player2.bet(20);
//		player5 need bet before player2
        assertSame(player5, game.getCurrentPlayer());
        player5.bet(30);
        player2.fold();

    }


    /**
     * call end game although current round bet not equal
     */
//    @Test(expected = AssertionError.class)
//    public void testCallEndGameAlthoughCurrentRoundBetNotEqual() {
//        Player master = new Player("Room master 1");
//        master.setGlobalBalance(5000);
//        Room room = new Room(master, BlindLevel.BLIND_10_20);
//
//        Game game = room.createNewGame();
//
//        Player player2 = new Player("Player_1 2");
//        player2.setGlobalBalance(5000);
//        room.addPlayer(player2);
//        Player player3 = new Player("Player_1 3");
//        player3.setGlobalBalance(5000);
//        room.addPlayer(player3);
//        Player player4 = new Player("Player_1 4");
//        player4.setGlobalBalance(5000);
//        room.addPlayer(player4);
//        Player player5 = new Player("Player_1 5");
//        player5.setGlobalBalance(5000);
//        room.addPlayer(player5);
//
//        game.setDealer(player5);
//
//        game.startGame();
//
//        player3.fold();
//        player4.bet(20);
//        player5.bet(30);
//        master.bet(20);
//        player2.bet(10);
//        player4.bet(10);
//
//        master.check();
//        player2.check();
//        player4.fold();
//        player5.bet(10);
//        master.bet(10);
//        player2.bet(10);
//
//        master.check();
//        player2.bet(20);
//        player5.bet(20);
//        master.fold();
//
//        player2.bet(20);
//        player5.bet(30);
//        player2.bet(20);
//
//    }

    /**
     * Get pot from game after end game.
     */
    @Test
    public void testGetPotAfterEndGame() {
        Player master = new Player("Room master 2");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        Player player2 = new Player("Player_2 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player_2 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player_2 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player_2 5");
        player5.setGlobalBalance(5000);
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

    /**
     * Get cards from game after end game.
     */
    @Test
    public void testGetCardsAfterEndGame() {
        Player master = new Player("Room master 3");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        Player player2 = new Player("Player_3 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player_3 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player_3 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player_3 5");
        player5.setGlobalBalance(5000);
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

        assertEquals(game.getBoard().getCardNumber(), 5);
    }

    @Test
    public void testNextGame_noOneLeft() {
        Player master = new Player("Room master 4");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        Player player2 = new Player("Player_4 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player_4 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player_4 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player_4 5");
        player5.setGlobalBalance(5000);
        room.addPlayer(player5);

        game.setDealer(player5);

        game.startGame();
        player3.bet(20);
        player4.bet(20);
        player5.bet(20);
        master.bet(10);
        player2.check();

        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();


        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();

        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();

        game = room.nextGame();
        /**
         * Dealer will be set on player1 (master)*/
        assertSame(game.getDealer(), master);
    }

    @Test
    public void testNextGame_newPlayerJoin() {
        Player master = new Player("Room master 5");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        Player player2 = new Player("Player_5 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player_5 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player_5 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player_5 5");
        player5.setGlobalBalance(5000);
        room.addPlayer(player5);

        game.setDealer(player5);

        game.startGame();
        player3.bet(20);
        player4.bet(20);
        player5.bet(20);
        master.bet(10);
        player2.check();

        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();

        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();

        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();


        System.out.println("status: "+game.getStatus());
        Player player6 = new Player("Player6");
        player6.setGlobalBalance(5000);
        room.addPlayer(player6);
        game = room.nextGame();
        /**
         * Dealer will be set on player6*/

        System.out.println("dl : " +game);
        assertSame(game.getDealer(), player6);
    }

    @Test
    public void testNextGame_PlayersFold() {
        Player master = new Player("Room master 6");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        Player player2 = new Player("Player_6 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player_6 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player_6 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player_6 5");
        player5.setGlobalBalance(5000);
        room.addPlayer(player5);

        game.setDealer(player5);

        game.startGame();
        player3.bet(20);
        player4.bet(20);
        player5.bet(20);
        master.bet(10);
        player2.check();

        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();


        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();

        master.check();
        player2.fold();
        player3.fold();
        player4.check();
        player5.check();

//        for (Player p : game.getListPlayer()){
//            System.out.println(p.getName());
//        }

        game = room.nextGame();


        /**
         * Dealer will be set on player 2*/
        assertSame(game.getDealer(), player2);
    }

    @Test
    public void testNextGame_DealerQuit() {
        Player master = new Player("Room master 7");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        Player player2 = new Player("Player_7 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player_7 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player_7 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player_7 5");
        player5.setGlobalBalance(5000);
        room.addPlayer(player5);

        game.setDealer(player5);

        game.startGame();
        player3.bet(20);
        player4.bet(20);
        player5.bet(20);
        master.bet(10);
        player2.check();

        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();


        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();

        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.fold();

        room.getListPlayer().remove(4);
        game = room.nextGame();


        /**
         * Dealer will be set on P1(master)*/
        assertSame(game.getDealer(), master);
    }

    @Test
    public void testNextGame_DealerQuit2() {
        Player master = new Player("Room master 8");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        /**
         * Player 3 was set as dealer*/
        Game game = room.createNewGame();

        Player player2 = new Player("Player_8 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player_8 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player_8 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player_8 5");
        player5.setGlobalBalance(5000);
        room.addPlayer(player5);

        game.setDealer(player3);

        game.startGame();
        master.bet(20);
        player2.bet(20);
        player3.bet(20);
        player4.bet(10);
        player5.check();

        player4.check();
        player5.check();
        master.check();
        player2.check();
        player3.check();


        player4.check();
        player5.check();
        master.check();
        player2.check();
        player3.check();

        player4.check();
        player5.check();
        master.check();
        player2.check();
        player3.check();

        room.getListPlayer().remove(2);
        game.getListPlayer().remove(2);
        game = room.nextGame();


        /**
         * Dealer will be set on P5*/
        assertSame(game.getDealer(), player5);
    }

    /**
     * Test End Game if Playing The Board situation occured(All players are winners)
     */
    @Test
    public void testGetPotIfPlayingTheBoard() throws InterruptedException {
        Player master = new Player("Room master 9");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();

        Player player2 = new Player("Player_9 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player_9 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player_9 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player_9 5");
        player5.setGlobalBalance(5000);
        room.addPlayer(player5);
        game.setDealer(player5);

        game.startGame();
        //P1 and P2 is SB and BB.

        //Each P bet 30. P3 folded.
        player3.fold();
        player4.bet(20);
        player5.bet(30);
        master.bet(20);
        player2.bet(10);
        player4.bet(10);
        //p4 folded. p1, p2, p5 bet 10 more (40 in total).
        master.check();
        player2.check();
        player4.fold();
        player5.bet(10);
        master.bet(10);
        player2.bet(10);

        //p1,p3,p4 fold. p2,p5 bet 20 more(60 in total)
        master.check();
        player2.bet(20);
        player5.bet(20);
        master.fold();

        //p2,p5 bet 30 more (90 in total)
        player2.bet(30);
        player5.bet(30);
        //total Pot is 250
        /**
         * Setup Playing the Board situation*/
        Board royalFlush = new Board(Card.TEN_OF_HEARTS, Card.JACK_OF_HEARTS, Card.QUEEN_OF_HEARTS, Card.KING_OF_HEARTS, Card.ACE_OF_HEARTS);
        game.setBoard(royalFlush);
        player2.setPlayerHand(new Hand(Card.THREE_OF_HEARTS, Card.SIX_OF_DIAMONDS));
        player5.setPlayerHand(new Hand(Card.THREE_OF_SPADES, Card.SIX_OF_CLUBS));
        System.out.println("winners1: " +player5.getBalance());
        System.out.println("winners2: " +player2.getBalance());
        Thread.sleep(3000);

        assertTrue(player5.getBalance() != player2.getBalance());

        assertTrue(1160 == player5.getBalance());
    }

    @Test
    public void testGetPotIfPlayingTheBoard2() {
        Player master = new Player("Room master 10");
        master.setGlobalBalance(5000);
        Room room = new Room(master, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();
        Player player2 = new Player("Player_10 2");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);
        Player player3 = new Player("Player_10 3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        Player player4 = new Player("Player_10 4");
        player4.setGlobalBalance(5000);
        room.addPlayer(player4);
        Player player5 = new Player("Player_10 5");
        player5.setGlobalBalance(5000);
        room.addPlayer(player5);
        game.setDealer(player5);


        game.startGame();
        //P1 and P2 is SB and BB.
        //Each P bet 30. P3 folded.
        player3.fold();
        player4.bet(20);
        player5.bet(30);
        master.bet(20);
        player2.bet(10);
        player4.bet(10);
        assertEquals(GameStatus.FLOP,game.getStatus());
        //p4 folded. p1, p2, p5 bet 10 more (40 in total).
        master.check();
        player2.check();
        player4.fold();
        player5.bet(10);
        master.bet(10);
        player2.bet(10);
        assertEquals(GameStatus.TURN,game.getStatus());

        // p1, p2,p5 bet 20 more(60 in total)
        master.bet(20);
        player2.bet(20);
        player5.bet(20);
        assertEquals(GameStatus.RIVER,game.getStatus());

        //p2,p5 bet 30 more (90 in total)
        master.bet(30);
        player2.bet(30);
        /**
         * Setup Playing The Board situation*/
        Board royalFlush = new Board(Card.TEN_OF_HEARTS, Card.JACK_OF_HEARTS, Card.QUEEN_OF_HEARTS, Card.KING_OF_HEARTS, Card.ACE_OF_HEARTS);
        game.setBoard(royalFlush);
        player2.setPlayerHand(new Hand(Card.THREE_OF_HEARTS, Card.SIX_OF_DIAMONDS));
        player5.setPlayerHand(new Hand(Card.THREE_OF_SPADES, Card.SIX_OF_CLUBS));
        master.setPlayerHand(new Hand(Card.FOUR_OF_CLUBS, Card.FIVE_OF_SPADES));
        player5.bet(30);
        assertEquals(GameStatus.END_HAND,game.getStatus());

        //total Pot is 250

        assertEquals(1010, master.getBalance());
    }
}
