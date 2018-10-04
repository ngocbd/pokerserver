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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;

import com.fcs.pokerserver.events.*;
import com.fcs.pokerserver.holder.Board;
import com.fcs.pokerserver.holder.Hand;
import com.fcs.pokerserver.holder.HandRank;
import com.fcs.pokerserver.holder.TwoPlusTwoHandEvaluator;
import org.junit.Assert;

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
    private ScheduledExecutorService commander = null;

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
        this.setId(System.currentTimeMillis());
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
    public void startGame() {
        if (this.getStatus() != GameStatus.NOT_STARTED) {
            System.out.println("Game is already start (Game.java-134)");
            return;
        }
        assert this.listPlayer.size() >= 2;

        // setting postion of player
        this.setStatus(GameStatus.SEATING);
        this.startTime = LocalDateTime.now();

        RoundGameEvent gameEvent = new RoundGameEvent(this, GameAction.WAITTING);
        this.fireEvent(gameEvent);
    }

    /**
     * Pre-flop play refers to the action that occurs before the flop is dealt. A game begins with the small blind and big blind posting the blinds, and cards are dealt to each player.
     *
     * @throws AssertionError if the total of Players < 2.
     */
    public void preflop() {
        assert this.listPlayer.size() >= 2;
        //reset command flag.
        this.resetCommandFlag();
        this.setStatus(GameStatus.PREFLOP);
        long betBigBlind = this.getRoom().getBlindLevel().getBigBlind();
        long betSmallBlind = this.getRoom().getBlindLevel().getSmallBlind();
        this.setCurrentPlayer(this.getSmallBlind());
        this.getSmallBlind().bet(betSmallBlind);
        this.getBigBlind().bet(betBigBlind);


        //this.potBalance += (betBigBlind + betSmallBlind);

        //this.deck.dealCard();
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

        assert this.isNextRoundReady();
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
        assert this.isNextRoundReady();
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
        assert this.isNextRoundReady();
        this.resetCommandFlag();

        Card card = this.deck.dealCard();
        this.getBoard().addCard(card);
        RoundGameEvent gameEvent = new RoundGameEvent(this, GameAction.RIVER);
        this.fireEvent(gameEvent);
        this.setStatus(GameStatus.RIVER);
        //this.setRound((short) 4);
    }

    /**
     * Finish the game. Show the winner Player.
     */
    public void endGame() {
        assert this.isNextRoundReady();
        if (this.getStatus() == GameStatus.END_HAND) return;
        winners = new ArrayList<>();
        bestHands = new ArrayList<>();
        this.setStatus(GameStatus.END_HAND);
        EndGameEvent gameEvent = new EndGameEvent(this);

        List<Hand> list = new ArrayList<Hand>();
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
        HandRank highestRank = evaluator.evaluate(b, highestHand);
        //Add best hand into list.
        bestHands.add(highestHand);
        //Add Win Player into list Player.
        for (int i = 0; i < this.getListPlayer().size(); i++) {
            if (this.getListPlayer().get(i).getPlayerHand() == highestHand) {
                winners.add(this.getListPlayer().get(i));
                break;
            }
        }
        /**
         * Check whether multiple winners case occured*/
        if (list.size() > 1) {
            for (int i = list.size() - 2; i >= 0; i--) {
                Hand temp = list.get(i);
                HandRank tempRank = evaluator.evaluate(b, temp);
                if (tempRank.getValue() < highestRank.getValue()) break;
                bestHands.add(temp);
                for (int y = 0; y < this.getListPlayer().size(); y++) {
                    Player p = this.listPlayer.get(y);
                    if (p.isSittingOut()) continue;
                    if (this.getListPlayer().get(y).getPlayerHand() == temp) {
                        winners.add(this.getListPlayer().get(y));
                    }
                }
            }
        }
        //rank of winner player
        rank = highestRank.toString();
        gameEvent.setBestHands(bestHands);
//        gameEvent.setRank(rank);
        gameEvent.setRank(String.valueOf(highestRank.getValue()));
        gameEvent.setPlayerwins(winners);

/**
 * Temporary add winning money to winner balance (Need enhancement later)
 * */
        //Find out the money to be claim for each player.
        long moneyTobeClaim = this.potBalance / (winners.size());
        for (Player p : winners) {
            p.setBalance(p.getBalance() + moneyTobeClaim);
        }
        /**
         * TODO Here need an event for adding winning money.
         * */
//        winner.setBalance(winner.getBalance() + this.potBalance);
        this.fireEvent(gameEvent);


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


    public void autoNextRound() {
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
            if (listPlayer.size() < 8) {
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
        this.listPlayer.stream().filter(p -> !p.isSittingOut()).forEach(p -> p.setCommandThisTurn(false));
    }

    /**
     * Return List of Player in Game
     *
     * @return List<Player> list
     */
    public List<Player> getListPlayer() {
        return listPlayer;
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
                .filter(x -> !x.isSittingOut())
                .filter(x -> x.getRoundBet() != this.getCurrentRoundBet())
                .findAny().isPresent();
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
            if (e instanceof PlayerBetEvent) {
                PlayerBetEvent pbe = (PlayerBetEvent) e;
                assert p.getRoundBet() >= this.currentRoundBet;

                this.potBalance += pbe.getAmount();
                this.currentRoundBet = p.getRoundBet(); // set current bet equal to this bet amount
                PlayerActionGameEvent ge = new PlayerActionGameEvent(this);
                ge.setE(pbe);
                this.fireEvent(ge);

//              This player has action now.
                p.setCommandThisTurn(true);
                //TODO Temporary set check next round for game
                // if next round ready then next Player will be left person of dealer
                if (isNextRoundReady()) {
                    this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
                    autoNextRound();
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
                        this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
                        autoNextRound();
                    } else
                        this.setCurrentPlayer(this.getNextPlayer(p));
                }


            }
            if (e instanceof PlayerCheckEvent) {
                PlayerCheckEvent pce = (PlayerCheckEvent) e;
                PlayerActionGameEvent ge = new PlayerActionGameEvent(this);
                ge.setE(pce);
                this.fireEvent(ge);

//              This player has action now.
                p.setCommandThisTurn(true);
                if (isNextRoundReady()) {
                    this.setCurrentPlayer(this.getNextPlayer(this.getDealer()));
                    autoNextRound();
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
