/*
The MIT License (MIT)
Copyright (c) 2018 by Ngocbd
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

package com.fcs.pokerserver.holder;
import java.util.List;

import com.fcs.pokerserver.Card;

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


/**
 * A {@link CardHolder} to store five community cards.
 * @category com > fcs > pokerserver > holder
 */
public class Board extends CardHolder {

	private static final long serialVersionUID = -4373461696894251713L;

	/**
	 * Creates new board with five community cards.
	 * 
	 * @param flop1 first card of the flop
	 * @param flop2 second card of the flop
	 * @param flop3 third card of the flop
	 * @param turn turn card
	 * @param river river card
	 */
	public Board(Card flop1, Card flop2, Card flop3, Card turn, Card river) {
		super(flop1, flop2, flop3, turn, river);
	}
	
	/**
	 * The constructor default.
	 * */
	public Board() {
		
	}
	
	/**
	 * Return the Cards in the Flop
	 * @return List<Card> cards
	 * */
	public List<Card> getFlopCards()
	{
		return this.cards.subList(0, 3);
	}
	
	/**
	 * Return the Cards in the Turn
	 * @return Card card
	 * */
	public Card getTurnCard()
	{
		
		return this.cards.get(3);
	}
	
	/**
	 * Return the Cards in the River
	 * @return Card card
	 * */
	public Card getRiverCard()
	{
		return this.cards.get(4);
	}

}
