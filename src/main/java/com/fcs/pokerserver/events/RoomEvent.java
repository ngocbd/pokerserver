package com.fcs.pokerserver.events;

import java.util.HashMap;

import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Room;

public class RoomEvent {
	private Room source ;
	private RoomAction action;
	public HashMap<String, Object> agruments = new HashMap<String, Object>(); 
	public RoomEvent(Room src,RoomAction action)
	{
		this.source = src;
		this.action = action;
	}
	
	public Room getSource() {
		return source;
	}
	public void setSource(Room source) {
		this.source = source;
	}
	public RoomAction getAction() {
		return action;
	}
	public void setAction(RoomAction action) {
		this.action = action;
	}
	
}
