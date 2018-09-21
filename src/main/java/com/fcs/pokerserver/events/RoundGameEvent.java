package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;

public class RoundGameEvent extends AbstractGameEvent {
    public RoundGameEvent(Game g, GameAction type){
        super(g);
        super.setType(type);
    }

    public RoundGameEvent(Game src) {
        super(src);
    }

    public GameAction getType() {
        return super.getType();
    }

    public void setType(GameAction type) {
        super.setType(type);
    }
}
