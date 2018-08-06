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
package com.fcs.pokerserver.holder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.fcs.pokerserver.Card;

/**
 * A container designed for storing cards.
 */
public class CardHolder implements Serializable {

	Logger log = Logger.getLogger(CardHolder.class.getName());
	private static final long serialVersionUID = -6289334212535961129L;

	List<Card> cards = new ArrayList<Card>();

	protected CardHolder(Card... cards) {
		super();
		this.cards.addAll(Arrays.asList(cards));
	}
	public CardHolder()
	{
		
		
	}
	public void addCard(Card... cards)
	{
		this.cards.addAll(Arrays.asList(cards));
		
	}
	/**
	 * Returns a card at particular index.
	 * 
	 * @param index index of the array of cards in this CardHolder
	 * @return a card at position index
	 */
	public Card getCard(int index) {
		return cards.get(index);
	}
	
	/*
	 * Get all cards on board
	 * */
	
	public List<Card> getAllCards()
	{
		return this.cards;
	}

	/**
	 * Returns the number of the cards stored in the container.
	 * 
	 * @return the number of cards
	 */
	public int getCardNumber() {
		return cards.size();
	}

	/**
	 * Creates a card iterator for the cards stored in the container.
	 * 
	 * @return unmodifiable card iterator
	 */
	public Iterator<Card> iterator() {
		return cards.iterator();
	}

	

}
