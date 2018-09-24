package com.fsc.pokerserver.test;

import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClientGameFlopTest {
    private String host = "http://localhost:8080/";
    private String arr[] = {"toan2", "danh2", "linh2", "chau2", "nghe2"};
    MqttServletGameServer mqttServletGameServer = MqttServletGameServer.getInstance();

    @Before
    public void setUp() throws Exception {
        Thread.sleep(2000);
        this.deleteUser();
    }

    public int getStatusCodeFromUrl(String url) throws IOException {
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
    public void gameFlop() throws IOException {
        String[] token = this.getTokenPlayer();
        //create room
        String urlCreateRoom = host + "api/room?token=" + token[0] + "&method=put";
        String roomId = Jsoup.connect(urlCreateRoom).get().body().text();
        //join room
        for (int i = 1; i < token.length; i++) {
            String url_1 = host + "api/room?token=" + token[i] + "&method=join&id=" + roomId;
            this.getStatusCodeFromUrl(url_1);
        }
        //startgame
        String startGame = host + "api/game?token=" + token[0] + "&method=start";
        this.getStatusCodeFromUrl(startGame);
        //preflop
        String preFlop = host + "api/game?token=" + token[0] + "&method=preflop";
        this.getStatusCodeFromUrl(preFlop);
        //Bet
        String utg = host + "api/game?token=" + token[3] + "&method=bet&value=20";
        this.getStatusCodeFromUrl(utg);
        String ngheBet = host + "api/game?token=" + token[4] + "&method=bet&value=20";
        this.getStatusCodeFromUrl(ngheBet);
        String toanBet = host + "api/game?token=" + token[0] + "&method=bet&value=20";
        this.getStatusCodeFromUrl(toanBet);
        String danhBet = host + "api/game?token=" + token[1] + "&method=bet&value=10";
        this.getStatusCodeFromUrl(danhBet);

        //flop
        String flop = host + "api/game?token=" + token[0] + "&method=flop";
        assertEquals(200, this.getStatusCodeFromUrl(flop));
    }

    @Test
    public void deleteUser() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/deluser?user=" + arr[i];
            assertEquals(200, this.getStatusCodeFromUrl(url));
        }
    }

}
