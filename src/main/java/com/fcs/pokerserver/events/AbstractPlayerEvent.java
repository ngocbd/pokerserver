package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;

public abstract class AbstractPlayerEvent {
    public Player src;
    public AbstractPlayerEvent(Player p){
        this.src = p;
    }

    public Player getSrc() {
        return src;
    }

    public void setSrc(Player src) {
        this.src = src;
    }
}
