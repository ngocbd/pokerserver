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

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;


import com.fcs.pokerserver.events.*;
import com.fcs.pokerserver.holder.Board;
import com.fcs.pokerserver.holder.Hand;
import com.fcs.pokerserver.holder.HandRank;
import com.fcs.pokerserver.holder.TwoPlusTwoHandEvaluator;

import javax.management.MBeanServer;
import javax.management.ObjectName;


/**
 * An instance of the Game class is created Game to Player play Poker Game. This is the most important class in project.
 *
 * @category com > fcs > pokerserver
 */

public class Game implements AbstractPlayerListener, GameMBean {

    private List<Player> listPlayer = new ArrayList<Player>();
    private Board board = new Board();
    private Deck deck = null;
    private long id;
    private long potBalance = 0;
    private long currentRoundBet = 0;
    private short round = 0;
    private Room room;
    private GameStatus status;
    private Player dealer;
    private int dealer_index;
    private Player bigBlind;
    private Player smallBlind;
    private Player currentPlayer = null;
    private String rank = "";
    /**
     * CODE PREPARING FOR SPLIT POT IN CASE OF MULTIPLE WINNERS.
     */
    private List<Player> winners = new ArrayList<>();
    private List<Hand> bestHands = new ArrayList<>();

    private LocalDateTime startTime = null; // meaning not started

    private List<GameListener> listeners = new ArrayList<GameListener>();

    @Override
    public String toString() {
        String flopcard = null;
        String turncard = null;
        String rivercard = null;
        try {
            flopcard = board.getFlopCards().toString();
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            turncard = board.getTurnCard().toString();
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            rivercard = board.getRiverCard().toString();
        } catch (IndexOutOfBoundsException e) {
        }
        StringBuilder data = new StringBuilder("{\"id\":" + this.getId() + ",\"potBalance\":" + this.getPotBalance() + ",\"currentRoundBet\":" + this.getCurrentRoundBet());
        data.append(",\"gameStatus\":\"" + this.getStatus().toString());
        data.append("\",\"dealer\":\"" + (this.getDealer() != null ? this.getDealer().getId() : null));
        data.append("\",\"bigBlind\":\"" + (this.getBigBlind() != null ? this.getBigBlind().getId() : null));
        data.append("\",\"smallBlind\":\"" + (this.getSmallBlind() != null ? this.getSmallBlind().getId() : null));
        data.append("\",\"currentPlayer\":\"" + (this.getCurrentPlayer() != null ? this.getCurrentPlayer().getId() : null));
        data.append("\",\"flopCard\":" + flopcard);
        data.append(",\"turncard\":" + turncard);
        data.append(",\"rivercard\":" + rivercard);
        data.append(",\"players\":" + listPlayer + "}");
        return data.toString();
    }

    /**
     * Create new Game in Room
     *
     * @param Room room
     */
    public Game(Room room) {
        this.room = room;
        this.setId(System.nanoTime());
        this.deck = new Deck();
        this.deck.initDeck();
        this.deck.shuffleDeck();
        this.setStatus(GameStatus.NOT_STARTED);
        registerMBean();

    }

    /**
     * Start Game.
     *
     * @throws AssertionError if the total of Players < 2.
     */
    public boolean startGame() {
        if (this.getStatus() != GameStatus.NOT_STARTED) {
            System.out.println("Game is already start (Game.java-132)");
            return false;
        }
        if (this.listPlayer.size() < 2) {
            System.out.println("Game " + this.getId() + " in Room-" + this.getRoom().getRoomID() + " Cannot start due to not enough players!");
            StartFailedGameEvent event = new StartFailedGameEvent(this, GameAction.FAILEDSTART);
            event.setMsg("Not enough player!");
            event.setListPlayers(this.listPlayer);
            this.fireEvent(event);
            return false;
        }
//        assert this.listPlayer.size() >= 2;

        // setting postion of player
        this.setStatus(GameStatus.SEATING);
        this.startTime = LocalDateTime.now();

        /**
         * Reset all-in flag of all players*/
        this.listPlayer.stream().forEach(x -> {
            x.setDidAllIn(false);
            x.setCommandThisTurn(false);
        });

        RoundGameEvent gameEvent = new RoundGameEvent(this, GameAction.WAITTING);
        this.fireEvent(gameEvent);
        this.preflop();
        System.out.println("dealer: " + dealer.getId());
        System.out.println("sb: " + smallBlind.getId());
        System.out.println("bb: " + bigBlind.getId());
        return true;
    }

