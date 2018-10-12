package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.holder.Hand;

import java.util.List;

public class EndGameEvent extends AbstractGameEvent {
    private List<Player> playerwins;
    private String rank;
    private List<Hand> bestHands;

    public EndGameEvent(Game src) {
        super(src);
        super.setType(GameAction.ENDED);
    }

    public List<Player> getPlayerwins() {
        return playerwins;
    }

    public void setPlayerwins(List<Player> playerwins) {
        this.playerwins = playerwins;
    }

    public List<Hand> getBestHands() {
        return bestHands;
    }

    public void setBestHands(List<Hand> bestHands) {
        this.bestHands = bestHands;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


    @Override
    public String toString() {
        return super.getSrc().getId() + " " + GameAction.ENDED.toString() + " winner: " + playerwins + " rank: " + rank + " bestHand: " + bestHands;
    }
}
