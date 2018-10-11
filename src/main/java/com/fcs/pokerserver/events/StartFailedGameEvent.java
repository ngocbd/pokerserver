package com.fcs.pokerserver.events;

import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.Player;

import java.util.List;

public class StartFailedGameEvent extends AbstractGameEvent {
    private List<Player> listPlayers;
    private String msg;

    public StartFailedGameEvent(Game src) {
        super(src);
    }

    public StartFailedGameEvent(Game src, GameAction type) {
        super(src, type);
    }

    public List<Player> getListPlayers() {
        return listPlayers;
    }

    public void setListPlayers(List<Player> listPlayers) {
        this.listPlayers = listPlayers;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
