package com.fsc.pokerserver.test;

import com.fcs.pokerserver.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AutoNextRoundTest {
    private Room room;
    private Player master;

    @Before
    public void setUp() {
        master = new Player("master(P1-sb)");
        master.setGlobalBalance(5000);
        master.setCOUNTDOWN_DELAY(5 * 1000);
        room = new Room(master, BlindLevel.BLIND_10_20);
    }

    @Test
    public void autoNextToFlop() {
        Game game = room.createNewGame();
        Player player2 = new Player("p2-bb");
        player2.setGlobalBalance(5000);
        player2.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player2);

        Player player3 = new Player("p3");
        player3.setGlobalBalance(5000);
        player3.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player3);
        game.setDealer(player3);
        game.startGame();

        game.preflop();
        player3.bet(30);
        master.bet(20);
        player2.bet(10);

        Assert.assertTrue(game.getStatus() == GameStatus.FLOP);
        Assert.assertTrue(game.getCurrentPlayer() == master);
    }

    @Test
    public void autoNextToTurn() {
        Game game = room.createNewGame();
        Player player2 = new Player("p2-bb");
        player2.setGlobalBalance(5000);
        player2.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player2);

        Player player3 = new Player("p3");
        player3.setGlobalBalance(5000);
        player3.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player3);
        game.setDealer(player3);
        game.startGame();

        game.preflop();
        player3.bet(30);
        master.bet(20);
        player2.bet(10);

        //GAME IS FLOP NOW.
        master.check();
        Assert.assertTrue(game.getStatus() == GameStatus.FLOP);
        Assert.assertTrue(game.getCurrentPlayer() == player2);
        player2.check();
        Assert.assertTrue(game.getStatus() == GameStatus.FLOP);
        Assert.assertTrue(game.getCurrentPlayer() == player3);
        player3.check();
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == master);

    }

    @Test
    public void autoNextToRiver() {
        Game game = room.createNewGame();
        Player player2 = new Player("p2-bb");
        player2.setGlobalBalance(5000);
        player2.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player2);

        Player player3 = new Player("p3");
        player3.setGlobalBalance(5000);
        player3.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player3);
        game.setDealer(player3);
        game.startGame();

        game.preflop();
        player3.bet(30);
        master.bet(20);
        player2.bet(10);

        //GAME IS FLOP NOW.
        master.check();
        Assert.assertTrue(game.getStatus() == GameStatus.FLOP);
        Assert.assertTrue(game.getCurrentPlayer() == player2);
        player2.check();
        Assert.assertTrue(game.getStatus() == GameStatus.FLOP);
        Assert.assertTrue(game.getCurrentPlayer() == player3);
        player3.check();

        //GAME IS TURN NOW.
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == master);
        master.bet(20);
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == player2);
        player2.bet(20);
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == player3);
        player3.fold();
        //GAME IS RIVER NOW.
        Assert.assertTrue(game.getStatus() == GameStatus.RIVER);
        Assert.assertTrue(game.getCurrentPlayer() == master);
    }

    @Test
    public void autoNextToEndGame() {
        Game game = room.createNewGame();
        Player player2 = new Player("p2-bb");
        player2.setGlobalBalance(5000);
        player2.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player2);

        Player player3 = new Player("p3");
        player3.setGlobalBalance(5000);
        player3.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player3);
        game.setDealer(player3);
        game.startGame();

        game.preflop();
        player3.bet(30);
        master.bet(20);
        player2.bet(10);

        //GAME IS FLOP NOW.
        master.check();
        Assert.assertTrue(game.getStatus() == GameStatus.FLOP);
        Assert.assertTrue(game.getCurrentPlayer() == player2);
        player2.check();
        Assert.assertTrue(game.getStatus() == GameStatus.FLOP);
        Assert.assertTrue(game.getCurrentPlayer() == player3);
        player3.check();

        //GAME IS TURN NOW.
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == master);
        master.bet(20);
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == player2);
        player2.bet(20);
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == player3);
        player3.fold();
        //GAME IS RIVER NOW.
        Assert.assertTrue(game.getStatus() == GameStatus.RIVER);
        Assert.assertTrue(game.getCurrentPlayer() == master);

        master.check();
        player2.bet(10);
        Assert.assertTrue(game.getStatus() == GameStatus.RIVER);
        Assert.assertTrue(game.getCurrentPlayer() == master);
        master.bet(10);
        //GAME IS END NOW
        Assert.assertTrue(game.getStatus() == GameStatus.END_HAND);
        System.out.println("Winners: " + game.getWinners());
    }

    @Test
    public void autoNextToEndGameSooner() {
        Game game = room.createNewGame();
        Player player2 = new Player("p2-bb");
        player2.setGlobalBalance(5000);
        player2.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player2);

        Player player3 = new Player("p3");
        player3.setGlobalBalance(5000);
        player3.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player3);
        game.setDealer(player3);
        game.startGame();

        game.preflop();
        player3.bet(30);
        master.bet(20);
        player2.bet(10);

        //GAME IS FLOP NOW.
        master.check();
        Assert.assertTrue(game.getStatus() == GameStatus.FLOP);
        Assert.assertTrue(game.getCurrentPlayer() == player2);
        player2.check();
        Assert.assertTrue(game.getStatus() == GameStatus.FLOP);
        Assert.assertTrue(game.getCurrentPlayer() == player3);
        player3.check();

        //GAME IS TURN NOW.
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == master);
        master.bet(20);
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == player2);
        player2.bet(20);
        Assert.assertTrue(game.getStatus() == GameStatus.TURN);
        Assert.assertTrue(game.getCurrentPlayer() == player3);
        player3.fold();
        //GAME IS RIVER NOW.
        Assert.assertTrue(game.getStatus() == GameStatus.RIVER);
        Assert.assertTrue(game.getCurrentPlayer() == master);

        master.check();
        player2.bet(10);
        Assert.assertTrue(game.getStatus() == GameStatus.RIVER);
        Assert.assertTrue(game.getCurrentPlayer() == master);
        master.fold();
        //GAME IS END SOON NOW
        Assert.assertTrue(game.getStatus() == GameStatus.END_HAND);
        Assert.assertSame(game.getWinners().get(0),player2);
        System.out.println("Winners: "+game.getWinners());
    }
}
