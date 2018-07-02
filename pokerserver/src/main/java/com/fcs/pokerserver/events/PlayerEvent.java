package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;

public class PlayerEvent {
	Player source ;
	PlayerAction action;
	public PlayerEvent(Player src,PlayerAction action)
	{
		this.source=src;
		this.action=action;
	}
}
