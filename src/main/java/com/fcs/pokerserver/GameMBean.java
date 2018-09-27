package com.fcs.pokerserver;

public interface GameMBean {
    public long jmx_getPotBalance();

    public void jmx_setPotBalance(long bal);

    public long jmx_getCurrentRoundBet();

    public void jmx_setCurrentRoundBet(long bal);

    public String jmx_getListPlayer();

    public void jmx_kickPlayer(String id);

    public String jmx_getBoard();

    public String jmx_getDealer();

    public String jmx_getSmallBlind();

    public String jmx_getBigBlind();

    public String jmx_setDealer(String id);
}
