/*
The MIT License (MIT)
Copyright (c) 2018 by habogay
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

package com.fsc.pokerserver.test;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.events.PlayerAction;
import com.fcs.pokerserver.events.PlayerEvent;
import com.fcs.pokerserver.events.PlayerListener;

/**
 * The class to test the Server and the Client in the game.
 * @category com > fcs > pokerserver > test
 * */
@Ignore
public class PlayerTest implements PlayerListener  {

//	@Before
//	public void setUp() throws Exception {
//	}

	/**
	 * The method to test to create new Player.
	 * */
	@Test
	public void test() {
		
		Player p = new Player();
		p.setName("Kuki");
		p.setBalance(50000);
		
		p.addPlayerListener(this);
		p.bet(100);
		assertEquals(p.getBalance(), 50000-100);

	}

	
	@Override
	public void actionPerformed(PlayerEvent event) {
		if(event.getAction()==PlayerAction.BET)
		{
			System.out.println(event.getSource().getName()+" bet "+event.getSource().getRoundBet());
		}

	}
	/**
	 * Test Travis
	 * */

}
