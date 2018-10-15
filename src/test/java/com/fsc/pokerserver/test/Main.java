package com.fsc.pokerserver.test;

import com.fcs.pokerserver.Player;

import java.util.*;


public class Main {

    public static void main(String[] args) {
        ArrayList<Player> l = new ArrayList<>();
        Collections.sort(l, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getBalance() > o2.getBalance()) return 1;
                if (o1.getBalance() == o2.getBalance()) return 0;
                return -1;
            }
        });

    }


}
