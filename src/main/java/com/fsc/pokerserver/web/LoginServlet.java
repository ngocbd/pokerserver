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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.gameserver.MqttServletGameServer;

import static com.google.common.base.Preconditions.checkArgument;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * The class to Player login
 *
 * @category com > fcs > pokerserver > web
 */
@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/api/login"}
)
public class LoginServlet extends HttpServlet {
    private String secret = "thisstringisverysecret";
    private Algorithm algorithm = Algorithm.HMAC256(secret);

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doOptions(request, response);
        MqttServletGameServer server = MqttServletGameServer.getInstance();
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        String endpoints[] = request.getRequestURI().split("/");
        String endpoint = endpoints[endpoints.length - 1];
        switch (endpoint) {
            case "login":
                goLogin(request, response, server);
                break;
            case "logout":
                goLogout(request, response, server);
                break;
            default:
                response.setStatus(400);
                response.getWriter().println("{\"msg\":\"URL is not well-form\"");
                return;
        }

    }

    /**
     * Logout need token
     */
    private void goLogout(HttpServletRequest request, HttpServletResponse response, MqttServletGameServer server) throws IOException {
        Player p = (Player) request.getAttribute("player");


    }

    private void goLogin(HttpServletRequest request, HttpServletResponse response, MqttServletGameServer server) throws IOException {
        String username = request.getParameter("username");

        checkArgument(username != null, "username can't not be null");

        String password = request.getParameter("password");

        checkArgument(password != null, "password can't not be null");


        Player exist = server.getListPlayer().stream().filter(p -> p.getName().equals(username)).findAny().orElse(null);

        checkArgument(exist == null, "User logged-in ready !!!!");

        User user = ofy().load().type(User.class).id(username).safe();
        checkArgument(password.equals(user.getPassword()), "Incorrect Password");

        Player p = new Player(username);
        /**
         * Set Balance + set Avatar
         * */
        p.setGlobalBalance(user.getBalance());
        p.setAvatar_url(user.getAvatar_url());
        server.addPlayer(p);
        String token = JWT.create()
                .withIssuer("pokerserver")
                .withJWTId(username)
                .sign(algorithm);

        p.setToken(token);
        response.getWriter().println(token);
    }
}