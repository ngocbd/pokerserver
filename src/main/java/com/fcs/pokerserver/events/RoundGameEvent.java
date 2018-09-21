package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;

public class RoundGameEvent extends AbstractGameEvent {
    GameAction type;
    public RoundGameEvent(Game g, GameAction type){
        super(g);
        this.type = type;
    }

    public RoundGameEvent(Game src) {
        super(src);
    }

    public GameAction getType() {
        return type;
    }

    public void setType(GameAction type) {
        this.type = type;
    }
}
