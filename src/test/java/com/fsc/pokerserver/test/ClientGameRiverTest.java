package com.fsc.pokerserver.test;

import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@Ignore
public class ClientGameRiverTest {
    private String host = "http://localhost:8080/";
    private String arr[] = {"toan2", "danh2", "linh2", "chau2", "nghe2"};
    private MqttServletGameServer mqttServletGameServer = MqttServletGameServer.getInstance();

    @Before
    public void setUp() throws Exception {
        Thread.sleep(2000);
        this.deleteUser();
    }

    public String[] getTokenPlayer() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/register?username=" + arr[i] + "&password=123456";
            Helper.getStatusCodeFromUrl(url);
        }
        String token[] = {"", "", "", "", ""};
        for (int i = 0; i < arr.length; i++) {
            Document tokenDoc = Jsoup.connect(host + "api/login?username=" + arr[i] + "&password=123456").get();
            token[i] = tokenDoc.body().text();
        }
        return token;
    }

    @Test
    public void gameRiver() throws IOException {
        String[] token = this.getTokenPlayer();
        //create room
        String urlCreateRoom = host + "api/room?token=" + token[0] + "&method=put";
        String roomId = Jsoup.connect(urlCreateRoom).get().body().text();
        //join room
        for (int i = 1; i < token.length; i++) {
            System.out.println("token at " + i + ": " + token[i]);
            String url_1 = host + "api/room?token=" + token[i] + "&method=join&id=" + roomId;
            Helper.getStatusCodeFromUrl(url_1);
        }
        //startgame
        String startGame = host + "api/game?token=" + token[0] + "&method=start";
        Helper.getStatusCodeFromUrl(startGame);

        //Bet
        String utg = host + "api/game?token=" + token[3] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(utg);
        String ngheBet = host + "api/game?token=" + token[4] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(ngheBet);
        String toanBet = host + "api/game?token=" + token[0] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(toanBet);
        String danhBet = host + "api/game?token=" + token[1] + "&method=bet&value=10";
        Helper.getStatusCodeFromUrl(danhBet);
        String bbLinhBet = host + "api/game?token=" + token[2] + "&method=check";
        Helper.getStatusCodeFromUrl(bbLinhBet);

        //Bet and Fold
        String sbDanhBet1 = host + "api/game?token=" + token[1] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(sbDanhBet1);
        String bbLinhBet1 = host + "api/game?token=" + token[2] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(bbLinhBet1);
        String utgChauBet1 = host + "api/game?token=" + token[3] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(utgChauBet1);
        String ngheBet1 = host + "api/game?token=" + token[4] + "&method=fold";
        Helper.getStatusCodeFromUrl(ngheBet1);
        String dealerToanBet1 = host + "api/game?token=" + token[0] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(dealerToanBet1);



        //Bet and Fold
        String sbDanhBet3 = host + "api/game?token=" + token[1] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(sbDanhBet3);
        String bbLinhBet3 = host + "api/game?token=" + token[2] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(bbLinhBet3);
        String utgChauBet3 = host + "api/game?token=" + token[3] + "&method=bet&value=20";
        Helper.getStatusCodeFromUrl(utgChauBet3);
        String dealerToanBet3 = host + "api/game?token=" + token[0] + "&method=bet&value=20";
//        this.getStatusCodeFromUrl(dealerToanBet3);

        assertEquals(200, Helper.getStatusCodeFromUrl(dealerToanBet3));
    }


    public void deleteUser() throws IOException {
        for (int i = 0; i < arr.length; i++) {
            String url = host + "api/deluser?user=" + arr[i];
            assertEquals(200, Helper.getStatusCodeFromUrl(url));
        }
    }

}
