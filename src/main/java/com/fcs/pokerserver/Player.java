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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcs.pokerserver.events.GameEvent;
import com.fcs.pokerserver.events.GameListener;
import com.fcs.pokerserver.events.PlayerAction;
import com.fcs.pokerserver.events.PlayerEvent;
import com.fcs.pokerserver.events.PlayerListener;
import com.fcs.pokerserver.holder.CardHolder;
import com.fcs.pokerserver.holder.Hand;

public class Player {
	private long balance;
	private long roundBet=0;
	private long gameBet=0;
	private short round=0;
	private long globalBalance;
	private String name;
	private String id;
	private boolean sittingOut=false;
	private Room currentRoom = null;
	
	 
	public Player()
	{
		
	}
	public Player(String name)
	{
		this.name=name;
	}
	private CardHolder playerHand = new CardHolder();
	
	private List<PlayerListener> listeners = new ArrayList<PlayerListener>();
	
	public void bet(long amount)
	{
		
		assert amount<this.balance;
		this.setRoundBet(this.getRoundBet() + amount);
		this.gameBet+=amount;
		this.balance=this.balance-amount;
		
		PlayerEvent pe = new PlayerEvent(this, PlayerAction.BET);
		
		pe.agruments.put("amount", amount);
		pe.agruments.put("roundBet", this.getRoundBet());
		pe.agruments.put("gameBet", this.gameBet);
		
		this.fireEvent(pe);
		
		
		
	}
	public void addPlayerListener(PlayerListener pl)
	{
		this.listeners.add(pl);
	}
	public void nextRound()
	{
		this.setRoundBet(0);
		this.round++;
	}
	public void newGame()
	{
		this.setRoundBet(0);
		this.gameBet=0;
		this.round=0;
	}
	public void fold()
	{
		
		
		
		PlayerEvent pe = new PlayerEvent(this, PlayerAction.FOLD);
		
		this.fireEvent(pe);
		this.sittingOut=true;
		
		
		
	}
	public void check()
	{
		
		
		
		PlayerEvent pe = new PlayerEvent(this, PlayerAction.CHECK);
		
		this.fireEvent(pe);
		
		
		
	}
	private void fireEvent(PlayerEvent pe)
	{
		for (Iterator<PlayerListener> iterator = this.listeners.iterator(); iterator.hasNext();) {
			PlayerListener listener =  iterator.next();
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

	public CardHolder getPlayerHand() {
		return playerHand;
	}
	void setPlayerHand(Hand playerHand) {
		this.playerHand = playerHand;
	}
	
	public long getGameBet() {
		return gameBet;
	}
	public void setGameBet(long gameBet) {
		this.gameBet = gameBet;
	}
	public long getRoundBet() {
		return roundBet;
	}
	public void setRoundBet(long roundBet) {
		this.roundBet = roundBet;
	}
	public boolean isSittingOut() {
		return sittingOut;
	}
	public void setSittingOut(boolean sittingOut) {
		this.sittingOut = sittingOut;
	}
	public Game getCurrentGame() {
		return currentGame;
	}
	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}
	public short getRound() {
		return round;
	}
	public void setRound(short round) {
		this.round = round;
	}
	private Game currentGame = null;
	
	public String toString()
	{
		return  this.getName()+","+this.balance+","+this.roundBet;
	}
	public Room getCurrentRoom() {
		return currentRoom;
	}
	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}
	
}
