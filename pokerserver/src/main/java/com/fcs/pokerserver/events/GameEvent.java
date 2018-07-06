package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;

public class GameEvent {
	private Game source ;
	private GameAction action;
	
	public GameEvent(Game src,GameAction action)
	{
		this.source = src;
		this.action = action;
	}
	
	public Game getSource() {
		return source;
	}
	public void setSource(Game source) {
		this.source = source;
	}
	public GameAction getAction() {
		return action;
	}
	public void setAction(GameAction action) {
		this.action = action;
	}
	
}
