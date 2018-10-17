package com.fcs.pokerserver.events;

public interface AbstractPlayerListener {
    /**
     * Perform any actions that occured in-game
     */
    public void actionPerformed(AbstractPlayerEvent e);

}