    /**
     * Pre-flop play refers to the action that occurs before the flop is dealt. A game begins with the small blind and big blind posting the blinds, and cards are dealt to each player.
     *
     * @throws AssertionError if the total of Players < 2.
     */
    public void preflop() {
        if (this.listPlayer.size() < 2) {
            System.out.println("Game " + this.getId() + " in Room-" + this.getRoom().getRoomID() + " Cannot start due to not enough players!");
            return;
        }
        /**VERY IMPORTANT to reset all Hand of Player before deal card to avoid error of evaluator.*/
        listPlayer.stream().forEach(p -> {
            p.setPlayerHand(new Hand());
            p.setRoundBet(0);
        });
//        assert this.listPlayer.size() >= 2;
        //reset command flag.
        this.resetCommandFlag();
        this.setStatus(GameStatus.PREFLOP);
        long betBigBlind = this.getRoom().getBlindLevel().getBigBlind();
        long betSmallBlind = this.getRoom().getBlindLevel().getSmallBlind();
        this.setCurrentPlayer(this.getSmallBlind());
        this.getSmallBlind().bet(betSmallBlind);
        this.getBigBlind().bet(betBigBlind);
        /**
         * BigBlind will not be count as "Had Action" in his Preflop first bet*/
        this.getBigBlind().setCommandThisTurn(false);
        // deal 2 card for each player // unordered // begin from master // need to fix to begin from dealer
        for (int i = 0; i < 2; i++) {
            for (Player player : listPlayer) {
                Card card = this.deck.dealCard();
                player.getPlayerHand().addCard(card);
            }

        }
        RoundGameEvent gameEvent = new RoundGameEvent(this, GameAction.PREFLOP);
        this.fireEvent(gameEvent);

        // current player is very hard to assign base on number of player
        this.setRound((short) 1);


    }


    /**
     * the first three community cards that are dealt face-up in the center of the table all at one time. The "flop" also indicates the second round of betting.
     *
     * @throws AssertionError if the next round of the Player is not Ready
     */
    public void flop() {

//        assert this.isNextRoundReady();
        this.resetCommandFlag();
        for (Player player : listPlayer) {

            player.nextRound();

        }
//		Card ignoreCard = this.deck.dealCard(); // ignore top card
        for (int i = 0; i < 3; i++) {
            Card card = this.deck.dealCard();
            getBoard().addCard(card);
        }
        RoundGameEvent gameEvent = new RoundGameEvent(this, GameAction.FLOP);
        this.fireEvent(gameEvent);
        this.setStatus(GameStatus.FLOP);

//        this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
        this.setCurrentBet(0);

        this.setRound((short) 2);
    }

    /**
     * In turn games, this is the fourth card deal. It is the third round of betting
     *
     * @throws AssertionError Next Round of Player is ready
     */
    public void turn() {
//        assert this.isNextRoundReady();
        this.resetCommandFlag();

//		for (Player player : listPlayer) {
//			
//			assert player.isSittingOut() || player.didCommandThisTurn();
//			player.nextRound();
//		}
        Card card = this.deck.dealCard();
        this.getBoard().addCard(card);
        RoundGameEvent gameEvent = new RoundGameEvent(this, GameAction.TURN);
        this.fireEvent(gameEvent);
        this.setStatus(GameStatus.TURN);
        //TODO need to check before current player fold or not
//        this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
        this.setCurrentBet(0);

        //this.setRound((short) 3);
    }

    /**
     * This is the last card given in all games.
     *
     * @throws AssertionError Next Round of Player is ready.
     */
    public void river() {
//        assert this.isNextRoundReady();
        this.resetCommandFlag();

        Card card = this.deck.dealCard();
        this.getBoard().addCard(card);
        RoundGameEvent gameEvent = new RoundGameEvent(this, GameAction.RIVER);
        this.fireEvent(gameEvent);
        this.setStatus(GameStatus.RIVER);
        //this.setRound((short) 4);
        this.setCurrentBet(0);

    }

