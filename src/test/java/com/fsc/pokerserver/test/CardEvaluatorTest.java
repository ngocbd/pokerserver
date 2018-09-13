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

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.fcs.pokerserver.Card;
import com.fcs.pokerserver.holder.Board;
import com.fcs.pokerserver.holder.Hand;
import com.fcs.pokerserver.holder.HandRank;
import com.fcs.pokerserver.holder.HandType;
import com.fcs.pokerserver.holder.TwoPlusTwoHandEvaluator;

/**
 * JUnit tests for the Seven Card Poker Hand Evaluation.
 * The Evaluation algorithm ({@link TwoPlusTwoHandEvaluator}) needs to always determine
 * the winner between two (or more) hands.  It should also correctly determine
 * the type of the hand (pair, flush, etc.)
 * @category com > fcs > pokerserver > test
 */
@Ignore
public class CardEvaluatorTest extends TestCase {
	public static void main(String[] args) {
		Hand h1 = new Hand(Card.THREE_OF_CLUBS, Card.SEVEN_OF_HEARTS);
		Hand h2 = new Hand(Card.TWO_OF_SPADES, Card.FOUR_OF_CLUBS);
		Hand h3 = new Hand(Card.NINE_OF_SPADES, Card.TWO_OF_DIAMONDS);
		Hand h4 = new Hand(Card.TEN_OF_DIAMONDS, Card.FOUR_OF_SPADES);
		Hand h5 = new Hand(Card.TEN_OF_CLUBS, Card.FOUR_OF_DIAMONDS);
		
		Board b = new Board(Card.ACE_OF_SPADES, Card.TWO_OF_CLUBS, Card.EIGHT_OF_HEARTS, 
				Card.THREE_OF_HEARTS, Card.TEN_OF_HEARTS);
//		int comp = compare(h1,h2,b);
		List<Hand> list = new ArrayList<Hand>();
		list.add(h1);
		list.add(h2);
		list.add(h3);
		list.add(h4);
		list.add(h5);
		
		list.sort(new Comparator<Hand>() {
			public int compare(Hand o1, Hand o2) {
				return CardEvaluatorTest.compare(o1, o2, b);
				
			}
		});
		
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		
		HandRank rank1 = evaluator.evaluate(b, h1);
		System.out.println(rank1.getHandType() +" : value ="+rank1.getValue());
		
		HandRank rank2 = evaluator.evaluate(b, h2);
		System.out.println(rank2.getHandType()+" : value ="+rank2.getValue());
		
		HandRank rank3 = evaluator.evaluate(b, h3);
		System.out.println(rank3.getHandType()+" : value ="+rank3.getValue());
		
		HandRank rank4 = evaluator.evaluate(b, h4);
		System.out.println(rank4.getHandType()+" : value ="+rank4.getValue());
		
		HandRank rank5 = evaluator.evaluate(b, h5);
		System.out.println(rank5.getHandType()+" : value ="+rank5.getValue());
		
		
		
		System.out.println("Best hand "+list.get(list.size()-1));
	}

	/**
	 * The method to test pair over pair
	 * */
	@Test
	public void testPair1(){
		Hand h1 = new Hand(Card.ACE_OF_CLUBS, Card.ACE_OF_DIAMONDS);
		Hand h2 = new Hand(Card.KING_OF_CLUBS, Card.KING_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_DIAMONDS, Card.EIGHT_OF_SPADES, Card.FOUR_OF_CLUBS, 
				Card.TEN_OF_HEARTS, Card.SIX_OF_SPADES);
		int comp = compare(h1,h2,b);
		assertTrue(comp > 0);
	}
	
	/**
	 * The method to test pair over pair in reverse
	 * */
	@Test
	public void testPair2(){
		Hand h2 = new Hand(Card.ACE_OF_CLUBS, Card.ACE_OF_DIAMONDS);
		Hand h1 = new Hand(Card.KING_OF_CLUBS, Card.KING_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_DIAMONDS, Card.EIGHT_OF_SPADES, Card.FOUR_OF_CLUBS, 
				Card.TEN_OF_HEARTS, Card.SIX_OF_SPADES);
		int comp = compare(h1,h2,b);
		assertTrue(comp < 0);
	}
	/**
	 * The method to Test pair over not pair
	 * */
	@Test
	public void testPairvsNotPair(){
		Hand h1 = new Hand(Card.NINE_OF_DIAMONDS, Card.SEVEN_OF_CLUBS);
		Hand h2 = new Hand(Card.KING_OF_CLUBS, Card.NINE_OF_SPADES);
		Board b = new Board(Card.TWO_OF_DIAMONDS, Card.EIGHT_OF_SPADES, Card.FOUR_OF_CLUBS, 
				Card.TEN_OF_HEARTS, Card.NINE_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp < 0);
	}
	
	/**
	 * The method to test two pair vs counterfeit two pair
	 * */
	@Test
	public void testTwoPair(){
		Hand h1 = new Hand(Card.NINE_OF_CLUBS, Card.THREE_OF_SPADES);
		Hand h2 = new Hand(Card.EIGHT_OF_CLUBS, Card.KING_OF_CLUBS);
		Board b = new Board(Card.NINE_OF_HEARTS, Card.THREE_OF_HEARTS, Card.TEN_OF_CLUBS, 
				Card.TEN_OF_HEARTS, Card.KING_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp < 0);
	}
	
