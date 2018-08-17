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

import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;
import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import com.google.common.base.Joiner;

/**
 * The class to react of game server.
 * @category com > fcs > pokerserver > web
 * */
@WebServlet(name = "GameServlet", urlPatterns = { "/api/game" })
public class GameServlet extends HttpServlet {
	

	MqttServletGameServer server = MqttServletGameServer.getInstance();
	static Logger logger = Logger.getLogger(GameServlet.class.getName());
	
	@Override 
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{ 
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String method = request.getParameter("method");
		if("put".equalsIgnoreCase(method))
		{
			doPut(request,response);
			return;
		}//The player join the room
		else if("join".equalsIgnoreCase(method))
		{
			doPost(request,response);
			return;
		}//The player start the game
		else if("start".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			p.getCurrentGame().setDealer(p);
		
			int sizeOfListPlayer = p.getCurrentGame().getListPlayer().size();
			for(int i=0;i<sizeOfListPlayer;i++)
			{
				p.getCurrentGame().getListPlayer().get(i).setBalance(1000);
			}
			
			p.getCurrentGame().startGame();
			
			logger.log(Level.INFO, "Start Game\n\tDealer: "+ p.getCurrentGame().getDealer().getName()+"\n\tSmall Blind: " + p.getCurrentGame().getSmallBlind().getName()+"\n\tBig Blind: " + p.getCurrentGame().getBigBlind().getName());

			return;
		}//The preflop of the game
		else if("preflop".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().preflop();
			
			logger.log(Level.INFO, "The Preflop of the game\n\tNumber of the cards: "+p.getCurrentGame().getBoard().getCardNumber()+"\n\tSmallBlind's balance: " + p.getCurrentGame().getSmallBlind().getBalance()+"\n\tBigBlind's balance: " + p.getCurrentGame().getBigBlind().getBalance()+"\n\tCurrent Player: " + p.getCurrentGame().getCurrentPlayer().getName());
			
			return;
		}//The player bet
		else if("bet".equalsIgnoreCase(method))
		{
			Player p = (Player) request.getAttribute("player");
			
			long betValue = Long.parseLong(request.getParameter("value"));
			
			p.bet(betValue);
			
			logger.log(Level.INFO, "The Player's name : "+p.getName()+"\n\tBet value: "+betValue+"\n\tBalance of Current Player: "+p.getBalance());
			return;
		}//The player fold
		else if("fold".equalsIgnoreCase(method))
		{
			Player p = (Player) request.getAttribute("player");
			
			p.fold();
			logger.log(Level.INFO, "The Player folded: "+p.getName());
			
			return;
		}//The flop of the game
		else if("flop".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().flop();

			String strFlop = "The Flop of the game \n\tNumber of cards: "+ p.getCurrentGame().getBoard().getCardNumber() + "\n\tThe Cards in the flop: "+ p.getCurrentGame().getBoard().getFlopCards().toString();
			for(int i=0;i<p.getCurrentGame().getListPlayer().size();i++)
			{
				strFlop+="\n\tCards of Player "+(i+1)+": "+p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(0).toString()+" "+p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(1).toString();
				strFlop+="\n\tBalance of Player "+(i+1)+": "+p.getCurrentGame().getListPlayer().get(i).getBalance();
			}
			logger.log(Level.INFO, strFlop);
			
			return;
		}//The turn of the game
		else if("turn".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().turn();

			String strFlop = "The Turn of the game\n\tNumber of cards: "+ p.getCurrentGame().getBoard().getCardNumber() + "\n\tThe Cards in the flop: "+ p.getCurrentGame().getBoard().getFlopCards().toString()+"\n\tThe Cards in the Turn: "+p.getCurrentGame().getBoard().getTurnCard().toString();
			for(int i=0;i<p.getCurrentGame().getListPlayer().size();i++)
			{
				strFlop+="\n\tCards of Player "+(i+1)+": "+p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(0).toString()+" "+p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(1).toString();
				strFlop+="\n\tBalance of Player "+(i+1)+": "+p.getCurrentGame().getListPlayer().get(i).getBalance();
			}
			logger.log(Level.INFO, strFlop);
			
			return;
		}//The river of the game
		else if("river".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().river();
		
			String strFlop = "The River of the game\n\tNumber of cards: "+ p.getCurrentGame().getBoard().getCardNumber() + "\n\tThe Cards in the flop: "+ p.getCurrentGame().getBoard().getFlopCards().toString()+"\n\tThe Cards in the Turn: "+p.getCurrentGame().getBoard().getTurnCard().toString()+"\n\tThe Cards in the River: "+p.getCurrentGame().getBoard().getRiverCard().toString();
			for(int i=0;i<p.getCurrentGame().getListPlayer().size();i++)
			{
				strFlop+="\n\tCards of Player "+(i+1)+": "+p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(0).toString()+" "+p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(1).toString();
				strFlop+="\n\tBalance of Player "+(i+1)+": "+p.getCurrentGame().getListPlayer().get(i).getBalance();
			}
			logger.log(Level.INFO, strFlop);
			
			return;
		}//The end of the game
		else if("end".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().endGame();
			
			
		
			String strFlop = "The End of the game\n\tNumber of cards: "+ p.getCurrentGame().getBoard().getCardNumber() + "\n\tThe Cards in the flop: "+ p.getCurrentGame().getBoard().getFlopCards().toString()+"\n\tThe Cards in the Turn: "+p.getCurrentGame().getBoard().getTurnCard().toString()+"\n\tThe Cards in the River: "+p.getCurrentGame().getBoard().getRiverCard().toString();
			for(int i=0;i<p.getCurrentGame().getListPlayer().size();i++)
			{
				strFlop+="\n\tCards of Player "+(i+1)+": "+p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(0).toString()+" "+p.getCurrentGame().getListPlayer().get(i).getPlayerHand().getCard(1).toString();
				strFlop+="\n\tBalance of Player "+(i+1)+": "+p.getCurrentGame().getListPlayer().get(i).getBalance();
			}
			strFlop += "\n\tThe winner: "+ p.getCurrentGame().getWinner()+"\n\tThe rank: "+p.getCurrentGame().getRank()+"\n\tThe best hand:" + p.getCurrentGame().getBestHand();
			logger.log(Level.INFO, strFlop);
			
			return;
		}
		else
		{
				String data = Joiner.on(",").join(this.server.getListRoom());
				logger.log(Level.INFO, data);
		}
	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Player p = (Player) request.getAttribute("player");
		
		String id = request.getParameter("id");
		
		Room room = server.getRoomByID(Long.parseLong(id));
		
		checkNotNull(room,"Room "+id+" not found");
		
		room.addPlayer(p);
		
		String data = Joiner.on(",").join(this.server.getListPlayer());
		
		response.getWriter().println(data);
		
	}
	
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Player p = (Player) request.getAttribute("player");
		
		Room room = p.getCurrentRoom();
		
		checkNotNull(room,"Room  not found");
		
		room.createNewGame();
		
		response.getWriter().println(room.getCurrentGame().getId()); 
	}
}