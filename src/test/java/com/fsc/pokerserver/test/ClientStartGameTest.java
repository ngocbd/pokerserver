package com.fsc.pokerserver.test;

import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClientStartGameTest {

    private String host = "http://localhost:8080/";
    private String arr[] = {"toan2", "danh2", "linh2", "chau2", "nghe2"};
    MqttServletGameServer mqttServletGameServer = MqttServletGameServer.getInstance();

    @Before
    public void setUp() throws Exception {
        Thread.sleep(2000);
        this.deleteUser();
    }

    private int getStatusCodeFromUrl(String url) throws IOException {
        return Jsoup.connect(url).method(Connection.Method.GET).execute().statusCode();

    }

    public String[] getTokenPlayer() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/register?username=" + arr[i] + "&password=123456";
            this.getStatusCodeFromUrl(url);
        }
        String token[] = {"", "", "", "", ""};
        for (int i = 0; i < arr.length; i++) {
            Document tokenDoc = Jsoup.connect(host + "api/login?username=" + arr[i] + "&password=123456").get();
            token[i] = tokenDoc.body().text();
        }
        return token;
    }

    @Test
    public void startGame() throws IOException,Exception {
        String[] token = this.getTokenPlayer();
        //create room
        String url = host + "api/room?token=" + token[0] + "&method=put";
        assertEquals(200,this.getStatusCodeFromUrl(url));

        String urlCreateRoom = host + "api/room?token=" + token[0] + "&method=put";
        String roomId = Jsoup.connect(urlCreateRoom).get().body().text();
        //join room
        for (int i = 1; i < token.length; i++) {
            System.out.println("token at " + i + ": " + token[i]);
            String url_1 = host + "api/room?token=" + token[i] + "&method=join&id=" + roomId;
            this.getStatusCodeFromUrl(url_1);
        }
            Thread.sleep(3000);
        //startgame
        String startGame = host + "api/game?token=" + token[0] + "&method=start";
        assertEquals(200, this.getStatusCodeFromUrl(startGame));

    }


    public void deleteUser() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/deluser?user=" + arr[i];
            assertEquals(200, this.getStatusCodeFromUrl(url));
        }
    }

}
