package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;

import java.util.HashMap;

public class PlayerFoldEvent extends AbstractPlayerEvent {

    public PlayerFoldEvent(Player src) {
        super(src);
    }

    public Player getSrc() {
        return src;
    }

    public void setSrc(Player src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return super.toString() + " FOLD";
    }
}
