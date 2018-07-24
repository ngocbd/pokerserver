package com.fcs.pokerserver.holder;
import java.util.Iterator;

import com.fcs.pokerserver.Card;



/**
 * Poker hand Evaluation algorithm based on the two plus two 7 card hand evaluation algorithm.
 * 
 * @author jacobhyphenated
 */
public class TwoPlusTwoHandEvaluator  {

	private static final String HAND_RANKS = "HandRanks.zip";
	private int[] handRanks;
	
	private static TwoPlusTwoHandEvaluator instance;
	
	public TwoPlusTwoHandEvaluator(){
		ConfigurationLoader reader = new ConfigurationLoader();
		try {
			handRanks = reader.loadHandRankResource(HAND_RANKS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The two plust two lookup table is very memory intensive.  You should only ever create
	 * one instance of the class.  Use this method to keep the singleton pattern.
	 * @return {@link TwoPlusTwoHandEvaluator} instance
	 */
	public static TwoPlusTwoHandEvaluator getInstance(){
		if(instance == null){
			instance = new TwoPlusTwoHandEvaluator();
		}
		return instance;
	}
	
	
	public HandRank evaluate(Board board, Hand hand) {
		Iterator<Card> cardIterator = new CardIterator(board, hand);
		int p = 53;
		while(cardIterator.hasNext()){
			Card card = cardIterator.next();
			p = handRanks[p + card.getEvaluation()];
		}
		return new HandRank(p);
	}

}