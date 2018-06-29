package com.fcs.pokerserver.holder;
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
 * A {@link CardHolder} for storing five community cards.
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

}
