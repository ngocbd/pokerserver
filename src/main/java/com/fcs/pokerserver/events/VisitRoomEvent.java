package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

public class VisitRoomEvent extends AbstractRoomEvent {
    private RoomAction type;
    private Player p;
    public VisitRoomEvent(Room src) {
        super(src);
    }

    public RoomAction getType() {
        return type;
    }

    public void setType(RoomAction type) {
        this.type = type;
    }

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return getSrc().getRoomID() + type.toString()+" player: "+p;
    }
}
