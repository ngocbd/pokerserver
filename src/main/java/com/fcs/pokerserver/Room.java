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

import com.fcs.pokerserver.events.AbstractGameEvent;
import com.fcs.pokerserver.events.AbstractRoomEvent;
import com.fcs.pokerserver.events.GameActRoomEvent;
import com.fcs.pokerserver.events.GameAction;
import com.fcs.pokerserver.events.GameListener;
import com.fcs.pokerserver.events.RoomAction;
import com.fcs.pokerserver.events.RoomListener;
import com.fcs.pokerserver.events.RoundGameEvent;
import com.fcs.pokerserver.events.VisitRoomEvent;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * An instance of the Room class is created Room when user want to play Poker Game.
 *
 * @category com > fcs > pokerserver
 */

public class Room implements GameListener, RoomMBean {
    private Game currentGame = null;
    private long RoomID;
    private Player master;
    private BlindLevel blindLevel;

    private List<Player> listPlayer = new ArrayList<Player>();

    private List<RoomListener> listeners = new ArrayList<RoomListener>();

    public Player getMaster() {
        return master;
    }

    /**
     * The method to add one more listener to this Room.
     *
     * @param RoomListener rl
     */
    public void addRoomListener(RoomListener rl) {
        this.listeners.add(rl);
    }

    /**
     * The method fire a RoomEvent to all listener.
     */
    private void fireEvent(AbstractRoomEvent re) {
        for (Iterator<RoomListener> iterator = this.listeners.iterator(); iterator.hasNext(); ) {
            RoomListener listener = (RoomListener) iterator.next();
            listener.actionPerformed(re);
        }
    }

    /**
     * The method to add the Player to the room.
     * The player is also added to game if  current GameStatus is NOT_STARTED and game's player size less than 8 players
     *
     * @param Player p
     */
    public void addPlayer(Player p) {
        if (listPlayer.contains(p)) return;
        this.listPlayer.add(p);
        //Buy 1000 chip for default in any room.
        p.sellChip();
        p.buyChip(1000);
        p.setCurrentRoom(this);

        if (this.currentGame != null
                && this.currentGame.getStatus() == GameStatus.NOT_STARTED
                && this.currentGame.getListPlayer().size() < 8
                && p.getBalance() > this.getBlindLevel().getBigBlind()) {

            this.currentGame.addPlayer(p);
        } else {
            p.setSittingOut(true);
        }

        VisitRoomEvent re = new VisitRoomEvent(this);
        re.setType(RoomAction.PLAYERJOINEDROOM);
        re.setP(p);

        this.fireEvent(re);
    }

    public void removePlayer(Player p) {
        if (!listPlayer.contains(p)) return;
        /**Check whether this player is playing in a game
         * If the player is currently playing, fold him and remove from game list, or else just remove him from game list.
         * */
        if (this.currentGame != null && this.currentGame.getListPlayer().contains(p)) {
            if (this.currentGame.getStatus() != GameStatus.END_HAND && this.currentGame.getStatus() != GameStatus.NOT_STARTED && this.currentGame.getCurrentPlayer() == p) {
                p.fold();
            }
            this.currentGame.getListPlayer().remove(p);
        }

        //Sell all Chip when player left
        p.sellChip();
        p.setSittingOut(true);
        this.listPlayer.remove(p);
        p.setCurrentRoom(null);
        VisitRoomEvent re = new VisitRoomEvent(this);
        re.setType(RoomAction.PLAYERLEFT);
        re.setP(p);
        this.fireEvent(re);
    }


    /**
     * Return the list of the Players
     *
     * @return List<Player> listPlayer
     */
    public List<Player> getListPlayer() {
        return listPlayer;
    }

    /**
     * The method to reset the list of the Players
     *
     * @param List<Player> listPlayer
     */
//    private void setListPlayer(List<Player> listPlayer) {
//        this.listPlayer = listPlayer;
//    }

    /**
     * Return the current game in the room.
     *
     * @return Game currentGame
     */
    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * The method to set the current game in the room
     *
     * @param Game currentGame
     */
    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Return the id of the current room.
     *
     * @return long roomId
     */
    public long getRoomID() {
        return RoomID;
    }

    /**
     * The method to set id for the room
     *
     * @param long roomId
     */
//    public void setRoomID(long roomID) {
//        RoomID = roomID;
//    }

    /**
     * Return the Player is the Master
     *
     * @return Player master
     */
//    public Player getMaster() {
//        return master;
//    }

    /**
     * The method to set the Player is the master
     *
     * @param Player master
     */
    public void setMaster(Player master) {
        this.master = master;
    }

    /**
     * Return the Blind Level of the Player
     *
     * @return BlindLevel blindLevel
     */
    public BlindLevel getBlindLevel() {
        return blindLevel;
    }

    /**
     * The method to set the Blind Level for the Player
     *
     * @param BlindLevel blindLevel
     */
//    public void setBlindLevel(BlindLevel blindLevel) {
//        this.blindLevel = blindLevel;
//    }


    /**
     * The constructor with 2 params are Player and BlindLevel
     *
     * @param Player master, BlindLevel blindLevel
     */
    /**
     * Add master into new Room, then create new game and add master into new game too.
     **/
    public Room(Player master, BlindLevel blindLevel) {
        this.master = master;
        this.blindLevel = blindLevel;

        this.RoomID = System.nanoTime();
//        this.createNewGame();
        this.addPlayer(master);
        registerMBean();
    }

