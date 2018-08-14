/*
The MIT License (MIT)
Copyright (c) 2013 Jacob Kanipe-Illig
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



/**
 * Representation of the poker hand strength.
 * @category com > fcs > pokerserver > holder
 */
public class HandRank implements Comparable<HandRank>, Serializable {

	private static final long serialVersionUID = 6897360347770643227L;

	private final int rankValue;

	/**
	 * Create new the HandRank with new value
	 * @param int rankValue
	 * */
	public HandRank(int rankValue) {
		super();
		this.rankValue = rankValue;
	}

	/**
	 * Compares the strength of the hand (the values of the ranks).
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
//	@Override
//	public final int otherCompareTo(HandRank rank) {
//		return rankValue < rank.rankValue ? -1
//				: (rankValue == rank.rankValue ? 0 : 1);
//	}
	@Override
	public final int compareTo(HandRank rank) {
		return rankValue - rank.rankValue ;
				
	}

	/**
	 * Return the hasCode.
	 * @return int rankValue.
	 * */
	@Override
	public final int hashCode() {
		return rankValue;
	}

	/**
	 * Return value true or false when equal compare
	 * @return boolean equals
	 * */
	@Override
	public final boolean equals(Object obj) {
		return (obj instanceof HandRank)
				&& (rankValue == ((HandRank) obj).rankValue);
	}

	/**
	 * Return the rankValue
	 * @return int rankValue
	 * */
	public final int getValue() {
		return rankValue;
	}
	
	/**
	 * The type of hand as represented by {@link HandType}
	 * @return {@link HandType}
	 */
	public HandType getHandType(){
		/*The rank value is categorized based on the Two Plus Two (2+2) hand evaluation algorithm.
		0 == bad hand, 1==one pair, so on.  This value is stored in the thirteenth bit of the rankValue score.
		Bit-shift to get the type value bit, match it to the enum with the ordinal ordering for the 2+2 evaluation type*/
		return HandType.values()[rankValue >> 12];
	}
	
	/**
	 * Override the toString method
	 * @return String
	 * */
	@Override
	public String toString()
	{
		return getHandType().toString();
	}

}