package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;

public class GetTurnPlayerEvent extends AbstractPlayerEvent {
    public GetTurnPlayerEvent(Player p) {
        super(p);
    }
}
