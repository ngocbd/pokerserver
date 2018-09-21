package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;

public abstract class AbstractGameEvent {
    private Game src;
    private GameAction type;
    public AbstractGameEvent(Game src){
        this.src = src;
    }

    public Game getSrc() {
        return src;
    }

    public void setSrc(Game src) {
        this.src = src;
    }

    public GameAction getType() {
        return type;
    }

    public void setType(GameAction type) {
        this.type = type;
    }
}
