package com.fsc.pokerserver.test;

import com.fcs.pokerserver.*;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AutoFoldTest {
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
    public void autoFold_Only1Remaining_endGameSooner() throws Exception {
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
        //p1 bet 10, p2 bet 20 already. Now turn P3
        Thread.sleep(3 * 1000);
        player3.bet(20);
        master.bet(10);

        //p1 bet 10, p2 bet 1. Now turn P3 autofold
        game.flop();
        Thread.sleep(3 * 1000);
        master.bet(10);
        Thread.sleep(3 * 1000);
        player2.bet(10);
        Thread.sleep(6 * 1000);
        System.out.println("is P3 fold: " + player3.isSittingOut());

        //p1 check, p2 autofold. Finally check winner is p1.
        game.turn();
        Thread.sleep(3 * 1000);
        master.check();
        Thread.sleep(6 * 1000);
        System.out.println("is P2 fold: " + player2.isSittingOut());
        System.out.println("List Winner size: " + game.getWinners().size());
        Assert.assertSame(game.getStatus(), GameStatus.END_HAND);
        Assert.assertSame(game.getWinners().get(0), master);
    }

    @Test
    public void autoFold_foldDouble_endGameSooner() throws Exception {
        Game game = room.createNewGame();
        Player player2 = new Player("p2-bb");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);

        Player player3 = new Player("p3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
        game.setDealer(player3);
        game.startGame();

        game.preflop();
        //p1 bet 10, p2 bet 20 already. Now turn P3
        Thread.sleep(3 * 1000);
        player3.bet(20);
        master.bet(10);

        //p1 bet 10, p2 bet 1. Now turn P3 autofold - and double fold
        game.flop();
        Thread.sleep(3 * 1000);
        master.bet(10);
        Thread.sleep(3 * 1000);
        player2.bet(10);
        Thread.sleep(6 * 1000);
        System.out.println("is P3 fold: " + player3.isSittingOut());
        player3.fold();

        //p1 check, p2 autofold. Finally check winner is p1.
        game.turn();
        Thread.sleep(3 * 1000);
        master.check();
        Thread.sleep(6 * 1000);
        System.out.println("is P2 fold: " + player2.isSittingOut());
        System.out.println("List Winner size: " + game.getWinners().size());
        Assert.assertSame(game.getStatus(), GameStatus.END_HAND);
        Assert.assertSame(game.getWinners().get(0), master);
    }

    @Test
    public void autoFold_foldDouble_endGameNormal() throws Exception {
        Game game = room.createNewGame();
        Player player2 = new Player("p2-bb");
        player2.setGlobalBalance(5000);
        player2.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player2);

        Player player3 = new Player("p3");
        player3.setGlobalBalance(5000);
        player3.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player3);

        Player player4 = new Player("p4");
        player4.setGlobalBalance(5000);
        player4.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player4);
        Player player5 = new Player("p5");
        player5.setGlobalBalance(5000);
        player5.setCOUNTDOWN_DELAY(5 * 1000);
        room.addPlayer(player5);
        game.setDealer(player5);
        game.startGame();

        game.preflop();
        //p1 bet 10, p2 bet 20 already. Now turn P3,p4,p5
        Thread.sleep(3 * 1000);
        player3.bet(20);
        player4.bet(20);
        Thread.sleep(2 * 1000);
        player5.bet(20);
        Thread.sleep(4 * 1000);
        master.bet(10);
        //p1, p2 bet 10. Now turn P3 double-fold, p4 p5 bet 10.
        game.flop();
        Thread.sleep(3 * 1000);
        master.bet(10);
        Thread.sleep(1 * 1000);
        player2.bet(10);
        Thread.sleep(6 * 1000);
        System.out.println("is P3 fold: " + player3.isSittingOut());
        player3.fold();
        player4.bet(10);
        player5.bet(10);

        //p1 check, p2 double-fold.  p4,p5 bet 20 then p1 bet 20.
        game.turn();
        Thread.sleep(2 * 1000);
        master.check();
        Thread.sleep(6 * 1000);
        System.out.println("is P2 fold: " + player2.isSittingOut());
        player2.fold();
        player4.bet(20);
        player5.bet(20);
        Thread.sleep(2 * 1000);
        master.bet(20);

        //p1 doublefold,  p4,p5 check.
        game.river();
        Thread.sleep(6*1000);
        System.out.println("is P1 fold: " + master.isSittingOut());
        master.fold();
        player4.check();
        player5.check();
        game.endGame();
        System.out.println("List Winner size: " + game.getWinners().size());
        Assert.assertTrue(master.isSittingOut());
        Assert.assertTrue(player2.isSittingOut());
        Assert.assertTrue(player3.isSittingOut());
    }

}
