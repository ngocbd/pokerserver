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

import com.fcs.pokerserver.events.GameAction;
import com.fcs.pokerserver.events.GameEvent;
import com.fcs.pokerserver.events.GameListener;
import com.fcs.pokerserver.events.RoomAction;
import com.fcs.pokerserver.events.RoomEvent;
import com.fcs.pokerserver.events.RoomListener;

public class Room implements GameListener {
	Game currentGame = null;
	long RoomID;
	Player master = null;
	
	private List<Player> listPlayer = new ArrayList<Player>();

	private List<RoomListener> listeners = new ArrayList<RoomListener>();

	public void addRoomListener(RoomListener rl) {
		this.listeners.add(rl);
	}

	private void fireEvent(RoomEvent re) {
		for (Iterator iterator = this.listeners.iterator(); iterator.hasNext();) {
			RoomListener listener = (RoomListener) iterator.next();
			listener.actionPerformed(re);

		}
	}

	public void addPlayer(Player p) {
		this.listPlayer.add(p);
		p.setCurrentRoom(this);

		if (this.currentGame.getStatus() == GameStatus.NOT_STARTED && this.currentGame.getListPlayer().size() < 8) {

			this.currentGame.addPlayer(p);
		}
		RoomEvent re = new RoomEvent(this, RoomAction.PLAYERJOINEDROOM);
		re.agruments.put("player", p);
		
		this.fireEvent(re);


	}

	public List<Player> getListPlayer() {
		return listPlayer;
	}

	private void setListPlayer(List<Player> listPlayer) {
		this.listPlayer = listPlayer;
	}

	public Game getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}

	public long getRoomID() {
		return RoomID;
	}

	public void setRoomID(long roomID) {
		RoomID = roomID;
	}

	public Player getMaster() {
		return master;
	}

	public void setMaster(Player master) {
		this.master = master;
	}

	public BlindLevel getBlindLevel() {
		return blindLevel;
	}

	public void setBlindLevel(BlindLevel blindLevel) {
		this.blindLevel = blindLevel;
	}

	BlindLevel blindLevel;

	public Room(Player master, BlindLevel blindLevel) {
		this.master = master;
		this.blindLevel = blindLevel;
		;
		this.RoomID = System.currentTimeMillis();
		
		this.createNewGame();
		

		

	}

	public Game createNewGame() {
		if(this.currentGame!=null)
		{
			if(this.currentGame.getStatus()!=GameStatus.END_HAND)
			{
				return this.currentGame;
			}
		}
		this.currentGame = new Game(this);
		this.currentGame.addGameListener(this);
		this.currentGame.addPlayer(this.master);


		//TODO not good because game event should fire from game
		RoomEvent re = new RoomEvent(this, RoomAction.GAMEACTION);

		re.agruments.put("gameevent", new GameEvent(this.currentGame, GameAction.CREATED));

		this.fireEvent(re);


		return this.currentGame;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub need to fix
		return String.valueOf(this.RoomID);
	}

	/*
	 * Send all event to room topic
	 */
	@Override
	public void actionPerformed(GameEvent event) {

		RoomEvent re = new RoomEvent(this, RoomAction.GAMEACTION);

		re.agruments.put("gameevent", event);

		this.fireEvent(re);


	}

}
