package com.fsc.pokerserver.web;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fcs.pokerserver.GameServer;
import com.fcs.pokerserver.Player;
import com.googlecode.objectify.ObjectifyFilter;



public class PokerTokenFilter implements Filter {
	String secret = "thisstringisverysecret";
	Algorithm algorithm = Algorithm.HMAC256(secret);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		String token = request.getParameter("token");

		checkArgument(token != null, "token can't not be null");

		JWTVerifier verifier = JWT.require(algorithm).withIssuer("pokerserver").build(); // Reusable verifier instance
		DecodedJWT jwt = verifier.verify(token);

		String username = jwt.getId();

		GameServer server = GameServer.getInstance();
		
		Player p = server.getPlayerByName(username);
		
		checkNotNull(p,"Player not login or not found");
		
		request.setAttribute("player", p);
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}