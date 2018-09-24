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

import com.fcs.pokerserver.events.*;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An instance of the Room class is created Room when user want to play Poker Game.
 *
 * @category com > fcs > pokerserver
 */

public class Room implements GameListener {
    private Game currentGame = null;
    private long RoomID;
    private Player master;
    private BlindLevel blindLevel;

    private List<Player> listPlayer = new ArrayList<Player>();

    private List<RoomListener> listeners = new ArrayList<RoomListener>();

    /**
     * The method to add more listener to this Room.
     *
     * @param RoomListener rl
     */
    public void addRoomListener(RoomListener rl) {
        this.listeners.add(rl);
    }

    /**
     * The method fire RoomEvent to all listener.
     */
    private void fireEvent(AbstractRoomEvent re) {
        for (Iterator iterator = this.listeners.iterator(); iterator.hasNext(); ) {
            RoomListener listener = (RoomListener) iterator.next();
            listener.actionPerformed(re);
        }
    }

    /**
     * The method to add the Player to the room.
     *
     * @param Player p
     */
    public void addPlayer(Player p) {
        if (listPlayer.contains(p)) return;
        this.listPlayer.add(p);
        p.setCurrentRoom(this);

        if (this.currentGame.getStatus() == GameStatus.NOT_STARTED && this.currentGame.getListPlayer().size() < 8) {

            this.currentGame.addPlayer(p);
        } else {
            p.setSittingOut(true);
        }

        VisitRoomEvent re = new VisitRoomEvent(this);
        re.setType(RoomAction.PLAYERJOINEDROOM);
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

        this.RoomID = System.currentTimeMillis();
        this.createNewGame();
        this.addPlayer(master);
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
        Game previous_Game = this.currentGame;
        Player previous_dealer = previous_Game.getDealer();
        int previous_dealer_index = previous_Game.getListPlayer().indexOf(previous_dealer);
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
            if (!p.isSittingOut()) {
                newListPlayer.add(p);
            }
        }
        /**
         * Add the remaining Player in room.
         * */
        for (Player p : listPlayer) {
            if (newListPlayer.size() >= 8) break;
            if (!newListPlayer.contains(p)) {
                p.setSittingOut(false);
                newListPlayer.add(p);
            }
        }
        this.currentGame.setListPlayer(newListPlayer);
        /**
         * Set new dealer for new Game
         * */
        // In case dealer hasnt quit yet.
        if (currentGame.getListPlayer().contains(previous_dealer)) currentGame.setDealer(currentGame.getNextPlayer(previous_dealer));
        //In case previous dealer did quit.
        if (previous_dealer_index<8) currentGame.setDealer(currentGame.getListPlayer().get(previous_dealer_index+1));
        if (previous_dealer_index==8) currentGame.setDealer(currentGame.getListPlayer().get(0));

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
        return String.valueOf(this.RoomID);
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

}
