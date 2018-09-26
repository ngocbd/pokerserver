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

/**
 * Enumeration of the card suits.
 * @category com > fcs > pokerserver
 */
public enum Suit {

//	CLUBS("\u2663"),
//	DIAMONDS("\u2666"),
//	HEARTS("\u2665"),
//	SPADES("\u2660");
	CLUBS("clubs"),
	DIAMONDS("diamonds"),
	HEARTS("hearts"),
	SPADES("spades");

	/**
	 * Short string representation of the suit .
	 */
	private final String name;

	private Suit(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the suit.
	 * @return String name
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return "_of_"+name;
	}

}