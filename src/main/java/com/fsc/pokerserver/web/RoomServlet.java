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

import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;
import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import com.google.common.base.Joiner;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The class to the Player react with the room
 *
 * @category com > fcs > pokerserver > web
 */
@WebServlet(name = "RoomServlet", urlPatterns = {"/api/room"})
public class RoomServlet extends HttpServlet {
    private MqttServletGameServer server = MqttServletGameServer.getInstance();

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
        String method = request.getParameter("method");
        if ("put".equalsIgnoreCase(method)) {
            doPut(request, response);
            return;
        } else if ("join".equalsIgnoreCase(method)) {
            doPost(request, response);
            return;
        } else {
            //TODO return more data ex Blind level , number of player , game status ...
//			List All room
            String data = Joiner.on(",").join(this.server.getListRoom());
            response.getWriter().println(data);

        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Player p = (Player) request.getAttribute("player");

        String id = request.getParameter("id");

        Room room = server.getRoomByID(Long.parseLong(id));

        checkNotNull(room, "Room " + id + " not found");

//		System.out.println("player name: "+p.getName());
//		System.out.println("Room Id: "+room.getRoomID());


        room.addPlayer(p);

        //TODO return more data ex Blind level ,  player balance  , game status ...
        String data = Joiner.on(",").join(room.getListPlayer());

        response.getWriter().println(data);
//		for (int i = 0; i < room.getListPlayer().size(); i++) {
//			System.out.println("Player Name in Room: "+room.getListPlayer().get(i).getName());
//		}
//		System.out.println("Player in room: "+room.getListPlayer().size());
    }
/**
 * Room is created adhere.
 * **/
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Player p = (Player) request.getAttribute("player");

        checkNotNull(p, "Player not found");

        Room room = new Room(p, BlindLevel.BLIND_10_20);


        server.addRoom(room);
        response.getWriter().println(room.getRoomID());

    }
}