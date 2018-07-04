package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Player;

public class PlayerEvent {
	private Player source ;
	private PlayerAction action;
	public PlayerEvent(Player src,PlayerAction action)
	{
		this.setSource(src);
		this.setAction(action);
	}
	public Player getSource() {
		return source;
	}
	public void setSource(Player source) {
		this.source = source;
	}
	public PlayerAction getAction() {
		return action;
	}
	public void setAction(PlayerAction action) {
		this.action = action;
	}
}
