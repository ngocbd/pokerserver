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

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fcs.pokerserver.events.*;
import com.fcs.pokerserver.holder.Hand;
import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.gson.annotations.Expose;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import javax.management.*;

/**
 * An instance of the Player class is created Player when user want to play Poker Game.
 *
 * @category com > fcs > pokerserver
 */
@Entity
public class Player implements PlayerMBean {
    private long balance;
    private long roundBet = 0;
    private long gameBet = 0;
    private short round = 0;
    private long globalBalance;
    @Index
    private String name;
    @Id
    private String id;
    private boolean sittingOut = false;
    private Room currentRoom = null;
    private String token = null;
    private boolean commandThisTurn = false;

    private Hand playerHand = new Hand();
    private List<AbstractPlayerListener> listeners = new ArrayList<>();
    private Game currentGame = null;
    private String avatar_url;

    @Override
    public String toString() {
        return "{\"id\":\"" + this.getId() + "\",\"name\":\"" + this.getName() + "\",\"balance\":" + this.getBalance() + ",\"globalBalance\":" + this.getGlobalBalance() + "}";
    }

    @Override
    public long jmx_getBalance() {
        return this.balance;
    }

    @Override
    public void jmx_setBalance(long bal) {
        this.balance = bal;
    }

    @Override
    public long jmx_getGlobalBalance() {
        return this.globalBalance;
    }

    @Override
    public void jmx_setGlobalBalance(long bal) {
        this.globalBalance = bal;
    }

    @Override
    public void jmx_setSittingOut(boolean bool) {
        this.sittingOut = bool;
    }

    @Override
    public void jmx_setRoundBet(long amount) {
        this.roundBet = amount;
    }

    @Override
    public void jmx_setGameBet(long amount) {
        this.gameBet = amount;
    }

