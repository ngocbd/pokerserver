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


import com.googlecode.objectify.ObjectifyService;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The class to the Player sign up
 * @category com > fcs > pokerserver > web
 * */
@WebServlet(
    name = "RegisterServlet",
    urlPatterns = {"/api/register"}
)
public class RegisterServlet extends HttpServlet {

	@Override 
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{ 
		// TODO Auto-generated method stub 
		resp.setHeader("Access-Control-Allow-Origin", "*"); 
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST"); 
		resp.setHeader("Access-Control-Allow-Headers", "Content-Type, authorization"); 
		resp.setHeader("Access-Control-Max-Age", "86400"); 
		resp.setHeader("Cache-Control", "public, max-age=90000"); 
		// Tell the browser what requests we allow. 
		resp.setHeader("Allow", "GET, HEAD, POST, PUT, TRACE, OPTIONS"); 
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException,ServletException {
      doOptions(request,response);
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    
    String username = request.getParameter("username");
   
    checkNotNull(username, "username can't not be null");
    checkArgument(username.length()>3, "username must more than 3");
    
    String password = request.getParameter("password");
    
    checkNotNull(password, "password can't not be null"); 
    checkArgument(password.length()>5, "password must more than 5");
    
    User existUser = ofy().load().type(User.class).id(username).now();
    
    checkArgument( existUser==null , "User "+username+" exists ");
    
    User user  = new User();
    user.setUsername(username);
    user.setPassword(password);

    ofy().save().entity(user);

    response.getWriter().printf("%s",username);
  }
}