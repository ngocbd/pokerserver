package com.fsc.pokerserver.test;

import com.fcs.pokerserver.*;
import com.fcs.pokerserver.holder.Board;
import com.fcs.pokerserver.holder.Hand;
import org.junit.Assert;
import org.junit.Test;

public class TestSidePot {
    @Test
    public void sidepot_2PlayersPlaying() {
        Player hbg1 = new Player("hbg1");
        hbg1.setGlobalBalance(5000);
        Room room = new Room(hbg1, BlindLevel.BLIND_10_20);
        Game game = room.createNewGame();
        Player hbg2 = new Player("hbg2");
        hbg2.setGlobalBalance(5000);
        room.addPlayer(hbg2);

        game.setDealer(hbg1);
        game.startGame();
        //In Preflop, master bet 10, player2 bet 20.
        hbg2.bet(500);
        hbg1.bet(490);
        //Now is flop.
        hbg2.fold();

        //NNext Game. hbg1 - 1510, hbg2- 490
        room.nextGame();
        room.getCurrentGame().startGame();
        System.out.println("hbg1 gamebet: " + hbg1.getGameBet());
        System.out.println("hbg2 gamebet: " + hbg2.getGameBet());
        hbg1.bet(10);
        hbg2.check();

        hbg1.check();
        hbg2.check();
        Assert.assertEquals(GameStatus.TURN, room.getCurrentGame().getStatus());
        hbg1.check();
        hbg2.check();

        room.getCurrentGame().setBoard(new Board(Card.TWO_OF_DIAMONDS, Card.EIGHT_OF_SPADES, Card.FOUR_OF_CLUBS,
                Card.TEN_OF_HEARTS, Card.NINE_OF_HEARTS));
        hbg1.setPlayerHand(new Hand(Card.NINE_OF_DIAMONDS, Card.NINE_OF_SPADES));
        hbg2.setPlayerHand(new Hand(Card.THREE_OF_HEARTS, Card.FOUR_OF_SPADES));

        hbg1.bet(500);
        hbg2.allIn();

        Assert.assertEquals(GameStatus.END_HAND, game.getStatus());
        Assert.assertEquals(2000, hbg1.getBalance() + hbg2.getBalance());
        System.out.println("hbg1: " + hbg1.getBalance());

    }

    @Test
    public void sidePot_1Winner() {
        Player master = new Player("Master-P1");
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

        master.setBalance(1020);
        player2.setBalance(2020);
        player3.setBalance(3020);
        player4.setBalance(4020);
        player5.setBalance(5020);

        /**
         * Situation off 5 players allin
         * There are 4 pot
         * MainPot: 1020*5 = 5100
         * Sidepot 1: 1000*4 = 4000
         * Sidepot 2: 1000*3 =3000
         * Sidepot 3:1000*2=2000
         * */

        /**
         * Preflop now.*/
        game.startGame();
        player3.bet(20);
        player4.bet(20);
        player5.bet(20);
        master.bet(10);
        player2.check();
        /**
         * FLOP now*/
        Assert.assertEquals(GameStatus.FLOP, game.getStatus());
        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();
        /**
         * TURN now*/
        Assert.assertEquals(GameStatus.TURN, game.getStatus());
        master.check();
        player2.check();
        player3.check();
        player4.check();
        player5.check();
        System.out.println("Pot: " + game.getPotBalance());

        /**
         * RIVER now*/
        Assert.assertEquals(GameStatus.RIVER, game.getStatus());
        master.allIn();
        player2.allIn();
        player3.allIn();
        player4.allIn();

        //SET BOARD AND PLAYER HAND IN ORDER TO IDENTIFY WINNERS.
        game.setBoard(new Board(Card.TWO_OF_SPADES
                , Card.THREE_OF_CLUBS
                , Card.FOUR_OF_SPADES
                , Card.FIVE_OF_DIAMONDS
                , Card.SEVEN_OF_CLUBS));
        //Master win mainpot.
        master.setPlayerHand(new Hand(Card.FOUR_OF_DIAMONDS, Card.FIVE_OF_HEARTS));
        //player2 win second pot (1st sidepot)
        player2.setPlayerHand(new Hand(Card.SEVEN_OF_SPADES, Card.EIGHT_OF_DIAMONDS));
        //player3 win third pot (2nd sidepot)
        player3.setPlayerHand(new Hand(Card.QUEEN_OF_SPADES, Card.KING_OF_DIAMONDS));
        player4.setPlayerHand(new Hand(Card.QUEEN_OF_DIAMONDS, Card.JACK_OF_HEARTS));
        player5.setPlayerHand(new Hand(Card.QUEEN_OF_CLUBS, Card.TEN_OF_DIAMONDS));
        System.out.println("Pot: " + game.getPotBalance());
        player5.allIn();
        System.out.println("winner: " + game.getWinners());
        /**
         * END-GAME now*/
        Assert.assertEquals(GameStatus.END_HAND, game.getStatus());
        //master win mainpot of (1020*5), 1020 of each player bet
        Assert.assertEquals(5100, master.getBalance());
        Assert.assertEquals(4000, player2.getBalance());
        Assert.assertEquals(3000, player3.getBalance());
        Assert.assertEquals(2000, player4.getBalance());
        //Finally player 5 get his betting of 1000 back due to nobody can match his bet.
        Assert.assertEquals(1000, player5.getBalance());
        Assert.assertEquals(master.getBalance() + player2.getBalance() + player3.getBalance() + player4.getBalance() + player5.getBalance(), game.getPotBalance());
    }
}
