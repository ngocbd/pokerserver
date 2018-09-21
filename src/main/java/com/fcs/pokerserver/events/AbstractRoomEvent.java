package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Room;

public abstract class AbstractRoomEvent {
    private Room src;

    public AbstractRoomEvent(Room src) {
        this.src = src;
    }

    public Room getSrc() {
        return src;
    }

    public void setSrc(Room src) {
        this.src = src;
    }
}
