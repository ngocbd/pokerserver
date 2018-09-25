package com.fsc.pokerserver.web;

import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

@WebServlet(
        name = "GetProfilePlayerServlet",
        urlPatterns = {"/api/register"}
)

public class GetProfilePlayerServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(GetProfilePlayerServlet.class.getSimpleName());
    MqttServletGameServer server = MqttServletGameServer.getInstance();


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
        String name = req.getParameter("name");
        Player player = server.getPlayerByName(name);
        if (player == null) {
            logger.warning("Player does not exist or has not login!");
            resp.getWriter().println("Player does not exist or has not login!");
            return;
        }
        resp.getWriter().println(player.toJson());
    }
}
