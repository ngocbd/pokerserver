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

import com.fcs.pokerserver.Game;

/**
 * An instance of the GameEvent class is described the Events of the Game.
 * @category com > fcs > pokerserver > events
 * */
public class GameEvent {
	private Game source ;
	private GameAction action;
	public HashMap<String, Object> agruments = new HashMap<String, Object>(); 
	
	/**
	 * The constructor with 2 parameters Game and GameAction 
	 * @param Game src, GameAction action
	 * */
	public GameEvent(Game src,GameAction action)
	{
		this.source = src;
		this.action = action;
	}
	
	/**
	 * Return the Game Event Source from the game.
	 * @return Game eventSource
	 * */
	public Game getSource() {
		return source;
	}
	
	/**
	 * The method to set event source of the game.
	 * @param Game sourceEvent
	 * */
	public void setSource(Game source) {
		this.source = source;
	}
	
	/**
	 * Return the action from the game event in the game. Like CREATED, WAITTING, PREFLOP, FLOP, TURN, RIVER, ENDED, PLAYEREVENT
	 * @see GameAction
	 * @return GameAction action.
	 * */
	public GameAction getAction() {
		return action;
	}
	
	/**
	 * The method to set the Game Action in the Game.
	 * @param GameAction action
	 * */
	public void setAction(GameAction action) {
		this.action = action;
	}
	
	/**
	 * Override the String method to return the GameEvent String.
	 * @return String gameEventString
	 * */
	@Override
	public String toString()
	{
		String str="";
		for (String key : agruments.keySet()) {
			str+=key+":"+agruments.get(key);
		
		}
		return source.getId() + action.toString()+str;
	}
}
