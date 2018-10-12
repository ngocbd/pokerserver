package com.fsc.pokerserver.test;

import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClientLoginTest {

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

    @Test
    public void testLogin(){
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/register?username=" + arr[i] + "&password=123456";
            try {
                this.getStatusCodeFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < arr.length; j++) {
                String url_1 = host + "api/login?username=" + arr[i] + "&password=123456";
                try {
                    assertEquals(200, this.getStatusCodeFromUrl(url_1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteUser() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/deluser?user=" + arr[i];
            assertEquals(200, this.getStatusCodeFromUrl(url));
        }
    }

}
