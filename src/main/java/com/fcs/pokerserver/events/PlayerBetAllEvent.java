package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;

public class PlayerBetAllEvent extends AbstractPlayerEvent {
    public long amount;

    public PlayerBetAllEvent(Player p) {
        super(p);
        amount = p.getBalance();
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
