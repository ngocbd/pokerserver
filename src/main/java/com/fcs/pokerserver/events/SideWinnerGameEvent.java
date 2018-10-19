package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.holder.Hand;

import java.util.List;

public class SideWinnerGameEvent extends AbstractGameEvent {
    private List<Player> winners;
    private List<Hand> Hands;
    private String rank;

    public SideWinnerGameEvent(Game src) {
        super(src);
        this.setType(GameAction.SIDEWINNER);
    }

    public List<Player> getWinners() {
        return winners;
    }

    public void setWinners(List<Player> winners) {
        this.winners = winners;
    }

    public List<Hand> getHands() {
        return Hands;
    }

    public void setHands(List<Hand> hands) {
        Hands = hands;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
