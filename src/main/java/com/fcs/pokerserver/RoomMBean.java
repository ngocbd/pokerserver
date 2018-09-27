package com.fcs.pokerserver;

public interface RoomMBean {
    public String jmx_getRoomPlayerID();

    public void jmx_kickPlayer(String id);

    public String jmx_getMasterID();

    public void jmx_setMaster(String id);
}
