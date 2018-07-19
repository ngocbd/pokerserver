package com.fsc.pokerserver.web;


import java.io.IOException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.gameserver.MqttServletGameServer;

import static com.google.common.base.Preconditions.checkArgument;




import static com.googlecode.objectify.ObjectifyService.ofy;



@WebServlet(
    name = "LoginServlet",
    urlPatterns = {"/api/login"}
)


public class LoginServlet extends HttpServlet {
	String secret = "thisstringisverysecret";
	Algorithm algorithm = Algorithm.HMAC256(secret);
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
      
	  
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    
    String username = request.getParameter("username");
   
    checkArgument(username!=null, "username can't not be null");
    
    String password = request.getParameter("password");
    
    checkArgument(password!=null, "password can't not be null"); 
    
    
    User user = ofy().load().type(User.class).id(username).safe();
    checkArgument( password.equals(user.getPassword()) , "Incorrect Password");
    
    Player p = new Player(username);
    MqttServletGameServer.getInstance().addPlayer(p);
    String token = JWT.create()
	        .withIssuer("pokerserver")
	        .withJWTId(username)
	        .sign(algorithm);
	    
    p.setToken(token);
    response.getWriter().println(token);
    
    
  }
}