package com.fsc.pokerserver.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.googlecode.objectify.ObjectifyService.ofy;

import static com.google.common.base.Preconditions.checkArgument;

public class DeleteUserServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        String userName = req.getParameter("user");
        checkArgument(userName != null, "username can't not be null");

        User user = ofy().load().type(User.class).id(userName).now();
        if (user == null) {
            System.out.println("Account does not exist");
            return;
        }
        ofy().delete().entity(user).now();

        resp.getWriter().println("success");
    }
}