    /**
     * The method to create the new Game in the Room
     *
     * @return Game currentGame
     */
    public Game createNewGame() {
        if (this.currentGame != null && this.currentGame.getStatus() != GameStatus.END_HAND) {
            return this.currentGame;
        }

        this.currentGame = new Game(this);
        this.currentGame.addGameListener(this);
        this.currentGame.addPlayer(this.master);

        //TODO not good because game event should fire from game
        GameActRoomEvent re = new GameActRoomEvent(this);
        re.setE(new RoundGameEvent(this.currentGame, GameAction.CREATED));
        this.fireEvent(re);
        return this.currentGame;
    }

    /**
     * The method to next the Game in the Room
     *
     * @return Game currentGame
     */
    public Game nextGame() {
        if (this.currentGame.getStatus() != GameStatus.END_HAND) {
            System.out.println("Game is already started");
            return null;
        }
        System.out.println("Next Game Called");
//        assert this.currentGame.getStatus() == GameStatus.END_HAND;
        Game previous_Game = this.currentGame;
        /**
         * Cancel all Remaining Timer Task of previous game*/
        for (Player p : previous_Game.getListPlayer()) {
            try {
                p.getTask().cancel();
            } catch (NullPointerException e) {
                System.out.println("Task null ");
            }
        }

        Player previous_dealer = previous_Game.getDealer();
        int previous_dealer_index = previous_Game.getDealer_index();
        if (this.currentGame != null && this.currentGame.getStatus() != GameStatus.END_HAND) {
            return this.currentGame;
        }
        List<Player> newListPlayer = new ArrayList<>();
        this.currentGame = new Game(this);
        this.currentGame.addGameListener(this);
        /**
         * Prioritize players who are currently playing, will be added first into new game players list.
         * */
        for (Player p : previous_Game.getListPlayer()) {
            if (!p.isSittingOut() && (p.getBalance() > this.getBlindLevel().getBigBlind())) {
                newListPlayer.add(p);
            }
        }
        /**
         * Add the remaining Player in room.
         * */
        for (Player p : listPlayer) {
            if (newListPlayer.size() >= 8) break;
            if (!newListPlayer.contains(p) && (p.getBalance() > this.getBlindLevel().getBigBlind())) {
                p.setSittingOut(false);
                newListPlayer.add(p);
            }
        }
        previous_Game.dumpListPlayer();
        for (Player p : newListPlayer) {
            p.setCommandThisTurn(false);
            p.setSittingOut(false);
            p.setRoundBet(0);
            this.currentGame.addPlayer(p);
        }
        System.out.println("New Game status: " + this.currentGame.getStatus());
        System.out.println("List PLayer new Game: " + currentGame.getListPlayer().toString());
//        this.currentGame.setListPlayer(newListPlayer);
        /**
         * Set new dealer for new Game
         * In case dealer has not quit yet.
         * */

        if (currentGame.getListPlayer().contains(previous_dealer)) {
            Player newDealer = currentGame.getNextPlayer(previous_dealer);
            System.out.println("New Dealer: " + newDealer);
            currentGame.setDealer(newDealer);

        }
        /**
         * In case previous dealer did quit. New Dealer will lie on next index of prev_dealer.
         * In case next game got less players than prev-game. Dealer will be set by the index 0.
         * **/
        else if (previous_dealer_index < 8) {
            try {
                currentGame.setDealer(currentGame.getListPlayer().get(previous_dealer_index + 1));
            } catch (IndexOutOfBoundsException e) {
                currentGame.setDealer(currentGame.getListPlayer().get(0));
            }
        } else if (previous_dealer_index == 8) currentGame.setDealer(currentGame.getListPlayer().get(0));


        /**
         * In case prev-dealer did stay on the last position. The new dealer will be set by index 0
         * */


        //TODO not good because game event should fire from game
        GameActRoomEvent re = new GameActRoomEvent(this);
        re.setE(new RoundGameEvent(this.currentGame, GameAction.CREATED));
        this.fireEvent(re);
        return this.currentGame;

    }

    public List<Player> getListPlayers() {
        return listPlayer;
    }

    /**
     * Returns the id of the room.
     *
     * @return String roomId
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub need to fix
        return "{\"id\":" + this.RoomID + ",\"playerCount\":"
                + this.listPlayer.size() + ",\"blindLevel\":\""
                + blindLevel + "\",\"master\":\""
                + master.getId() + "\"}";
    }

    /**
     * Send all event to the room topic
     **/
    @Override
    public void actionPerformed(AbstractGameEvent event) {
        GameActRoomEvent re = new GameActRoomEvent(this);
        re.setE(event);
        this.fireEvent(re);
    }

    public void registerMBean() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("com.fcs.pokerserver:type=Room,id=" + this.RoomID);
            mbs.registerMBean(this, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String jmx_getRoomPlayerID() {
        return this.listPlayer.toString();
    }

    @Override
    public void jmx_kickPlayer(String id) {
        Player pl = this.listPlayer.stream().filter(p -> id.equals(p.getId())).findFirst().orElse(null);
        if (pl != null) this.listPlayer.remove(pl);
    }

    @Override
    public String jmx_getMasterID() {
        return this.master.toJson();
    }

    @Override
    public void jmx_setMaster(String id) {
        Player pl = this.listPlayer.stream().filter(p -> id.equals(p.getId())).findFirst().orElse(null);
        if (pl != null) this.master = pl;
    }
}
