package com.fcs.pokerserver.automation;

import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class CountDownPlayer extends TimerTask {
    private String id;
    private Player player;
    private Game currentGame;

    public CountDownPlayer(Player player, Game game) {
        id = UUID.randomUUID().toString();
        this.player = player;
        currentGame = player.getCurrentGame();
    }

    public static CountDownPlayer createInstance(Player p, Game g) {
        return new CountDownPlayer(p, g);
    }

    public String getId() {
        return id;
    }

    @Override
    public void run() {
        System.out.println("PID: " + player.getId() + " id: " + id);
        if (currentGame.getCurrentPlayer() == player) {
            player.fold();
            System.out.println("IN " + id + " Player: " + player.getId() + " is folded automaticatlly!");
        }
    }
}
