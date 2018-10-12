package com.fsc.pokerserver.test;

import com.fcs.pokerserver.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;


public class Main {

    public static void main(String[] args) {
        Player p1 = new Player();
        p1.setRoundBet(1);
        Player p2 = new Player();
        p2.setRoundBet(2);

        Player p3 = new Player();
        p3.setRoundBet(3);
        Player p4 = new Player();
        p4.setRoundBet(4);
        List<Player> l = new ArrayList<>();
        l.add(p3);
        l.add(p1);
        l.add(p4);
        l.add(p2);

        List<Player> g = new ArrayList<>(l);
        Collections.sort(g, (x1, x2) -> {
            if (x1.getRoundBet() > x2.getRoundBet()) return 1;
            if (x1.getRoundBet() < x2.getRoundBet()) return -1;
            return 0;
        });
        for (Player p : g) {
            p.setRoundBet(0);
        }
        for (Player p : l) {
            System.out.println(p.getRoundBet());;
        }
    }


}
