package com.fsc.pokerserver.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.events.PlayerAction;
import com.fcs.pokerserver.events.PlayerEvent;
import com.fcs.pokerserver.events.PlayerListenner;

public class PlayerTest implements PlayerListenner  {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		Player p = new Player();
		p.setName("Kuki");
		p.setBalance(50000);
		
		p.setListenner(this);
		p.bet(100);
		assertEquals(p.getBalance(), 50000-100);
		
	}

	@Override
	public void actionPerformed(PlayerEvent event) {
		if(event.getAction()==PlayerAction.BET)
		{
			
			System.out.println(event.getSource().getName()+" bet "+event.getSource().getLastBet());
		}
		
	}

}
