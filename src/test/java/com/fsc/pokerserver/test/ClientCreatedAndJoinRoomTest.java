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

public class ClientCreatedAndJoinRoomTest implements MqttCallback {
    private String host = "http://localhost:8080/";
    private String token[];
    private String arr[] = {"toan2", "danh2", "linh2", "chau2", "nghe2"};
    MqttServletGameServer mqttServletGameServer = MqttServletGameServer.getInstance();

    @Before
    public void setUp() throws Exception {
        Thread.sleep(2000);
    }

    public int getStatusCodeFromUrl(String url) throws IOException {
        return Jsoup.connect(url).method(Connection.Method.GET).execute().statusCode();

    }

    public String[] getTokenPlayer() throws  IOException {
        for (int i = 0; i < arr.length; i++) {
            Document tokenDoc = Jsoup.connect(host + "api/login?username=" + arr[i] + "&password=123456").get();
            token[i] = tokenDoc.body().text();
        }
        return token;
    }
    @Test
    public void createdRoom() throws IOException {
        // Players login and return Array Token of Players
        token = this.getTokenPlayer();

        //create room
        String url = host + "api/room?token=" + token[0] + "&method=put";
        assertEquals(200, this.getStatusCodeFromUrl(url));
    }


    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
