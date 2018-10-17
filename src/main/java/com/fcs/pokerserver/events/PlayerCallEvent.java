package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;

public class PlayerCallEvent extends AbstractPlayerEvent {
    public long amount;
    private PlayerAction type = PlayerAction.CALL;


    public PlayerCallEvent(Player p) {
        super(p);
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
        return super.toString() + " CALL: "+amount;
    }
}
