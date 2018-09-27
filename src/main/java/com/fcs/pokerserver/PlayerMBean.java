package com.fcs.pokerserver;

public interface PlayerMBean {
    public long jmx_getBalance();

    public void jmx_setBalance(long bal);

    public long jmx_getGlobalBalance();

    public void jmx_setGlobalBalance(long bal);

    public void jmx_setSittingOut(boolean bool);

    public void jmx_setRoundBet(long amount);

    public void jmx_setGameBet(long amount);
}