    /**
     * Finish the game. Show the winner Player.
     */
    public void endGame() {
        if (this.getStatus() == GameStatus.END_HAND) return;
        winners = new ArrayList<>();
        bestHands = new ArrayList<>();
        this.setStatus(GameStatus.END_HAND);
        List<Hand> list = new ArrayList<>();
        for (int i = 0; i < this.getListPlayer().size(); i++) {
            Player p = this.getListPlayer().get(i);
            if (p.isSittingOut()) continue;
            list.add(p.getPlayerHand());
        }
        Board b1 = this.getBoard();
        Board b = new Board(b1.getFlopCards().get(0), b1.getFlopCards().get(1), b1.getFlopCards().get(2), b1.getTurnCard(), b1.getRiverCard());
        this.setBoard(b);

        list.sort(new Comparator<Hand>() {
            public int compare(Hand o1, Hand o2) {
                return TwoPlusTwoHandEvaluator.compare(o1, o2, b);
            }
        });
        TwoPlusTwoHandEvaluator evaluator = TwoPlusTwoHandEvaluator.getInstance();
        Hand highestHand = list.get(list.size() - 1);
        //Add best hand into list.
        HandRank highestRank = evaluator.evaluate(b, highestHand);
        bestHands.add(highestHand);

        //Add Win Player into list Player.
        Player player = findPlayerFromHand(this.getListPlayer(), highestHand);
        winners.add(player);
        /**
         * Check whether multiple winners case occured*/
        findCo_Winner(bestHands, winners, list, listPlayer, evaluator, b);


        //rank of winner player
        System.out.println("GameID: " + this.getId());
        System.out.println("Highest Rank value: " + highestRank.getValue());
        rank = highestRank.getHandType().toString();


/**
 * Check all-in situation*/
        if (this.listPlayer.stream().filter(x -> !x.isSittingOut()).anyMatch(x -> x.isDidAllIn())) {
            //TODO Code split pot here (only 1 winner case-- Need upgrade later).
            splitSidePot(listPlayer, winners, b, false);

        } else {
            /**
             * Temporary add winning money to winner balance (Need enhancement later)
             * */
            //Find out the money to be claim for each player.
            long moneyTobeClaim = this.potBalance / (winners.size());
            System.out.println("moneyTobeClaim: " + moneyTobeClaim);
            for (Player p : winners) {
                p.setBalance(p.getBalance() + moneyTobeClaim);
            }
            EndGameEvent gameEvent = new EndGameEvent(this);
            gameEvent.setBestHands(bestHands);
            gameEvent.setRank(String.valueOf(highestRank.getValue()));
            gameEvent.setPlayerwins(winners);
            this.fireEvent(gameEvent);
        }

    }

    /**
     * Find Co Winners needs:
     *
     * @param bestHands  List of hands with highest hand already inside
     * @param winners    List of Winners that have highest-hand winner already inside
     * @param listHand   List of hands that already sorted (contains best hand as the last element)
     * @param listPlayer List of Players
     * @param evaluator  Evaluator used for calculating hand rank
     * @param b          Board of the game
     */
    public void findCo_Winner(List<Hand> bestHands, List<Player> winners, List<Hand> listHand, List<Player> listPlayer, TwoPlusTwoHandEvaluator evaluator, Board b) {
        if (listHand.size() > 1) {
            Hand highestHand = bestHands.get(0);
            HandRank highestRank = evaluator.evaluate(b, highestHand);
            for (int i = listHand.size() - 2; i >= 0; i--) {
                Hand temp = listHand.get(i);
                HandRank tempRank = evaluator.evaluate(b, temp);
                if (tempRank.getValue() < highestRank.getValue()) break;
                bestHands.add(temp);
                Player p = findPlayerFromHand(listPlayer, temp);
                winners.add(p);
            }
        } else System.out.println("Co-winner not exist in game " + this.id);

    }


    public void endGameSoon(Player p) {
        if (this.getStatus() == GameStatus.END_HAND) return;
        this.setStatus(GameStatus.END_HAND);
        winners = new ArrayList<>();
        bestHands = new ArrayList<>();
        bestHands.add(p.getPlayerHand());
        winners.add(p);
        rank = "endsoon";
        EndGameEvent gameEvent = new EndGameEvent(this);
        gameEvent.setPlayerwins(winners);
        gameEvent.setBestHands(bestHands);
        gameEvent.setRank(rank);
        p.setBalance(p.getBalance() + this.potBalance);
        this.fireEvent(gameEvent);

    }

