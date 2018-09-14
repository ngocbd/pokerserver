/*
The MIT License (MIT)
Copyright (c) 2018 by Ngocbd
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.fcs.pokerserver.events;

import java.util.HashMap;

import com.fcs.pokerserver.Room;

/**
 * The RoomEvent class is described the Events in the Room.
 * @category com > fcs > pokerserver > events
 * */
public class RoomEvent {
	private Room source ;
	private RoomAction action;
	public HashMap<String, Object> agruments = new HashMap<String, Object>(); 
	
	/**
	 * The constructor with 2 parameters Room and RoomAction 
	 * @param Room src, RoomAction action
	 * */
	public RoomEvent(Room src,RoomAction action)
	{
		this.source = src;
		this.action = action;
	}
	
	/**
	 * Return the Room Event Source from the game.
	 * @return Room eventSource
	 * */
	public Room getSource() {
		return source;
	}
	
	/**
	 * The method to set event source in the Room.
	 * @param Room sourceEvent
	 * */
	public void setSource(Room source) {
		this.source = source;
	}
	
	/**
	 * Return the action in the Room event. Like CREATED, WAITTING, PREFLOP, FLOP, TURN, RIVER, ENDED, PLAYEREVENT
	 * @see RoomAction
	 * @return RoomAction action.
	 * */
	public RoomAction getAction() {
		return action;
	}
	
	/**
	 * The method to set the Action in the Room.
	 * @param RoomAction action
	 * */
	public void setAction(RoomAction action) {
		this.action = action;
	}
	
	/**
	 * Override the String method to return the RoomEvent String.
	 * @return String roomEventString
	 * */
	@Override
	public String toString()
	{
		String str="";
		for (String key : agruments.keySet()) {
			str+=key+":"+agruments.get(key);
		
		}
		return source.getRoomID() + action.toString()+str;
	}
}
