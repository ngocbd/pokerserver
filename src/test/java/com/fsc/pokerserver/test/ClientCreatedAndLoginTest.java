package com.fsc.pokerserver.test;

import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ClientCreatedAndLoginTest {

    private String host = "http://localhost:8080/";
    private ArrayList token[];
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

    @Test
    public void testCreatePlayer() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/register?username=" + arr[i] + "&password=123456";
            assertEquals(200, this.getStatusCodeFromUrl(url));
        }

    }

    @Test
    public void TestLogin() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/login?username=" + arr[i] + "&password=123456";
//            Document tokenDoc = Jsoup.connect(url).get();
//            token[i] = tokenDoc.body().text();
            assertEquals(200, this.getStatusCodeFromUrl(url));
        }
    }


    public void deleteUser() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/deluser?user=" + arr[i];
            assertEquals(200, this.getStatusCodeFromUrl(url));
        }
    }

}
