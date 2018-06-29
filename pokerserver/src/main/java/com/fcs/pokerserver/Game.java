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
import java.util.List;

public class Game {
	
	private List<Player> listPlayer = new ArrayList<Player>();
	private Deck deck =null;
	private long id ;
	private long potBalance;
	private Room room;
	public Game(Room room)
	{
		this.room=room;
		this.setId(System.currentTimeMillis());
		this.deck=new Deck();
		this.deck.initDeck();
		
	}
	public void addPlayer(Player p)
	{
		this.listPlayer.add(p);
		
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
	
}
