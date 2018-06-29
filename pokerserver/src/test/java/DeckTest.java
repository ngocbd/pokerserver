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


import static org.junit.Assert.*;

import org.junit.Test;

import com.fcs.pokerserver.Deck;

/**
 * @author DaiKa
 *
 */
public class DeckTest {

	 @Test public void initDeck() {
	       Deck deck = new Deck();
	       deck.initDeck();
	       deck.shuffleDeck();
	       assertEquals(deck.exportDeck().size(),52);
	    }
	 @Test public void shuffleDeck() {
	       Deck deck = new Deck();
	       deck.initDeck();
	       
	       
	       
	       Deck shuffledDeck = new Deck();
	       shuffledDeck.initDeck();
	       shuffledDeck.shuffleDeck();
	       assertNotEquals(shuffledDeck.exportDeck(), deck.exportDeck());
	    }

}
