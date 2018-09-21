package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Room;

public class GameActRoomEvent extends AbstractRoomEvent {
    private AbstractGameEvent e;
    public GameActRoomEvent(Room src) {
        super(src);
    }

    public AbstractGameEvent getE() {
        return e;
    }

    public void setE(AbstractGameEvent e) {
        this.e = e;
    }
}
