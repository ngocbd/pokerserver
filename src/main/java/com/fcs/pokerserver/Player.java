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
import java.util.Timer;

import com.fcs.pokerserver.automation.CountDownPlayer;
import com.fcs.pokerserver.events.*;
import com.fcs.pokerserver.holder.Board;
import com.fcs.pokerserver.holder.Hand;
import com.fcs.pokerserver.holder.TwoPlusTwoHandEvaluator;
import com.fcs.pokerserver.utility.EncryptionEngine;
import com.google.api.gax.rpc.AlreadyExistsException;
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
    private String iv = "default";
    private boolean commandThisTurn = false;
    private int action_count = 0;

    private Hand playerHand = new Hand();
    private List<AbstractPlayerListener> listeners = new ArrayList<>();
    private Game currentGame = null;
    private String avatar_url;
    private Timer countdown = new Timer();
    private CountDownPlayer task = null;
    private long COUNTDOWN_DELAY = 35 * 1000;
    private boolean didAllIn = false;

    @Override
    public String toString() {
        return "{\"id\":\"" + this.getId() + "\",\"name\":\"" + this.getName() + "\",\"balance\":" + this.getBalance() + ",\"globalBalance\":" + this.getGlobalBalance() + ",\"isSittingOut\":" + this.isSittingOut() + ",\"isCommandThisTurn\":" + this.didCommandThisTurn() + ",\"gamebet\":" + this.getGameBet() +",\"hand\":"+this.playerHand +"}";
    }

    @Override
    public String jmx_info() {
        return "{\"id\":\"" + this.getId() + "\",\"name\":\"" + this.getName() + "\",\"balance\":" + this.getBalance() + ",\"globalBalance\":" + this.getGlobalBalance() + ",\"isSittingOut\":" + this.isSittingOut() + ",\"hand\":" + playerHand.toString() + ",\"isCommandThisTurn\":" + this.didCommandThisTurn() + ",\"roundBet\":" + this.getRoundBet() + "}";
    }

    @Override
    public String jmx_getToken() {
        return this.token;
    }

    @Override
    public long jmx_getBalance() {
        return this.balance;
    }

    @Override
    public long jmx_getRoundBet() {
        return this.roundBet;
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

    @Override
    public int jmx_evaluateHand() {
        Board b = this.getCurrentGame().getBoard();
        TwoPlusTwoHandEvaluator evaluator = TwoPlusTwoHandEvaluator.getInstance();
        return evaluator.evaluate(b, this.getPlayerHand()).getValue();
    }

    @Override
    public String jmx_getBoard() {
        Board b = this.getCurrentGame().getBoard();
        return "flop: " + b.getFlopCards() + " turn: " + b.getTurnCard() + " river: " + b.getRiverCard();
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

    public void detachListener() {
        listeners = new ArrayList<>();
    }

    /**
     * The method to the Player bet the chip in the current game.
     *
     * @param long amount
     * @throws AssertionError if the bet amount > the Player's balance or the Player is sitting out
     */
    public void allIn() {
        if (this.sittingOut) {
            System.out.println("Cannot All In - Player " + this.id + " is sitting out!");
            return;
        }
        if (this != this.currentGame.getCurrentPlayer()) {
            System.out.println("Cannot All In - Player " + this.id + " is not current player!");
            return;
        }
        this.setDidAllIn(true);
        long betAllin = this.getBalance();
        long newRoundBet = this.getRoundBet() + betAllin;
        this.setRoundBet(newRoundBet);
        this.gameBet += betAllin;
        this.balance = 0;
        if (task != null) {
            task.cancel();
            System.out.println("Player: " + this.id + " task is cancelled");
        }
        ;
        PlayerBetAllEvent pbe = new PlayerBetAllEvent(this);
        pbe.setAmount(betAllin);
        System.out.println("amount: " + pbe.getAmount());
        this.triggerEvent(pbe);
    }

    public boolean bet(long amount) {
        if (amount > this.balance) {
            System.out.println("Player " + this.id + " bet too much!");
            return false;
        }
        if (this.sittingOut) {
            System.out.println("Player " + this.id + " is sitting out!");
            return false;
        }
        if (this != this.currentGame.getCurrentPlayer()) {
            System.out.println("Player " + this.id + " is not current player!");
            return false;
        }
        if (amount == this.balance) {
            allIn();
            return true;
        }
        long newRoundBet = this.getRoundBet() + amount;
        if (newRoundBet < this.currentGame.getCurrentRoundBet()) {
            System.out.println("Cannot Bet less than current Round Bet");
            return false;
        }
        this.setRoundBet(newRoundBet);
        this.gameBet += amount;
        this.balance = this.balance - amount;
        if (task != null) {
            task.cancel();
            System.out.println("Player: " + this.id + " task is cancelled");
        }
        ;
        PlayerBetEvent pbe = new PlayerBetEvent(this);
        pbe.setAmount(amount);
        this.triggerEvent(pbe);
        return true;
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
    public boolean fold() {
        if (this != this.currentGame.getCurrentPlayer()) {
            System.out.println("Player " + this.getId() + " forbid to fold in the other's turn");
            return false;
        }
        if (task != null) {
            task.cancel();
        }
        PlayerFoldEvent pfe = new PlayerFoldEvent(this);
        this.triggerEvent(pfe);
        return true;
    }

    /**
     * The Player want to check in the game.
     */
    public boolean check() {
        if (this != this.currentGame.getCurrentPlayer()) {
            System.out.println("Player " + this.getId() + " forbid to fold in the other's turn");
            return false;
        }
        if (this.roundBet != currentGame.getCurrentRoundBet() && !this.isDidAllIn()) {
            System.out.println("Player " + this.getId() + " forbid to check due to unequal betting");
            return false;
        }
        if (task != null) {
            task.cancel();
        }
        PlayerCheckEvent pce = new PlayerCheckEvent(this);
        this.triggerEvent(pce);
        return true;
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


    /**
     * Return the Balance of the Player
     *
     * @return long balance
     */
    public long getBalance() {
        return balance;
    }


    public void myTurn() {
        task = CountDownPlayer.createInstance(this, this.getCurrentGame());
        countdown.schedule(task, COUNTDOWN_DELAY);
//        System.out.println("My Turn: " + this.getId() + " ID task: " + task.getId());
        GetTurnPlayerEvent e = new GetTurnPlayerEvent(this);
        this.triggerEvent(e);
    }

    public void setTask(CountDownPlayer task) {
        this.task = task;
    }

    public CountDownPlayer getTask() {
        return task;
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
     * Encrypted the Hand of the Player. The player has 2 cards in the hand.
     * Encryption Algorithm is AES/CBC/PKCS5Padding
     * IV does not need to be random (fixed) due to only 1 message - 1 secretKey relationship.
     *
     * @return String encrypted hand of player with secret key is his token
     */
    public String getEncryptedHand() {
        if (this.playerHand == null) {
            System.out.println("Hand is null");
            return null;
        }
        EncryptionEngine encryption = EncryptionEngine.getInstance();
        return encryption.encrypt(this.playerHand.toString(), token, iv, "AES/CBC/PKCS5Padding");
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

    public boolean buyChip(long amount) {
        if (this.globalBalance < amount) {
            System.out.println("Player " + this.id + " cannot buy chip " + amount + "- Not enough global balance");
            return false;
        }
        if (this.globalBalance >= amount) {
            this.balance += amount;
            this.globalBalance -= amount;
        }
        return true;
    }

    public void sellChip() {
        if (balance == 0) return;
        this.globalBalance += balance;
        balance = 0;
    }

    public long getCOUNTDOWN_DELAY() {
        return COUNTDOWN_DELAY;
    }

    public void setCOUNTDOWN_DELAY(long COUNTDOWN_DELAY) {
        this.COUNTDOWN_DELAY = COUNTDOWN_DELAY;
    }

    public boolean isDidAllIn() {
        return didAllIn;
    }

    public void setDidAllIn(boolean didAllIn) {
        this.didAllIn = didAllIn;
    }
}
