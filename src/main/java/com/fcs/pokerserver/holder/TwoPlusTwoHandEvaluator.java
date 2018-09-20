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
import java.util.Iterator;

import com.fcs.pokerserver.Card;



/**
 * Poker hand Evaluation algorithm based on the two plus two 7 card hand evaluation algorithm.
 * @category com > fcs > pokerserver > holder
 */
public class TwoPlusTwoHandEvaluator  {


	private static final String HAND_RANKS = "/HandRanks.zip";
	
	private int[] handRanks;
	
	private static TwoPlusTwoHandEvaluator instance;
	
	/**
	 * Create new the TwoPlusTwoHandEvaluator
	 * */
	public TwoPlusTwoHandEvaluator() throws Exception{
		ConfigurationLoader reader = new ConfigurationLoader();
		
			handRanks = reader.loadHandRankResource(HAND_RANKS);
		
	}
	
	/**
	 * The two plus two lookup table is very memory intensive.  You should only ever create
	 * one instance of the class.  Use this method to keep the singleton pattern.
	 * @return {@link TwoPlusTwoHandEvaluator} instance
	 * @throws Exception 
	 */
	public static TwoPlusTwoHandEvaluator getInstance() {
		if(instance == null){
			try {
				instance = new TwoPlusTwoHandEvaluator();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
		return instance;
	}
	
	/**
	 * The method to evaluate the card on Player's hand
	 * @return HandRand
	 * @param Board board, Hand hand
	 * */
	public HandRank evaluate(Board board, Hand hand) {
		Iterator<Card> cardIterator = new CardIterator(board, hand);
		int p = 53;
		while(cardIterator.hasNext()){
			Card card = cardIterator.next();
			p = handRanks[p + card.getEvaluation()];
		}
		return new HandRank(p);
	}

	/**
	 * The method to compare the 2 hands with the board. Return the hand has bigger value.
	 * @param Hand h1, Hand h2, Board b
	 * @return int value(+ or -) 
	 * */
	public static int compare(Hand h1, Hand h2, Board b){
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank1 = evaluator.evaluate(b, h1);
		HandRank rank2 = evaluator.evaluate(b, h2);
		return rank1.compareTo(rank2);
	}
}