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

import com.fcs.pokerserver.Player;

/**
 * An instance of the PlayerEvent class is described the Events of the Player.
 * @category pokerserver.events
 * */
public class PlayerEvent {
	private Player source ;
	private PlayerAction action;
	public HashMap<String, Object> agruments = new HashMap<String, Object>(); 
	
	/**
	 * The constructor with 2 parameters Player and PlayerAction 
	 * @param Player src, PlayerAction action
	 * */
	public PlayerEvent(Player src,PlayerAction action)
	{
		this.setSource(src);
		this.setAction(action);
	}
	
	/**
	 * Return the Player Event Source from the game.
	 * @return Player eventSource
	 * */
	public Player getSource() {
		return source;
	}
	
	/**
	 * The method to set event source of the Player.
	 * @param Player sourceEvent
	 * */
	public void setSource(Player source) {
		this.source = source;
	}
	
	/**
	 * Return the action from the Player event in the game. Like BET, FOLD, CALL, CHECK
	 * @see PlayerAction
	 * @return PlayerAction action.
	 * */
	public PlayerAction getAction() {
		return action;
	}
	
	/**
	 * The method to set the Action for the Player in the Game.
	 * @param PlayerAction action
	 * */
	public void setAction(PlayerAction action) {
		this.action = action;
	}
	
	/**
	 * Override the String method to return the PlayerEvent String.
	 * @return String playerEventString
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
