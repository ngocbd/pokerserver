/*
The MIT License (MIT)

Copyright (c) 2018 Ngocbd

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
package com.fcs.pokerserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fcs.pokerserver.events.GameEvent;
import com.fcs.pokerserver.events.GameListener;
import com.fcs.pokerserver.events.PlayerAction;
import com.fcs.pokerserver.events.PlayerEvent;
import com.fcs.pokerserver.events.PlayerListener;
import com.fcs.pokerserver.holder.CardHolder;
import com.fcs.pokerserver.holder.Hand;

public class Player {
	private long balance;
	private long lastBet;
	private long globalBalance;
	private String name;
	private String id;
	 
	
	private CardHolder playerHand = new CardHolder();
	
	private List<PlayerListener> listeners = new ArrayList<PlayerListener>();
	
	public void bet(long amount)
	{
		
		assert amount<balance;
		this.lastBet=amount;
		this.balance=this.balance-amount;
		
		PlayerEvent pe = new PlayerEvent(this, PlayerAction.BET);
		
		this.fireEvent(pe);
		
		
		
	}
	public void fold()
	{
		
		
		
		PlayerEvent pe = new PlayerEvent(this, PlayerAction.FOLD);
		
		this.fireEvent(pe);
		
		
		
	}
	public void check()
	{
		
		
		
		PlayerEvent pe = new PlayerEvent(this, PlayerAction.CHECK);
		
		this.fireEvent(pe);
		
		
		
	}
	private void fireEvent(PlayerEvent pe)
	{
		for (Iterator iterator = this.listeners.iterator(); iterator.hasNext();) {
			PlayerListener listener = (PlayerListener) iterator.next();
			listener.actionPerformed(pe);
			
			
		}
	}
	public long getBalance() {
		return balance;
	}
	
	public void myTurn()
	{
		
		
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public long getGlobalBalance() {
		return globalBalance;
	}
	public void setGlobalBalance(long globalBalance) {
		this.globalBalance = globalBalance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getCurrentGameID() {
		return currentGameID;
	}
	public void setCurrentGameID(long currentGameID) {
		this.currentGameID = currentGameID;
	}
	public CardHolder getPlayerHand() {
		return playerHand;
	}
	void setPlayerHand(Hand playerHand) {
		this.playerHand = playerHand;
	}
	public long getLastBet() {
		return lastBet;
	}
	public void setLastBet(long lastBet) {
		this.lastBet = lastBet;
	}
	public List<PlayerListener> getListenner() {
		return this.listeners;
	}
	public void addPlayerListenner(PlayerListener listener) {
		this.listeners.add(listener);
	}
	private long currentGameID = -1;
	
	
}
