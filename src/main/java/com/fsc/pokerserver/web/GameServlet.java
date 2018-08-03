package com.fsc.pokerserver.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.GameStatus;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;
import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import com.google.common.base.Joiner;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import static com.googlecode.objectify.ObjectifyService.ofy;

@WebServlet(name = "GameServlet", urlPatterns = { "/api/game" })

public class GameServlet extends HttpServlet {
	

	MqttServletGameServer server = MqttServletGameServer.getInstance();
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String method = request.getParameter("method");
		if("put".equalsIgnoreCase(method))
		{
			doPut(request,response);
			return;
		}
		else if("join".equalsIgnoreCase(method))
		{
			doPost(request,response);
			return;
		}
		else if("start".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			p.getCurrentGame().setDealer(p);
		
			int sizeOfListPlayer = p.getCurrentGame().getListPlayer().size();
//			response.getWriter().println("size of list players: "+sizeOfListPlayer);
			for(int i=0;i<sizeOfListPlayer;i++)
			{
				p.getCurrentGame().getListPlayer().get(i).setBalance(1000);
//				response.getWriter().println("Name number "+i+": "+p.getCurrentGame().getListPlayer().get(i).getName());;
			}
			
			p.getCurrentGame().startGame();
			
			
//			response.getWriter().println("Start Game Successful");
//			response.getWriter().println("Dealer: {Name: "+ p.getCurrentGame().getDealer().getName() + " ; Balance: "+p.getCurrentGame().getDealer().getBalance()+"}");
//			response.getWriter().println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
//			response.getWriter().println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
			
			System.out.println("Start Game Successful");
			System.out.println("Dealer: {Name: "+ p.getCurrentGame().getDealer().getName() + " ; Balance: "+p.getCurrentGame().getDealer().getBalance()+"}");
			System.out.println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
			System.out.println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
			
			
			return;
		}
		else if("preflop".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().preflop();
//			response.getWriter().println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
//			response.getWriter().println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
//			response.getWriter().println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
			
			System.out.println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
			System.out.println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
			System.out.println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
			
			return;
		}
		else if("bet".equalsIgnoreCase(method))
		{
			Player p = (Player) request.getAttribute("player");
			
			long betValue = Long.parseLong(request.getParameter("value"));
			p.bet(betValue);
//			response.getWriter().println("Player: "+p.getName()+"\n Bet value: "+betValue+" \n Balance of Current Player: "+p.getBalance());
			System.out.println("Player: "+p.getName()+"\n Bet value: "+betValue+" \n Balance of Current Player: "+p.getBalance());
		}
		else if("fold".equalsIgnoreCase(method))
		{
			Player p = (Player) request.getAttribute("player");
			
			p.fold();
//			response.getWriter().println("Player: "+p.getName()+"\n Bet value: "+betValue+" \n Balance of Current Player: "+p.getBalance());
			System.out.println("Player folded: "+p.getName());
		}
		else if("flop".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().flop();
//			response.getWriter().println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
//			response.getWriter().println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
//			response.getWriter().println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
//			response.getWriter().println("Total of cards: "+p.getCurrentGame().getBoard().getCardNumber());
		
			System.out.println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
			System.out.println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
			System.out.println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
			System.out.println("Total of cards: "+p.getCurrentGame().getBoard().getCardNumber());
			
			return;
		}
		else if("turn".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().turn();
//			response.getWriter().println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
//			response.getWriter().println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
//			response.getWriter().println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
//			response.getWriter().println("Total of cards: "+p.getCurrentGame().getBoard().getCardNumber());
		
			System.out.println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
			System.out.println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
			System.out.println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
			System.out.println("Total of cards: "+p.getCurrentGame().getBoard().getCardNumber());
			
			return;
		}
		else if("river".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().river();
//			response.getWriter().println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
//			response.getWriter().println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
//			response.getWriter().println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
//			response.getWriter().println("Total of cards: "+p.getCurrentGame().getBoard().getCardNumber());
		
			System.out.println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
			System.out.println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
			System.out.println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
			System.out.println("Total of cards: "+p.getCurrentGame().getBoard().getCardNumber());
			
			return;
		}
		else if("end".equalsIgnoreCase(method))
		{
			
			Player p = (Player) request.getAttribute("player");
			
			p.getCurrentGame().endGame();
//			response.getWriter().println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
//			response.getWriter().println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
//			response.getWriter().println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
//			response.getWriter().println("Total of cards: "+p.getCurrentGame().getBoard().getCardNumber());
		
			System.out.println("Small Blind: {Name: "+ p.getCurrentGame().getSmallBlind().getName() + " ; Balance: "+p.getCurrentGame().getSmallBlind().getBalance()+"}");
			System.out.println("Big Blind: {Name: "+ p.getCurrentGame().getBigBlind().getName() + " ; Balance: "+p.getCurrentGame().getBigBlind().getBalance()+"}");	
			System.out.println("Current Player: "+ p.getCurrentGame().getCurrentPlayer().getName());
			System.out.println("Total of cards: "+p.getCurrentGame().getBoard().getCardNumber());
			
			return;
		}
		else
		{
				String data = Joiner.on(",").join(this.server.getListRoom());
//				response.getWriter().println(data);	
				System.out.println(data);
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