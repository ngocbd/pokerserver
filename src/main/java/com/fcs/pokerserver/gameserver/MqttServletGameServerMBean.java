package com.fcs.pokerserver.gameserver;

import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

import java.util.List;

public interface MqttServletGameServerMBean {
    public String jmx_getRoomsList();
    public String jmx_getPlayerList();
}
