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

import com.fcs.pokerserver.events.PlayerAction;
import com.fcs.pokerserver.events.PlayerEvent;
import com.fcs.pokerserver.events.PlayerListenner;
import com.fcs.pokerserver.holder.CardHolder;
import com.fcs.pokerserver.holder.Hand;

public class Player {
	private long balance;
	private long lastBet;
	private long globalBalance;
	private String name;
	private String id;
	
	
	private CardHolder playerHand = new CardHolder();
	
	private PlayerListenner listenner;
	
	public void bet(long amount)
	{
		
		assert amount<balance;
		this.lastBet=amount;
		this.balance=this.balance-amount;
		
		if(this.getListenner()!=null) getListenner().actionPerformed(new PlayerEvent(this, PlayerAction.BET));
		
	}
	public long getBalance() {
		return balance;
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
	public PlayerListenner getListenner() {
		return listenner;
	}
	public void setListenner(PlayerListenner listenner) {
		this.listenner = listenner;
	}
	private long currentGameID = -1;
	
	
}