    /**
     * This is the case of only 1 player win
     * TODO need upgrade into multiple winners later
     */
    public void splitSidePot(List<Player> listPlayer, List<Player> winners, Board b, boolean sideWinners) {
        TwoPlusTwoHandEvaluator evaluator = TwoPlusTwoHandEvaluator.getInstance();

        List<Player> temp_list = new ArrayList<>(listPlayer);
        temp_list.removeAll(winners);

        //Firstly, Winners should receive their betting money back.
        for (Player winner : winners) {
            winner.setBalance(winner.getBalance() + winner.getGameBet());

        }
        /**
         * List of co-winner need to be sorted according to their gamebet*/
        winners.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getGameBet() > o2.getGameBet()) return 1;
                if (o1.getGameBet() < o2.getGameBet()) return -1;
                return 0;
            }
        });
        /**
         * Logic is :
         * Find the lowest gamebet winner, find his grant from loser then divide for all co-winners
         * Remove him from winners list and do similarly until no co-winner left*/
        /**
         * Divide winning money for all winners base on lowest bet winner. Stop when no co-winner left */
        grantForWinners(winners, temp_list);

        if (sideWinners) {
            SideWinnerGameEvent e = new SideWinnerGameEvent(this);
            e.setWinners(winners);
            List<Hand> hands = new ArrayList<>();
            winners.stream().forEach(x -> hands.add(x.getPlayerHand()));
            e.setHands(hands);
            e.setRank(evaluator.evaluate(b, winners.get(0).getPlayerHand()).getHandType().toString());
            this.fireEvent(e);
        } else {
            EndGameEvent gameEvent = new EndGameEvent(this);
            List<Hand> bestHands = new ArrayList<>();
            winners.stream().forEach(x -> bestHands.add(x.getPlayerHand()));
            HandRank highestRank = evaluator.evaluate(b, winners.get(0).getPlayerHand());

            gameEvent.setBestHands(bestHands);
            gameEvent.setRank(String.valueOf(highestRank.getValue()));
            gameEvent.setPlayerwins(winners);
            this.fireEvent(gameEvent);
        }

        winners = new ArrayList<>();

        /**
         * Now find the list of players in side pot, side pot contains all players that has gamebet remaining*/
        List<Player> sidePot = temp_list.stream().filter(x -> x.getGameBet() != 0).collect(Collectors.toList());
        //Side pot is empty then return
        if (sidePot.isEmpty()) {
            return;
        } else {
            System.out.println("sidepot: " + sidePot);
        }

        List<Hand> handlist = new ArrayList<>();
        sidePot.stream().forEach(x -> handlist.add(x.getPlayerHand()));
        //Find the winner in side-pot
        handlist.sort(new Comparator<Hand>() {
            public int compare(Hand o1, Hand o2) {
                return TwoPlusTwoHandEvaluator.compare(o1, o2, b);
            }
        });
        List<Hand> bestHands = new ArrayList<>();
        bestHands.add(handlist.get(handlist.size() - 1));
        for (int i = 0; i < sidePot.size(); i++) {
            if (sidePot.get(i).getPlayerHand() == bestHands.get(0)) {
                winners.add(sidePot.get(i));
                break;
            }
        }
        /**
         * using findCo-Winner to add all co-winner into winners list*/
        findCo_Winner(bestHands, winners, handlist, listPlayer, evaluator, b);
        splitSidePot(sidePot, winners, b, true);


    }

    /**
     * Grants winning money for all co-winner base on lowest gamebet winner
     * <p>
     * stop when no co-winner left.
     *
     * @param winners   list of winners that already sorted ascending gamebet.
     * @param temp_list list of losers.
     */
    private void grantForWinners(List<Player> winners, List<Player> temp_list) {
        //Filter all losers that has no remaining gamebet, remove all of them.
        List<Player> temp_list_filtered = temp_list.stream().filter(x -> x.getGameBet() != 0).
                collect(Collectors.toList());
        Player lowestWinner = winners.get(0);


        // Find the grant that divide for all co-winners.
        long grantMoneyForAll = 0;
        for (Player p : temp_list_filtered) {
            if (p.getGameBet() <= lowestWinner.getGameBet()) {
                grantMoneyForAll += p.getGameBet();
                p.setGameBet(0);
            } else {
                grantMoneyForAll += lowestWinner.getGameBet();
                p.setGameBet(p.getGameBet() - lowestWinner.getGameBet());
            }
        }
        /**
         * Now we distribute grantMoneyForAll for all co-winners*/
        for (Player p : winners) {
            p.setBalance(p.getBalance() + (grantMoneyForAll / winners.size()));
        }
        /**
         * Subtract lowestWinner gamebet from the other co-winners gamebet and set gamebet of lowestwinner back to 0
         * Collect new winners list that winners remains gamebet*/
        for (int i = 0; i < winners.size(); i++) {
            Player p = winners.get(i);
            p.setGameBet(p.getGameBet() - lowestWinner.getGameBet());
        }
        List<Player> newWinnersList = winners.stream().filter(x -> x.getGameBet() != 0).collect(Collectors.toList());

        if (newWinnersList.isEmpty()) return;
        /**
         * Then we sort new winner list base on game bet*/
        newWinnersList.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getGameBet() > o2.getGameBet()) return 1;
                if (o1.getGameBet() < o2.getGameBet()) return -1;
                return 0;
            }
        });

        /**
         * And finally keep recursively call this method*/
        grantForWinners(newWinnersList, temp_list);


    }

    public Player findPlayerFromHand(List<Player> listPlayer, Hand hand) {
        Player p = null;
        for (int y = 0; y < listPlayer.size(); y++) {
            if (listPlayer.get(y).isSittingOut()) continue;
            if (listPlayer.get(y).getPlayerHand() == hand) {
                p = listPlayer.get(y);
                break;
            }
        }
        return p;
    }

    public void autoNextRound() {
        this.listPlayer.stream().filter(x -> !x.isSittingOut()).forEach(x -> x.setRoundBet(0));
        switch (this.status) {
//            case NOT_STARTED:
//            case SEATING:
//                this.preflop();
//                break;
            case PREFLOP:
                this.flop();
                break;
            case FLOP:
                this.turn();
                break;
            case TURN:
                this.river();
                break;
            case RIVER:
                this.endGame();
                break;
        }
    }

    /**
     * Get Deck
     *
     * @return Deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Set value for Deck
     * @param Deck deck
     * */
