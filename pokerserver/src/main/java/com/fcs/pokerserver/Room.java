package com.fcs.pokerserver;

public class Room {
	Game currentGame =null;
	long RoomID;
	Player master = null;
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
	public Room(Player master,BlindLevel blindLevel)
	{
		this.master=master;
		this.blindLevel=blindLevel; ; 
		
		
	}
	public Game createNewGame()
	{
		this.currentGame = new Game(this);
		this.currentGame.addPlayer(this.master);
		return this.currentGame;
	}
	
}
