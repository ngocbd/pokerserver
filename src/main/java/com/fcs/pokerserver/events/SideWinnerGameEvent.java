package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.holder.Hand;

public class SideWinnerGameEvent extends AbstractGameEvent {
    private Player winner;
    private Hand Hand;
    private String rank;

    public SideWinnerGameEvent(Game src) {
        super(src);
        this.setType(GameAction.SIDEWINNER);
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public com.fcs.pokerserver.holder.Hand getHand() {
        return Hand;
    }

    public void setHand(com.fcs.pokerserver.holder.Hand hand) {
        Hand = hand;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
