package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;

public class PlayerCheckEvent extends AbstractPlayerEvent {

    public PlayerCheckEvent(Player src) {
        super(src);
    }

    public Player getSrc() {
        return src;
    }

    public void setSrc(Player src) {
        this.src = src;
    }

    @Override
    public String toString()
    {
        return super.toString() + " CHECK";
    }
}
