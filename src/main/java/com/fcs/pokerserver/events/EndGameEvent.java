package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.holder.Hand;

public class EndGameEvent extends AbstractGameEvent {
    String playerwinId;
    String rank;
    Hand bestHand;

    public EndGameEvent(Game src) {
        super(src);
    }

    public String getPlayerwinId() {
        return playerwinId;
    }

    public void setPlayerwinId(String playerwinId) {
        this.playerwinId = playerwinId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Hand getBestHand() {
        return bestHand;
    }

    public void setBestHand(Hand bestHand) {
        this.bestHand = bestHand;
    }
}
