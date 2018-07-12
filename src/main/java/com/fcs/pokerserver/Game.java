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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcs.pokerserver.events.GameAction;
import com.fcs.pokerserver.events.GameEvent;
import com.fcs.pokerserver.events.GameListener;
import com.fcs.pokerserver.events.PlayerAction;
import com.fcs.pokerserver.events.PlayerEvent;
import com.fcs.pokerserver.events.PlayerListener;
import com.fcs.pokerserver.holder.Board;

import junit.framework.AssertionFailedError;

public class Game implements PlayerListener {

	private List<Player> listPlayer = new ArrayList<Player>();
	private Board board = new Board();
	private Deck deck = null;
	private long id;
	private long potBalance = 0;
	private long currentRoundBet = 0;
	private short round=0;
	private Room room;
	private GameStatus status;
	private Player dealer;
	private Player bigBlind;
	private Player smallBlind;
	private Player currentPlayer = null;
	
	
	
	private LocalDateTime startTime = null; // meaning not started  
	
	private List<GameListener> listeners= new ArrayList<GameListener>();
	
	public Game(Room room) {
		this.room = room;
		this.setId(System.currentTimeMillis());
		this.deck = new Deck();
		this.deck.initDeck();
		this.deck.shuffleDeck();
		this.setStatus(GameStatus.NOT_STARTED);

	}

	public void startGame() {
		assert this.listPlayer.size() >=2;

		// setting postion of player
		this.setStatus(GameStatus.SEATING);
		this.startTime = LocalDateTime.now();
		
		
	}
	
	public void preflop() {
		assert this.listPlayer.size() >= 2;

		this.setStatus(GameStatus.PREFLOP);
		long betBigBlind = this.getRoom().getBlindLevel().getBigBlind();
		long betSmallBlind = this.getRoom().getBlindLevel().getSmallBlind();
		this.setCurrentPlayer(this.getSmallBlind());
		this.getSmallBlind().bet(betSmallBlind);
		this.setCurrentPlayer(this.getBigBlind());
		this.getBigBlind().bet(betBigBlind);
		
		
		//this.potBalance += (betBigBlind + betSmallBlind);

		//this.deck.dealCard();
		// deal 2 card for each player // unordered // begin from master // need to fix to begin from dealer
		for (int i = 0; i < 2; i++) {
			for (Player player : listPlayer) {
				Card card = this.deck.dealCard();
				player.getPlayerHand().addCard(card);

			}

		}
		GameEvent gameEvent=  new GameEvent(this, GameAction.PREFLOP);
		this.fireEvent(gameEvent);
		
		// current player is very hard to assign base on number of player
		
		this.setCurrentPlayer(getNextPlayer(this.getBigBlind()));
		this.setRound((short) 1);
		

	}

	public void flop() {
		
		assert this.isNextRoundReady();
		for (Player player : listPlayer) {
			
			player.nextRound();
			
		}
		Card ignoreCard = this.deck.dealCard(); // ignore top card 
		for (int i = 0; i < 3; i++) {
			Card card = this.deck.dealCard();
			getBoard().addCard(card);
		}
		GameEvent gameEvent=  new GameEvent(this, GameAction.FLOP);
		this.fireEvent(gameEvent);
		this.setStatus(GameStatus.FLOP);
		
		this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
		this.setCurrentBet(0);
		
		this.setRound((short) 2);
	}

	public void turn() {
		assert this.isNextRoundReady();
		for (Player player : listPlayer) {
			assert player.didCommandThisTurn();
			player.nextRound();
		}
		Card card = this.deck.dealCard();
		this.getBoard().addCard(card);
		GameEvent gameEvent=  new GameEvent(this, GameAction.TURN);
		this.fireEvent(gameEvent);
		this.setStatus(GameStatus.TURN);
		//TODO need to check before current player fold or not
		this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
		this.setCurrentBet(0);
		
		//this.setRound((short) 3);
	}

