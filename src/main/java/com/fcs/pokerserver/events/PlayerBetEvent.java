package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;

import java.util.HashMap;

public class PlayerBetEvent extends AbstractPlayerEvent {
    public long amount;

    public PlayerBetEvent(Player source) {
        super(source);
    }


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString()
    {
        return src.getId() + "BET: "+amount;
    }
}