    public void registerMbean() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("com.fcs.pokerserver" + ":type=Player,id=" + this.getId());
            mbs.registerMBean(this, name);
        } catch (MalformedObjectNameException | AlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException | InstanceAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor set Id for the Player is current time millis
     */
    public Player() {
        this.setId(String.valueOf(System.nanoTime()));
        registerMbean();
    }

    /**
     * other constructor set name and id for Player.
     *
     * @param String name
     */
    public Player(String name) {
        this.name = name;
        this.setId(name);
        registerMbean();
    }

    public void attachListener(AbstractPlayerListener listener) {
        listeners.add(listener);
    }

    public AbstractPlayerListener detachListener(AbstractPlayerListener listener) {
        if (listeners.contains(listener)) {
            return null;
        } else {
            listeners.remove(listener);
            return listener;
        }
    }

    /**
     * The method to the Player bet the chip in the current game.
     *
     * @param long amount
     * @throws AssertionError if the bet amount > the Player's balance or the Player is sitting out
     */

    public void bet(long amount) {
        assert amount < this.balance;


        assert this.sittingOut == false;
        this.setRoundBet(this.getRoundBet() + amount);
        this.gameBet += amount;
        this.balance = this.balance - amount;

        PlayerBetEvent pbe = new PlayerBetEvent(this);
        pbe.setAmount(amount);
        this.triggerEvent(pbe);
    }

    /**
     * The method to add more listener to this Player.
     *
     * @param PlayerListener pl
     */
    public void addPlayerListener(AbstractPlayerListener pl) {
        this.listeners.add(pl);
    }


    /**
     * The method to Player is next the round.
     */
    public void nextRound() {
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
     */
    public void fold() {
        PlayerFoldEvent pfe = new PlayerFoldEvent(this);
        this.triggerEvent(pfe);
        this.sittingOut = true;
    }

    /**
     * The Player want to check in the game.
     */
    public void check() {
        PlayerCheckEvent pce = new PlayerCheckEvent(this);
        this.triggerEvent(pce);
    }

    /**
     * The method fire PlayerEvent to all listener.
     */
    private void triggerEvent(AbstractPlayerEvent e) {
        for (Iterator<AbstractPlayerListener> iterator = this.listeners.iterator(); iterator.hasNext(); ) {
            AbstractPlayerListener listener = iterator.next();
            listener.actionPerformed(e);
        }
    }

//    private void fireEvent(PlayerEvent pe) {
//        for (Iterator<AbstractPlayerListener> iterator = this.listeners.iterator(); iterator.hasNext(); ) {
//            AbstractPlayerListener listener = iterator.next();
//            listener.actionPerformed(pe);
//        }
//    }

    /**
     * Return the Balance of the Player
     *
     * @return long balance
     */
    public long getBalance() {
        return balance;
    }


    public void myTurn() {
        // where to write code

    }

    /**
     * The method to set balance of the Player
     *
     * @param long balance
     */
    public void setBalance(long balance) {
        this.balance = balance;
    }

    /**
     * The method to get the global balance
     *
     * @param long globalBalance
     */
    public long getGlobalBalance() {
        return globalBalance;
    }

    /**
     * The method to set the global balance
     *
     * @param long globalBalance
     */
    public void setGlobalBalance(long globalBalance) {
        this.globalBalance = globalBalance;
    }

    /**
     * The method to get the Player's name in the game.
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * The method to set the name for the Player
     *
     * @param String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The method to get the Player's Id in the game.
     *
     * @return String id
     */
    public String getId() {
        return id;
    }

    /**
     * The method to set the Id for the Player
     *
     * @param String id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the Hand of the Player. The player has 2 cards in the hand.
     *
     * @return Hand playerHand
     */
    public Hand getPlayerHand() {
        return playerHand;
    }

    /**
     * The method to set the cards on hand of the Player
     *
     * @param Hand playerHand
     */
    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    /**
     * The method to get the Player's pot(bet value) in the game.
     *
     * @return long potGame
     */
    public long getGameBet() {
        return gameBet;
    }

    /**
     * The method to set the Player's pot(bet value) in the game.
     *
     * @param long gameBet
     */
    public void setGameBet(long gameBet) {
        this.gameBet = gameBet;
    }

    /**
     * The method to get the Player's bet in the round.
     *
     * @return long the roundBet.
     */
    public long getRoundBet() {
        return roundBet;
    }

    /**
     * The method to set the Player's round bet.
     *
     * @param long roundBet
     */
    public void setRoundBet(long roundBet) {
        this.roundBet = roundBet;
    }

    /**
     * The method to check the Player is sitting out in the game.
     *
     * @return boolean sittingOut
     */
    public boolean isSittingOut() {
        return sittingOut;
    }

    /**
     * The method to reset is sitting out for the Player
     *
     * @param boolean sittingOut
     */
    public void setSittingOut(boolean sittingOut) {
        this.sittingOut = sittingOut;
    }

    /**
     * The method to the Player get the current game.
     *
     * @return Game currentGame.
     */
    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * The method to set the current game.
     *
     * @param Game currentGame.
     */
    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Return the round of the current game.
     *
     * @return short round.
     */
    public short getRound() {
        return round;
    }

    /**
     * The method to reset the round of the current game.
     *
     * @param short round.
     */
    public void setRound(short round) {
        this.round = round;
    }


    /**
     * Return the Player's information by Json type.
     *
     * @return String jsonString
     */
    public String toJson() {
        return "{" + "\"Id\": \"" + this.getId() + "\",\"Hand\": " + this.getPlayerHand() + ",\"Name\": \"" + this.getName() + "\", \"Balance\": " + this.balance + ", \"RoundBet\": " + this.roundBet + "}";
    }

    /**
     * Return the current Room in the game.
     *
     * @return Room currentRoom
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * The method to set the room in the game.
     *
     * @param Room currentRoom
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Return the Token of Player in the game.
     *
     * @return String token.
     */
    public String getToken() {
        return token;
    }

    /**
     * The method to reset Token for the Player
     *
     * @param String token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Return boolean value to check the Player command this turn.
     *
     * @return boolean commandThisTurn
     */
    public boolean didCommandThisTurn() {
        return commandThisTurn;
    }

    /**
     * The method to set value the Player command this turn.
     *
     * @param boolean commandThisTurn
     */
    public void setCommandThisTurn(boolean commandThisTurn) {
        this.commandThisTurn = commandThisTurn;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
