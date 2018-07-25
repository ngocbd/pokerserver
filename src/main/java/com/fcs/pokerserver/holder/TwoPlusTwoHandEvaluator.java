package com.fcs.pokerserver.holder;
import java.util.Iterator;

import com.fcs.pokerserver.Card;



/**
 * Poker hand Evaluation algorithm based on the two plus two 7 card hand evaluation algorithm.
 * 
 * @author ngocbd
 */
public class TwoPlusTwoHandEvaluator  {

	private static final String HAND_RANKS = "HandRanks.zip";
	private int[] handRanks;
	
	private static TwoPlusTwoHandEvaluator instance;
	
	public TwoPlusTwoHandEvaluator() throws Exception{
		ConfigurationLoader reader = new ConfigurationLoader();
		
			handRanks = reader.loadHandRankResource(HAND_RANKS);
		
	}
	
	/**
	 * The two plust two lookup table is very memory intensive.  You should only ever create
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