	public void river() {

		Card card = this.deck.dealCard();
		this.getBoard().addCard(card);
		GameEvent gameEvent=  new GameEvent(this, GameAction.RIVER);
		this.fireEvent(gameEvent);
		this.setStatus(GameStatus.RIVER);
		
		//this.setRound((short) 4);
	}
	public void endGame()
	{
		this.setStatus(GameStatus.END_HAND);
		GameEvent gameEvent=  new GameEvent(this, GameAction.ENDED);
		this.fireEvent(gameEvent);
		
		
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public long getPotBalance() {
		return potBalance;
	}

	public void setPotBalance(long potBalance) {
		this.potBalance = potBalance;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void addPlayer(Player p) {
		
		// check if timeout join after 15 second then Reject
		if(this.startTime==null || Duration.between(this.startTime  , LocalDateTime.now()).getSeconds()<=15 )
		{
			this.listPlayer.add(p);
			p.addPlayerListener(this);
			
		}
		else
		{
			throw new RejectedExecutionException("Reject player join because 15 seconds is timeout");
		}

	}

	public List<Player> getListPlayer() {
		return listPlayer;
	}

	private void setListPlayer(List<Player> listPlayer) {
		this.listPlayer = listPlayer;
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public GameStatus getStatus() {
		return status;
	}

	private void setStatus(GameStatus status) {
		this.status = status;
	}

	public Player getDealer() {
		return dealer;
	}

	private int getIndexPlayerList(Player player) {
		int index = 0;
		for (; index < this.getListPlayer().size(); index++) {
			if (this.getListPlayer().get(index) == player) {
				break;
			}
		}
		return index;
	}
	public Player getNextPlayer(Player p)
	{
		
		for(int i = 0; i < this.getListPlayer().size(); i++){
			//Find the player we are starting at
			if(this.getListPlayer().get(i).equals(p)){
				//The next player is either the next in the list, or the first in the list if startPlayer is at the end
				return (i == this.getListPlayer().size() - 1) ? this.getListPlayer().get(0) : this.getListPlayer().get(i+1);
			}
		}
		return null;
		
	}

	public void setDealer(Player dealer) {

		// kt dk neeu add 1 thang mat day ko co trong playerlist thi error

		if (listPlayer.contains(dealer)) {

			this.dealer = dealer;
			


			int sizeofPlayerList = this.getListPlayer().size();
			int indexOfDealer = getIndexPlayerList(dealer);
			int indexOfBigBlind = -1;
			int indexOfSmallBlind = -1;
			if (sizeofPlayerList >= 3) {
				if (indexOfDealer == (sizeofPlayerList - 1)) {

					indexOfSmallBlind = (indexOfDealer + 1) - sizeofPlayerList;
					indexOfBigBlind = indexOfSmallBlind + 1;
				} else if (indexOfDealer == (sizeofPlayerList - 2)) {

					indexOfSmallBlind = indexOfDealer + 1;
					indexOfBigBlind = (indexOfSmallBlind + 1) - sizeofPlayerList;
				} else {

					indexOfSmallBlind = indexOfDealer + 1;
					indexOfBigBlind = indexOfSmallBlind + 1;
				}
				
				//TODO change to use setter method 
				this.bigBlind = listPlayer.get(indexOfBigBlind);
				this.smallBlind = listPlayer.get(indexOfSmallBlind);

			}

		} else {
			throw new AssertionFailedError("Player not in Game");
		}

	}

	public Player getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(Player bigBlind) {
		this.bigBlind = bigBlind;
	}

	public Player getSmallBlind() {
		return smallBlind;
	}

	public void setSmallBlind(Player smallBlind) {
		this.smallBlind = smallBlind;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	public void addGameListener(GameListener gl)
	{
		this.listeners.add(gl);
	}
	private void fireEvent(GameEvent ge)
	{
		for (Iterator iterator = this.listeners.iterator(); iterator.hasNext();) {
			GameListener listener = (GameListener) iterator.next();
			listener.actionPerformed(ge);
			
			
		}
	}
	
	public boolean isNextRoundReady()
	{
		//TODO need more test and code review
		return !this.listPlayer.stream()
				.filter(x->!x.isSittingOut())		
				//.filter(x -> x.getRoundBet() != this.getCurrentRoundBet() || x.getRound() != this.getRound())
				.filter(x -> x.getRoundBet() != this.getCurrentRoundBet() )
				.findAny().isPresent();
		
			
			
		
	}
	public Player getPlayerHasProblem()
	{
		//TODO need more test and code review
		return this.listPlayer.stream()
				.filter(x->!x.isSittingOut())		
				.filter(x -> x.getRoundBet() != this.getCurrentRoundBet() || x.getRound() != this.getRound())
				.findAny().orElse(null);
		
			
			
		
	}
	public String dumpListPlayer()
	{
		ObjectMapper mapper = new ObjectMapper();
		


		String jsonInString ="Error when dump Object";
		try {
			 jsonInString = mapper.writeValueAsString(this.listPlayer);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonInString;
	}
	@Override
	public void actionPerformed(PlayerEvent event) {
		Player p = event.getSource();
		assert p==this.getCurrentPlayer();
//		System.out.println(event.getAction());
		if(listPlayer.contains(p))
		{
			if(event.getAction()==PlayerAction.BET)
			{
				assert p.getRoundBet()>=this.currentRoundBet;
				
				
				this.potBalance+= (long)event.agruments.get("amount");
				this.currentRoundBet=p.getRoundBet(); // set current bet equal to this bet amount
				
				
				//TODO Temporary set check next round for game
				// if next round ready then next Player will be left person of dealer
				if(isNextRoundReady())
				{
					this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
				}
				else
				{
				
				
					Player next = this.getNextPlayer(p);
					if(next!=null)
					{
						this.setCurrentPlayer(next);
					}				
				}
				
			}
			
			if(event.getAction()==PlayerAction.FOLD)
			{
				this.setCurrentPlayer(this.getNextPlayer(p));
			}
			
			if(event.getAction()==PlayerAction.CHECK)
			{
				this.setCurrentPlayer(this.getNextPlayer(p));
			}
		}
		
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player p) {
		this.currentPlayer = p;
		this.currentPlayer.myTurn();
		
	}

	public long getCurrentRoundBet() {
		return currentRoundBet;
	}

	public void setCurrentBet(long currentBet) {
		this.currentRoundBet = currentBet;
	}

	public short getRound() {
		return round;
	}

	public void setRound(short round) {
		this.round = round;
	}

}