	/**
	 * The method to test flush over 3 of a kind
	 * */
	@Test
	public void testFlushThreeOfKind(){
		Hand h1 = new Hand(Card.TEN_OF_CLUBS, Card.EIGHT_OF_CLUBS);
		Hand h2 = new Hand(Card.KING_OF_SPADES, Card.KING_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_CLUBS, Card.THREE_OF_CLUBS, Card.FOUR_OF_CLUBS, 
				Card.KING_OF_HEARTS, Card.SEVEN_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp > 0);
	}
	
	/**
	 * The method to test flush over straight
	 * */
	@Test
	public void testFlushStraight(){
		Hand h1 = new Hand(Card.TEN_OF_CLUBS, Card.EIGHT_OF_CLUBS);
		Hand h2 = new Hand(Card.FIVE_OF_SPADES, Card.SIX_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_CLUBS, Card.THREE_OF_CLUBS, Card.FOUR_OF_CLUBS, 
				Card.KING_OF_HEARTS, Card.ACE_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp > 0);
	}
	
	/**
	 * The method to test straight over pair
	 * */
	@Test
	public void testStraight(){
		Hand h1 = new Hand(Card.KING_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		Hand h2 = new Hand(Card.FIVE_OF_SPADES, Card.SIX_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_CLUBS, Card.THREE_OF_CLUBS, Card.FOUR_OF_CLUBS, 
				Card.KING_OF_HEARTS, Card.ACE_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp < 0);
	}
	
	/**
	 * The method to test split
	 * */
	@Test
	public void testSplit(){
		Hand h1 = new Hand(Card.TEN_OF_CLUBS, Card.EIGHT_OF_CLUBS);
		Hand h2 = new Hand(Card.TEN_OF_DIAMONDS, Card.NINE_OF_HEARTS);
		Board b = new Board(Card.TWO_OF_CLUBS, Card.TWO_OF_DIAMONDS, Card.TEN_OF_SPADES, 
				Card.KING_OF_HEARTS, Card.ACE_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp == 0);
	}
	
	/**
	 * The method to test 3 pair.
	 * */
	@Test
	public void testThreePair(){
		Hand h1 = new Hand(Card.TEN_OF_DIAMONDS, Card.TEN_OF_CLUBS);
		Hand h2 = new Hand(Card.ACE_OF_DIAMONDS, Card.QUEEN_OF_SPADES);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.EIGHT_OF_DIAMONDS,
				Card.JACK_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		int comp = compare(h1,h2,b);
		assertTrue("Comp is " + comp,comp > 0);
	}
	
	/**
	 * The method to test the one pair.
	 * */
	@Test
	public void testOnePairType(){
		Hand h1 = new Hand(Card.TEN_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.TWO_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.PAIR, rank.getHandType());
	}
	
	/**
	 * The method to test two pair.
	 * */
	@Test 
	public void testTwoPairType(){
		Hand h1 = new Hand(Card.TEN_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.JACK_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.TWO_PAIR, rank.getHandType());
	}
	
	/**
	 * The method to test the Three Of A Kind.
	 * */
	@Test
	public void testThreeOfAKindType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.FIVE_OF_CLUBS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.JACK_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.THREE_OF_A_KIND, rank.getHandType());
	}
	
	/**
	 * The method to test the Full House.
	 * */
	@Test
	public void testFullHouseType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.JACK_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.FULL_HOUSE, rank.getHandType());
	}
	
	/**
	 * The method to test the Straight.
	 * */
	@Test
	public void testStraightType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.TEN_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.QUEEN_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.STRAIGHT, rank.getHandType());
	}
	
	/**
	 * The method to test the Flush.
	 * */
	@Test
	public void testFlushType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.NINE_OF_DIAMONDS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.TWO_OF_DIAMONDS,
				Card.FIVE_OF_DIAMONDS, Card.EIGHT_OF_DIAMONDS);
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.FLUSH, rank.getHandType());
	}
	
	/**
	 * The method to test the Four Of A Kind.
	 * */
	@Test
	public void testFourOfAKindType(){
		Hand h1 = new Hand(Card.TWO_OF_DIAMONDS, Card.SIX_OF_CLUBS);
		Board b = new Board(Card.TWO_OF_HEARTS, Card.TWO_OF_CLUBS, Card.NINE_OF_DIAMONDS,
				Card.TWO_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.FOUR_OF_A_KIND, rank.getHandType());
	}
	
	/**
	 * The method to test the Straight Flush.
	 * */
	@Test
	public void testStraightFlushType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.TEN_OF_DIAMONDS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.QUEEN_OF_DIAMONDS, Card.EIGHT_OF_DIAMONDS);
		TwoPlusTwoHandEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.STRAIGHT_FLUSH, rank.getHandType());
	}
	
	/**
	 * The method to compare the 2 hands with the board. Return the hand has bigger value. Compareto equivalent to h1.compareTo(h2) using Two Plus Two Algorithm
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