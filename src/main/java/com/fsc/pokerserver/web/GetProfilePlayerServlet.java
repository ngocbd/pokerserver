package com.fsc.pokerserver.web;

import com.fcs.pokerserver.Player;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class GetProfilePlayerServlet extends HttpServlet {

//    static {
//        ObjectifyService.register(Player.class);
//    }

    static private Logger logger = Logger.getLogger(GetProfilePlayerServlet.class.getSimpleName());

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doOptions(req, resp);

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String userName = req.getParameter("username");
        checkArgument(userName != null, "username can't not be null");
        Player player = ofy().load().type(Player.class).id(userName).now();
        if (player == null) {
            logger.warning("player does not exist");
            resp.getWriter().println(gson.toJson("player does not exist"));
            return;
        }

        resp.getWriter().println(gson.toJson(player));
    }
}
