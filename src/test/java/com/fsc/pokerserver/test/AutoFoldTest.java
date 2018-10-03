package com.fsc.pokerserver.test;

import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AutoFoldTest{
    private Room room;
    private Player master;

    @Before
    public void setUp() {
        master = new Player("master(P1-sb)");
        master.setGlobalBalance(5000);
        room = new Room(master, BlindLevel.BLIND_10_20);
    }

    @Test
    public void autoFold() throws Exception {
        Game game = room.createNewGame();

        Player player2 = new Player("p2-bb");
        player2.setGlobalBalance(5000);
        room.addPlayer(player2);

        Player player3 = new Player("p3");
        player3.setGlobalBalance(5000);
        room.addPlayer(player3);
//
//        Player player4 = new Player("p4");
//        player4.setGlobalBalance(5000);
//        room.addPlayer(player4);
//
//        Player player5 = new Player("p5");
//        player5.setGlobalBalance(5000);
//        room.addPlayer(player5);

        game.setDealer(player3);
        game.startGame();
        game.preflop();
//        Thread.sleep(1*1000);
        player3.fold();
        game.endGame();
        System.out.println(game.getRank());
//        master.fold();
//        Thread.sleep(5*1000);
//        Assert.assertTrue(master.isSittingOut());


    }
}
