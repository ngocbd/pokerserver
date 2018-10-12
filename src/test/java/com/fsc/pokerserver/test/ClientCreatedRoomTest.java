package com.fsc.pokerserver.test;

import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClientCreatedRoomTest {
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
        String token[]={"","","","",""};
        for (int i = 0; i < arr.length; i++) {
            Document tokenDoc = Jsoup.connect(host + "api/login?username=" + arr[i] + "&password=123456").get();
            token[i] = tokenDoc.body().text();
        }
        return token;
    }

    @Test
    public void createdRoom() throws IOException {
        // Players login and return Array Token of Players
        String[] token = this.getTokenPlayer();
        //create room
        String url = host + "api/room?token=" + token[0] + "&method=put";
        assertEquals(200, this.getStatusCodeFromUrl(url));
    }


    public void deleteUser() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/deluser?user=" + arr[i];
            assertEquals(200, this.getStatusCodeFromUrl(url));
        }
    }

}
