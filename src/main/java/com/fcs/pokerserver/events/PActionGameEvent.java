package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;

public class PActionGameEvent extends AbstractGameEvent {
    AbstractPlayerEvent pe;
    public PActionGameEvent(Game src) {
        super(src);
        super.setType(GameAction.PLAYEREVENT);
    }

    public PActionGameEvent(Game src, AbstractPlayerEvent e) {
        super(src);
        this.pe = e;
    }

    public AbstractPlayerEvent getPE() {
        return pe;
    }

    public void setE(AbstractPlayerEvent e) {
        this.pe = e;
    }

    public GameAction getType() {
        return super.getType();
    }
}
