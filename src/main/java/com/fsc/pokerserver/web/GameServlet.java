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

package com.fsc.pokerserver.web;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;
import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import com.google.common.base.Joiner;

/**
 * The class to react of game server.
 *
 * @category com > fcs > pokerserver > web
 */
@WebServlet(name = "GameServlet", urlPatterns = {"/api/game"})
public class GameServlet extends HttpServlet {


    public MqttServletGameServer server = MqttServletGameServer.getInstance();
    private static Logger logger = Logger.getLogger(GameServlet.class.getName());

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub.
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, authorization");
        resp.setHeader("Access-Control-Max-Age", "86400");
        resp.setHeader("Cache-Control", "public, max-age=90000");
        // Tell the browser what requests we allow.
        resp.setHeader("Allow", "GET, HEAD, POST, PUT, TRACE, OPTIONS");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doOptions(request, response);
        String method = request.getParameter("method");
        Player p = null;
        switch (method) {
            case "put":
                doPut(request, response);
                break;
            case "join":
                doPost(request, response);
                break;
            case "start":
                p = (Player) request.getAttribute("player");
                p.getCurrentGame().setDealer(p);
                int sizeOfListPlayer = p.getCurrentGame().getListPlayer().size();
                if (sizeOfListPlayer < 2) {
                    System.out.println("Cannot start game due to not enough players");
                    response.setStatus(403);
                    response.getWriter().println("{\"error\":\"Not enough player\"}");
                    return;
                }
                p.getCurrentGame().startGame();
                logger.log(Level.INFO, "Start Game\n\tDealer: " + p.getCurrentGame().getDealer().getName() + "\n\tSmall Blind: " + p.getCurrentGame().getSmallBlind().getName() + "\n\tBig Blind: " + p.getCurrentGame().getBigBlind().getName());
                break;
            case "preflop":
                p = (Player) request.getAttribute("player");
                p.getCurrentGame().preflop();
                logger.log(Level.INFO, "The Preflop of the game\n\tNumber of the cards: " + p.getCurrentGame().getBoard().getCardNumber() + "\n\tSmallBlind's balance: " + p.getCurrentGame().getSmallBlind().getBalance() + "\n\tBigBlind's balance: " + p.getCurrentGame().getBigBlind().getBalance() + "\n\tCurrent Player: " + p.getCurrentGame().getCurrentPlayer().getName());
                break;
            case "bet":
                p = (Player) request.getAttribute("player");
                long betValue = Long.parseLong(request.getParameter("value"));
                p.bet(betValue);
                logger.log(Level.INFO, "The Player's name : " + p.getName() + "\n\tBet value: " + betValue + "\n\tBalance of Current Player: " + p.getBalance());
                break;
            case "check":
                p = (Player) request.getAttribute("player");
                if (!p.check()) {
                    logger.log(Level.WARNING, "The player cannot check: " + p.getName());
                    response.setStatus(403);
                    response.getWriter().println("{\"error\":\"You need to bet or call first!\"}");
                    break;
                }
                logger.log(Level.INFO, "The Player checked: " + p.getName());
                break;
            case "fold":
                p = (Player) request.getAttribute("player");
                p.fold();
                logger.log(Level.INFO, "The Player folded: " + p.getName());
                break;
            case "flop":
                p = (Player) request.getAttribute("player");
                p.getCurrentGame().flop();
                String strFlop = "The Flop of the game \n\tNumber of cards: " + p.getCurrentGame().getBoard().getCardNumber() + "\n\tThe Cards in the flop: " + p.getCurrentGame().getBoard().getFlopCards().toString();
                for (int i = 0; i < p.getCurrentGame().getListPlayer().size(); i++) {
                    strFlop += "\n\tCards of Player " + (i + 1) + ": " + p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(0).toString() + " " + p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(1).toString();
                    strFlop += "\n\tBalance of Player " + (i + 1) + ": " + p.getCurrentGame().getListPlayer().get(i).getBalance();
                }
                logger.log(Level.INFO, strFlop);
                break;
            case "turn":
                p = (Player) request.getAttribute("player");
                p.getCurrentGame().turn();
                String strTurn = "The Turn of the game\n\tNumber of cards: " + p.getCurrentGame().getBoard().getCardNumber() + "\n\tThe Cards in the flop: " + p.getCurrentGame().getBoard().getFlopCards().toString() + "\n\tThe Cards in the Turn: " + p.getCurrentGame().getBoard().getTurnCard().toString();
                for (int i = 0; i < p.getCurrentGame().getListPlayer().size(); i++) {
                    strTurn += "\n\tCards of Player " + (i + 1) + ": " + p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(0).toString() + " " + p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(1).toString();
                    strTurn += "\n\tBalance of Player " + (i + 1) + ": " + p.getCurrentGame().getListPlayer().get(i).getBalance();
                }
                logger.log(Level.INFO, strTurn);
                break;
            case "river":
                p = (Player) request.getAttribute("player");
                p.getCurrentGame().river();
                String strRiver = "The River of the game\n\tNumber of cards: " + p.getCurrentGame().getBoard().getCardNumber() + "\n\tThe Cards in the flop: " + p.getCurrentGame().getBoard().getFlopCards().toString() + "\n\tThe Cards in the Turn: " + p.getCurrentGame().getBoard().getTurnCard().toString() + "\n\tThe Cards in the River: " + p.getCurrentGame().getBoard().getRiverCard().toString();
                for (int i = 0; i < p.getCurrentGame().getListPlayer().size(); i++) {
                    strRiver += "\n\tCards of Player " + (i + 1) + ": " + p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(0).toString() + " " + p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(1).toString();
                    strRiver += "\n\tBalance of Player " + (i + 1) + ": " + p.getCurrentGame().getListPlayer().get(i).getBalance();
                }
                logger.log(Level.INFO, strRiver);
                break;
            case "end":
                p = (Player) request.getAttribute("player");
                p.getCurrentGame().endGame();
                String strEnd = "The End of the game\n\tNumber of cards: " + p.getCurrentGame().getBoard().getCardNumber() + "\n\tThe Cards in the flop: " + p.getCurrentGame().getBoard().getFlopCards().toString() + "\n\tThe Cards in the Turn: " + p.getCurrentGame().getBoard().getTurnCard().toString() + "\n\tThe Cards in the River: " + p.getCurrentGame().getBoard().getRiverCard().toString();
                for (int i = 0; i < p.getCurrentGame().getListPlayer().size(); i++) {
                    strEnd += "\n\tCards of Player " + (i + 1) + ": " + p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(0).toString() + " " + p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(1).toString();
                    strEnd += "\n\tBalance of Player " + (i + 1) + ": " + p.getCurrentGame().getListPlayer().get(i).getBalance();
                }
                strEnd += "\n\tThe winner: " + p.getCurrentGame().getWinners() + "\n\tThe rank: " + p.getCurrentGame().getRank() + "\n\tThe best hand:" + p.getCurrentGame().getBestHands();
                logger.log(Level.INFO, strEnd);
                break;
            case "roundcheck":
                p = (Player) request.getAttribute("player");
                response.setContentType("application/json");
                response.getWriter().println("{\"result\":" + p.getCurrentGame().isNextRoundReady() + "}");
                break;
            case "gamestatus":
                getGameStatus(request, response);
                break;
            default:
                String data = Joiner.on(",").join(this.server.getListRoom());
                logger.log(Level.INFO, data);
                return;
        }
    }

    private void getGameStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Room room = server.getRoomByID(Long.parseLong(id));
        checkNotNull(room, "Room " + id + " not found");
        Game game = room.getCurrentGame();
        checkNotNull(game, "Game has not created yet.");
        response.getWriter().println(game.toString());

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Player p = (Player) request.getAttribute("player");

        String id = request.getParameter("id");

        Room room = server.getRoomByID(Long.parseLong(id));

        checkNotNull(room, "Room " + id + " not found");

        room.addPlayer(p);

        String data = Joiner.on(",").join(this.server.getListPlayer());

        response.getWriter().println(data);

    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Player p = (Player) request.getAttribute("player");

        Room room = p.getCurrentRoom();

        checkNotNull(room, "Room not found");

        room.createNewGame();

        response.getWriter().println(room.getCurrentGame().getId());
    }
}