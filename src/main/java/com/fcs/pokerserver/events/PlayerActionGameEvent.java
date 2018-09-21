package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;

public class PlayerActionGameEvent extends AbstractGameEvent {
    AbstractPlayerEvent pe;
    public PlayerActionGameEvent(Game src) {
        super(src);
        super.setType(GameAction.PLAYEREVENT);
    }

    public PlayerActionGameEvent(Game src, AbstractPlayerEvent e) {
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
    @Override
    public String toString() {
        return super.getSrc().getId() + " " + GameAction.PLAYEREVENT.toString()+" playerEvent: "+pe.toString();
    }
}
