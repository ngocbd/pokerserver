package com.fcs.pokerserver.automation;

import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;

import java.util.TimerTask;

public class CountDownPlayer extends TimerTask {
    private Player player;
    private Game currentGame;

    public CountDownPlayer(Player player, Game game) {
        this.player = player;
        currentGame = player.getCurrentGame();
    }

    @Override
    public void run() {
        if (currentGame.getCurrentPlayer() == player) player.fold();
    }
}
