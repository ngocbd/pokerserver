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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Standard deck of cards for poker. 52 Cards. 13 Clubs, Diamonds, Spades, and Hearts.
 * @category pokerserver
 * @author Ngocbd
 */
public class Deck {
	private List<Card> cards;
	

	/**
	 * Simple call constructor with no shuffle.
	 * */
	public Deck(){
		this(true);
	}
	

	/**
	 * Call constructor with 2 options: shuffle(true) or not shuffle(flase)
	 * */
	public Deck(boolean shuffle){
		initDeck();
		if(shuffle){
			shuffleDeck();
		}
	}
	

	/**
	 * Create the Deck with cards.
	 * */
	public Deck(List<Card> cards){
		this.cards = cards;
	}
	
	/**
	 * Create and Init for Deck.
	 * Add 52 cards in the deck.
	 * */
	public void initDeck(){
		cards = new LinkedList<Card>();
		cards.addAll(Arrays.asList(Card.values()));
	}
	
	/**
	 * Shuffle Deck used to randomize a deck of playing cards to provide an element of chance in card games.
	 * */
	public void shuffleDeck(){
		Collections.shuffle(cards);
	}
	

	/**
	 * Deal the cards in the Deck 
	 * @return the card 
	 *  
	 * */
	public Card dealCard(){
		return cards.remove(0);
	}
	
	/**
	 * Return all of cards of Deck
	 * @return cards
	 * */
	public List<Card> exportDeck(){
		return cards;
	}
}