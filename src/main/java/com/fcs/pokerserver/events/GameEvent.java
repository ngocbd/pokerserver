package com.fcs.pokerserver.events;

import java.util.HashMap;
import java.util.Iterator;

import com.fcs.pokerserver.Game;

public class GameEvent {
	private Game source ;
	private GameAction action;
	public HashMap<String, Object> agruments = new HashMap<String, Object>(); 
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
	public String toString()
	{
		String str="";
		for (String key : agruments.keySet()) {
			str+=key+":"+agruments.get(key);
		
		}
		return source.getId() + action.toString()+str;
	}
}