//	public void setDeck(Deck deck) {
//		this.deck = deck;
//	}

    /**
     * Get "the money or chips"(Pot) in the center of a table that players try to win
     *
     * @return long Pot
     */
    public long getPotBalance() {
        return potBalance;
    }

    /**
     * Set(inscrease) the money or chips in the center of a table.
     * @param add more the money or chips(long pot) of players  in the center of a table.
     * @return void
     * */
//	public void setPotBalance(long potBalance) {
//		this.potBalance = potBalance;
//	}

    /**
     * Get room in game
     *
     * @return Room room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * ReSet Room.
     *
     * @param Room room
     * @return void
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Add player into the game
     *
     * @param Player p
     */
    public void addPlayer(Player p) {
        if (listPlayer.contains(p)) return;
        // check if timeout join after 15 second then Reject
        if (this.startTime == null || Duration.between(this.startTime, LocalDateTime.now()).getSeconds() <= 15) {
            if (listPlayer.size() < 6) {
                p.setSittingOut(false);
                this.listPlayer.add(p);
                p.attachListener(this);
                p.setCurrentGame(this);
            } else {
                p.setSittingOut(true);
            }

        } else {
            throw new RejectedExecutionException("Reject player join because 15 seconds is timeout");
        }

    }

    /**
     * Reset CommmandThisTurn flag of all playing player
     **/
    public void resetCommandFlag() {
        this.listPlayer.stream().filter(p -> !p.isSittingOut()).forEach(p -> {
            p.setCommandThisTurn(false);
        });
    }

    /**
     * Return List of Player in Game
     *
     * @return List<Player> list
     */
    public List<Player> getListPlayer() {
        return listPlayer;
    }

    /**
     * Return data of all Players in Game, with hand encrypted via AES algorithm
     *
     * @return String Players information in game with encrypted hand.
     */
    public String getDataPlayers() {
        StringBuilder builder = new StringBuilder("[");
        for (Player p : this.listPlayer) {
            builder.append("{\"id\":\"");
            builder.append(p.getId());
            builder.append("\",");
            builder.append("\"balance\":");
            builder.append(p.getBalance());
            builder.append(",");
            builder.append("\"globalBalance\":");
            builder.append(p.getGlobalBalance());
            builder.append(",");
            builder.append("\"hand\":\"");
            builder.append(p.getEncryptedHand("AES"));
            builder.append("\"},");
        }
        builder.setLength(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

//	private void setListPlayer(List<Player> listPlayer) {
//		this.listPlayer = listPlayer;
//	}

    /**
     * Return Id of Game
     *
     * @return long id
     */
    public long getId() {
        return id;
    }

    /**
     * Set Id for Game
     *
     * @param long id
     */
    private void setId(long id) {
        this.id = id;
    }

    /**
     * Return Status of Game
     *
     * @return GameStatus gameStatus
     */
    public GameStatus getStatus() {
        return status;
    }

    private void setStatus(GameStatus status) {
        this.status = status;
    }

    /**
     * Return Player is Dealer
     *
     * @return Player Dealer of game
     */
    public Player getDealer() {
        return dealer;
    }

    public void setListPlayer(List<Player> listPlayer) {
        this.listPlayer = listPlayer;
    }
//	private int getIndexPlayerList(Player player) {
//		int index = 0;
//		for (; index < this.getListPlayer().size(); index++) {
//			if (this.getListPlayer().get(index) == player) {
//				break;
//			}
//		}
//		return index;
//	}

    /**
     * Return Player is Next Player of Game
     *
     * @param Player p
     * @return Player
     * @throws AssertionError the list of Players is not contain the Player.
     */
    public Player getNextPlayer(Player p) {
//        System.out.println(listPlayer.toString());
        assert listPlayer.contains(p);
        Player temp = null;
        for (int i = 0; i < this.getListPlayer().size(); i++) {
            //Find the player we are starting at
            if (this.getListPlayer().get(i).equals(p)) {
                //The next player is either the next in the list, or the first in the list if startPlayer is at the end
                temp = (i == this.getListPlayer().size() - 1) ? this.getListPlayer().get(0) : this.getListPlayer().get(i + 1);
                break;
            }
        }
        if (temp.isSittingOut()) {
            return getNextPlayer(temp);
//            return temp;
        } else
            return temp;
    }

    /**
     * Set the player is Dealer in the game
     *
     * @param Player dealer
     * @throws AssertionError the list of Players is not contain the Player.
     */
    public void setDealer(Player dealer) {
        assert this.listPlayer.contains(dealer);
        this.dealer = dealer;
        this.smallBlind = this.getNextPlayer(this.dealer);
        this.bigBlind = this.getNextPlayer(this.smallBlind);
        this.dealer_index = this.listPlayer.indexOf(dealer);
    }

    public int getDealer_index() {
        return dealer_index;
    }

    public void setDealer_index(int dealer_index) {
        this.dealer_index = dealer_index;
    }

    /**
     * Return Player is Big Blind
     *
     * @return Player Big Blind of game
     */
    public Player getBigBlind() {
        return bigBlind;
    }

    /**
     * Set the player is Big Blind in the game
     * @param Player bigBlind
     * */

    /**
     * Return Player is Small Blind
     *
     * @return Player Big Small of game
     */
    public Player getSmallBlind() {
        return smallBlind;
    }

    /**
     * Set the player is Small Blind in the game
     * @param Player smallBlind
     * */
    /**
     * Return The cards of Board on the table in the game
     *
     * @return Board cards
     */
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    /**
     * Set cards of Board on the table in game
     * @param Board board
     * */
//	public void setBoard(Board board) {
//		this.board = board;
//	}

    /**
     * Add Listener for the game
     *
     * @param GameListener gl
     */
    public void addGameListener(GameListener gl) {
        this.listeners.add(gl);
    }

    private void fireEvent(AbstractGameEvent ge) {
        for (Iterator iterator = this.listeners.iterator(); iterator.hasNext(); ) {
            GameListener listener = (GameListener) iterator.next();
            listener.actionPerformed(ge);
        }
    }

    public void dumpListPlayer() {
        for (Player p : listPlayer) {
            p.detachListener();
        }
    }

    /**
     * Return the Next Round Ready for Player
     *
     * @return boolean is next round ready
     */
    public boolean isNextRoundReady() {
        //TODO need more test and code review
        boolean hasPlayerNotAct = this.listPlayer.stream()
                .filter(p -> !p.isSittingOut())
                .anyMatch(p -> !p.didCommandThisTurn());
        boolean doAllPlayersBetEqual = !this.listPlayer.stream()
                .filter(x -> !x.isSittingOut()).filter(x -> !x.isDidAllIn())
                .filter(x -> x.getRoundBet() != this.getCurrentRoundBet())
                .findAny().isPresent();
//        System.out.println("doAllPlayersBetEqual: "+doAllPlayersBetEqual);
//        System.out.println("hasPlayerNotAct: "+hasPlayerNotAct);
        return doAllPlayersBetEqual && !hasPlayerNotAct;
    }


    /**
     * Return the Player has problem. The player has current Round Bet is different for other players.
     * @return Player PlayerHasProblem
     * */
//	public Player getPlayerHasProblem()
//	{
//		//TODO need more test and code review
//		return this.listPlayer.stream()
//				.filter(x->!x.isSittingOut())
//				.filter(x -> x.getRoundBet() != this.getCurrentRoundBet() || x.getRound() != this.getRound())
//				.findAny().orElse(null);
//	}

    /**
     * Return all list of players to become playerListString.
     * @return String dumpListPlayer.
     * */
//	public String dumpListPlayer()
//	{
//		ObjectMapper mapper = new ObjectMapper();
//
//
//
//		String jsonInString ="Error when dump Object";
//		try {
//			 jsonInString = mapper.writeValueAsString(this.listPlayer);
//		} catch (JsonGenerationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return jsonInString;
//	}

    /**
     * Override the actionPerformed method according to Type of EVENT to make sure the Player has action need in the game.
     *
     * @param AbstractPlayerEvent e.
     * @return void.
     * @throws AssertionError if the Player is not the current player or the player is not in game. The Round of Bet is not less than the current round bet.
     */
    @Override
    public void actionPerformed(AbstractPlayerEvent e) {

        if (this.status == GameStatus.END_HAND) {
            System.out.println("Game is ended. Nothing could happens now.");
            return;
        }
        Player p = e.getSrc();
        if (p != this.currentPlayer) {
            System.out.println("This is not current player: " + p.getId() + " current is : " + this.currentPlayer.getId());
            return;
        }
        if (p.isSittingOut()) {
            System.out.println("This player: " + p.getId() + " is folded. Cannot make more actions!");
            return;
        }
//        assert p == this.getCurrentPlayer();
        if (listPlayer.contains(p)) {
            if (e instanceof PlayerBetAllEvent) {
                PlayerBetAllEvent pae = (PlayerBetAllEvent) e;
                p.setCommandThisTurn(true);
                this.potBalance += pae.getAmount();
                if (this.currentRoundBet < p.getRoundBet()) this.currentRoundBet = p.getRoundBet();
                PlayerActionGameEvent ge = new PlayerActionGameEvent(this);
                ge.setE(pae);
                this.fireEvent(ge);
                if (isNextRoundReady()) {
                    autoNextRound();
                    this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
                } else {
                    this.setCurrentPlayer(this.getNextPlayer(p));
                }

            }
            if (e instanceof PlayerBetEvent) {
                PlayerBetEvent pbe = (PlayerBetEvent) e;
                assert p.getRoundBet() >= this.currentRoundBet;
                this.potBalance += pbe.getAmount();
                this.currentRoundBet = p.getRoundBet(); // set current bet equal to this bet amount
                /**
                 * This player has action now.
                 **/

                p.setCommandThisTurn(true);
                PlayerActionGameEvent ge = new PlayerActionGameEvent(this);
                ge.setE(pbe);
                this.fireEvent(ge);
                //TODO Temporary set check next round for game
                // if next round ready then next Player will be left person of dealer
                if (isNextRoundReady()) {
                    autoNextRound();
                    this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
                } else {
                    Player next = this.getNextPlayer(p);
                    if (next != null) {
                        this.setCurrentPlayer(next);
                    }
                }

            }
            if (e instanceof PlayerFoldEvent) {
                PlayerFoldEvent pfe = (PlayerFoldEvent) e;
                p.setSittingOut(true);
                PlayerActionGameEvent ge = new PlayerActionGameEvent(this);
                ge.setE(pfe);
                this.fireEvent(ge);

//              This player has action now.
                p.setCommandThisTurn(true);

                /**
                 * Check if there is only 1 player playing after this player fold then endgame immediately*/
                int i = 0;
                Player temp = null;
                for (Player player : this.listPlayer) {
                    if (!player.isSittingOut()) {
                        i++;
                        temp = player;
                    }
                }
                if (i == 1) {
                    temp.getCurrentGame().endGameSoon(temp);
                } else {
                    if (isNextRoundReady()) {
                        autoNextRound();
                        this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
                    } else
                        this.setCurrentPlayer(this.getNextPlayer(p));
                }


            }
            if (e instanceof PlayerCheckEvent) {
                Player player = e.getSrc();
                if (player.getRoundBet() != this.currentRoundBet && !p.isDidAllIn()) {
                    System.out.println("Check cannot happen: player bet: " + player.getRoundBet() + " Roundbet: " + this.currentRoundBet);
                    return;
                }
                PlayerCheckEvent pce = (PlayerCheckEvent) e;
                PlayerActionGameEvent ge = new PlayerActionGameEvent(this);
                ge.setE(pce);
                this.fireEvent(ge);

//              This player has action now.
                p.setCommandThisTurn(true);
                if (isNextRoundReady()) {
                    autoNextRound();
                    this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
                } else
                    this.setCurrentPlayer(this.getNextPlayer(p));
            }
            if (e instanceof GetTurnPlayerEvent) {
                GetTurnPlayerEvent gte = (GetTurnPlayerEvent) e;
                PlayerActionGameEvent ge = new PlayerActionGameEvent(this);
                ge.setE(gte);
                this.fireEvent(ge);
            }

        }
    }

    /**
     * Return the current Player in Game
     *
     * @return Player current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Set the player is current player.
     *
     * @param Player p
     */
    public void setCurrentPlayer(Player p) {
        this.currentPlayer = p;
        this.currentPlayer.myTurn();
    }

    /**
     * Return the player has current round bet
     *
     * @return long value of the player has current round bet
     */
    public long getCurrentRoundBet() {
        return currentRoundBet;
    }

    /**
     * Set the current Bet for the player in the game
     *
     * @param long currentBet
     */
    public void setCurrentBet(long currentBet) {
        this.currentRoundBet = currentBet;
    }

    /**
     * Return the round of the game.
     *
     * @return short value of round of the game.
     */
    public short getRound() {
        return round;
    }

    /**
     * Set the round for game.
     *
     * @param short round
     */
    public void setRound(short round) {
        this.round = round;
    }

    public List<Player> getWinners() {
        return winners;
    }

    public String getRank() {
        return rank;
    }

    public List<Hand> getBestHands() {
        return bestHands;
    }

    public void registerMBean() {
        try {
            MBeanServer sv = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("com.fcs.pokerserver:type=Game,id=" + this.getId());
            sv.registerMBean(this, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public long jmx_getPotBalance() {
        return this.potBalance;
    }

    @Override
    public void jmx_setPotBalance(long bal) {
        this.potBalance = bal;
    }

    @Override
    public long jmx_getCurrentRoundBet() {
        return this.currentRoundBet;
    }

    @Override
    public void jmx_setCurrentRoundBet(long bal) {
        this.currentRoundBet = bal;
    }

    @Override
    public String jmx_getListPlayer() {
        return this.listPlayer.toString();
    }

    @Override
    public void jmx_kickPlayer(String id) {
        Player pl = this.listPlayer.stream().filter(p -> id.equals(p.getId())).findFirst().orElse(null);
        if (pl != null) listPlayer.remove(pl);
    }

    @Override
    public String jmx_getBoard() {
        return "flop: " + board.getFlopCards().toString() + " turn: " + board.getTurnCard() + " river: " + board.getRiverCard();
    }

    @Override
    public String jmx_getDealer() {
        return this.dealer.toJson();
    }

    @Override
    public String jmx_getSmallBlind() {
        return this.smallBlind.toJson();
    }

    @Override
    public String jmx_getBigBlind() {
        return this.bigBlind.toJson();
    }

    @Override
    public String jmx_setDealer(String id) {
        Player pl = this.listPlayer.stream().filter(p -> id.equals(p.getId())).findFirst().orElse(null);
        this.dealer = pl;
        return pl.toJson();
    }
}
