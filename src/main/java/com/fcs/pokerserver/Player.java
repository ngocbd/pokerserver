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

import com.fcs.pokerserver.events.PlayerAction;
import com.fcs.pokerserver.events.PlayerEvent;
import com.fcs.pokerserver.events.PlayerListener;
import com.fcs.pokerserver.holder.Hand;

/**
 * An instance of the Player class is created Player when user want to play Poker Game.
 * @category com > fcs > pokerserver
 * */
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
	private String token = null;
	private boolean commandThisTurn=false;

	private Hand playerHand = new Hand();
	private List<PlayerListener> listeners = new ArrayList<PlayerListener>();

	private Game currentGame = null;


	/**
	 * Constructor set Id for the Player is current time millis 
	 * */
	public Player()
	{
		this.setId(String.valueOf(System.currentTimeMillis()));
	}
	
	/**
	 * other constructor set name and id for Player.
	 * @param String name
	 * */
	public Player(String name)
	{
		this.name=name;
		this.setId(String.valueOf(System.currentTimeMillis()));
	}

	
	/**
	 * The method to the Player bet the chip in the current game.
	 * @param long amount
	 * @throws AssertionError if the bet amount > the Player's balance or the Player is sitting out
	 * */
	public void bet(long amount)
	{
		assert amount<this.balance;
		
		assert this.sittingOut=false;
		this.setRoundBet(this.getRoundBet() + amount);
		this.gameBet+=amount;
		this.balance=this.balance-amount;
		
		PlayerEvent pe = new PlayerEvent(this, PlayerAction.BET);
		pe.agruments.put("amount", amount);
		this.fireEvent(pe);
	}
	
	/**
	 * The method to add more listener to this Player.
	 * @param PlayerListener pl
	 * */
	public void addPlayerListener(PlayerListener pl)
	{
		this.listeners.add(pl);
	}
	
	
	/**
	 * The method to Player is next the round.
	 * */
	public void nextRound()
	{
		this.setRoundBet(0);
		this.round++;
	}
	
	/**
	 * The Player create a new game.
	 * 
	 * */
//	public void newGame()
//	{
//		this.setRoundBet(0);
//		this.gameBet=0;
//		this.round=0;
//	}
	
	/**
	 * The Player want to fold in the game.
	 * */
	public void fold()
	{
		PlayerEvent pe = new PlayerEvent(this, PlayerAction.FOLD);
		
		this.fireEvent(pe);
		this.sittingOut=true;
	}
	
	/**
	 * The Player want to check in the game.
	 * */
	public void check()
	{
		PlayerEvent pe = new PlayerEvent(this, PlayerAction.CHECK);	
		this.fireEvent(pe);
	}
	
	/**
	 * The method fire PlayerEvent to all listener.
	 * */
	private void fireEvent(PlayerEvent pe)
	{
		for (Iterator<PlayerListener> iterator = this.listeners.iterator(); iterator.hasNext();) {
			PlayerListener listener =  iterator.next();
			listener.actionPerformed(pe);
		}
	}
	
	/**
	 * Return the Balance of the Player
	 * @return long balance
	 * */
	public long getBalance() {
		return balance;
	}
	
	
	public void myTurn()
	{
		// where to write code
		
	}
	
	/**
	 * The method to set balance of the Player
	 * @param long balance
	 * */
	public void setBalance(long balance) {
		this.balance = balance;
	}
	
	/**
	 * The method to get the global balance
	 * @param long globalBalance
	 * */
	public long getGlobalBalance() {
		return globalBalance;
	}
	
	/**
	 * The method to set the global balance
	 * @param long globalBalance
	 * */
	public void setGlobalBalance(long globalBalance) {
		this.globalBalance = globalBalance;
	}
	
	/**
	 * The method to get the Player's name in the game.
	 * @return String name
	 * */
	public String getName() {
		return name;
	}
	
	/**
	 * The method to set the name for the Player
	 * @param String name
	 * */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * The method to get the Player's Id in the game.
	 * @return String id
	 * */
	public String getId() {
		return id;
	}
	
	/**
	 * The method to set the Id for the Player
	 * @param String id
	 * */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the Hand of the Player. The player has 2 cards in the hand.
	 * @return Hand playerHand
	 * */
	public Hand getPlayerHand() {
		return playerHand;
	}
	
	/**
	 * The method to set the cards on hand of the Player
	 * @param Hand playerHand
	 * */
	public void setPlayerHand(Hand playerHand) {
		this.playerHand = playerHand;
	}
	
	/**
	 * The method to get the Player's pot(bet value) in the game.
	 * @return long potGame
	 * */
	public long getGameBet() {
		return gameBet;
	}
	
	/**
	 * The method to set the Player's pot(bet value) in the game.
	 * @param long gameBet
	 * */
	public void setGameBet(long gameBet) {
		this.gameBet = gameBet;
	}
	
	/**
	 * The method to get the Player's bet in the round.
	 * @return long the roundBet.
	 * */
	public long getRoundBet() {
		return roundBet;
	}
	
	/**
	 * The method to set the Player's round bet.
	 * @param long roundBet
	 * */
	public void setRoundBet(long roundBet) {
		this.roundBet = roundBet;
	}
	
	/**
	 * The method to check the Player is sitting out in the game.
	 * @return boolean sittingOut
	 * */
	public boolean isSittingOut() {
		return sittingOut;
	}
	
	/**
	 * The method to reset is sitting out for the Player
	 * @param boolean sittingOut 
	 * */
	public void setSittingOut(boolean sittingOut) {
		this.sittingOut = sittingOut;
	}
	
	/**
	 * The method to the Player get the current game.
	 * @return Game currentGame.
	 * */
	public Game getCurrentGame() {
		return currentGame;
	}
	
	/**
	 * The method to set the current game.
	 * @param Game currentGame.
	 * */
	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}
	
	/**
	 * Return the round of the current game.
	 * @return short round.
	 * */
	public short getRound() {
		return round;
	}
	
	/**
	 * The method to reset the round of the current game.
	 * @param short round.
	 * */
	public void setRound(short round) {
		this.round = round;
	}
	
	

	/**
	 * Return the Player's information by Json type.
	 * @return String jsonString 
	 * */
	public String toString()
	{
		return "{"+"\"Name\": \""+this.getName()+"\", \"Balance\": "+this.balance+", \"RoundBet\": "+this.roundBet+"}";
	}
	
	/**
	 * Return the current Room in the game.
	 * @return Room currentRoom
	 * */
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	/**
	 * The method to set the room in the game.
	 * @param Room currentRoom
	 * */
	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}
	
	/**
	 * Return the Token of Player in the game.
	 * @return String token.
	 * */
	public String getToken() {
		return token;
	}
	
	/**
	 * The method to reset Token for the Player
	 * @param String token
	 * */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Return boolean value to check the Player command this turn.
	 * @return boolean commandThisTurn
	 * */
	public boolean didCommandThisTurn() {
		return commandThisTurn;
	}
	
	/**
	 * The method to set value the Player command this turn.
	 * @param boolean commandThisTurn
	 * */
	public void setCommandThisTurn(boolean commandThisTurn) {
		this.commandThisTurn = commandThisTurn;
	}
	
}
