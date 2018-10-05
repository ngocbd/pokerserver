
/*
The MIT License (MIT)
Copyright (c) 2018 by habogay
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.fcs.pokerserver.gameserver;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.servlet.DispatcherType;


import com.fcs.pokerserver.Game;
import com.fcs.pokerserver.events.*;

import com.fsc.pokerserver.web.*;
import com.google.common.base.Preconditions;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.joda.time.DateTime;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.googlecode.objectify.ObjectifyService.begin;
import static com.googlecode.objectify.ObjectifyService.ofy;
import static com.google.common.base.Preconditions.checkArgument;


/**
 * The class is tested. It's not a production.
 *
 * @category com > fcs > pokerserver > gameserver
 */
public class MqttServletGameServer implements MqttCallback, RoomListener, MqttServletGameServerMBean {
    private static MqttServletGameServer instance = null;
    private List<Player> listPlayer = new ArrayList<Player>();
    private List<Room> listRoom = new ArrayList<Room>();

    private static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
    private static final String SERVER_TOPIC = "/pokerserver/server";
    private Sender sender;
    private static Logger logger = Logger.getLogger(MqttServletGameServer.class.getName());


    static {

        final InputStream inputStream = MqttServletGameServer.class.getResourceAsStream("logging.properties");

        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final Exception e) {
            Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            Logger.getAnonymousLogger().severe(e.getMessage());
        }
    }

    private MqttServletGameServer() throws Exception {
        ServletHolder loginServlet = new ServletHolder(LoginServlet.class);
        ServletHolder registerServlet = new ServletHolder(RegisterServlet.class);
        ServletHolder roomServlet = new ServletHolder(RoomServlet.class);
        ServletHolder gameServlet = new ServletHolder(GameServlet.class);
        ServletHolder deleteUserServlet = new ServletHolder(DeleteUserServlet.class);
        ServletHolder getProfile = new ServletHolder(GetProfilePlayerServlet.class);

        Server server = new Server(8080);
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        LocateRegistry.createRegistry(1234);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbs.registerMBean(this, new ObjectName("com.fcs.pokerserver.gameserver:MqttServletGameServer=MqttServletGameServer"));
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://0.0.0.0:1234/jmxrmi");
        JMXConnectorServer svr = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);

        svr.start();
        server.addEventListener(mbContainer);
        server.addBean(mbContainer);
        // Add loggers MBean to server (will be picked up by MBeanContainer above)
        server.addBean(logger);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);

        NCSARequestLog requestLog = new NCSARequestLog();
        String logFileName = System.getProperty("user.home") + "/pokerserver-logs-yyyy_mm_dd.request.log";
        logger.fine("Request log:" + logFileName);

        requestLog.setFilename(logFileName);
        requestLog.setFilenameDateFormat("yyyy_MM_dd");
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(true);
        requestLog.setLogCookies(false);
        requestLog.setLogTimeZone("GMT");
        server.setRequestLog(requestLog);
        context.addFilter(ObjectifyWebFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        context.addFilter(PokerTokenFilter.class, "/api/room", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(PokerTokenFilter.class, "/api/game", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(PokerTokenFilter.class, "/api/profile", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(PokerTokenFilter.class, "/api/logout", EnumSet.of(DispatcherType.REQUEST));

        context.addServlet(loginServlet, "/api/login");
        context.addServlet(loginServlet, "/api/logout");
        context.addServlet(registerServlet, "/api/register");
        context.addServlet(roomServlet, "/api/room");
        context.addServlet(gameServlet, "/api/game");
        context.addServlet(deleteUserServlet, "/api/deluser");
        context.addServlet(getProfile, "/api/profile");

        logger.warning("MqttServletGameServer starting..." + ManagementFactory.getRuntimeMXBean().getName());
        try {
            server.start();
            try {
                this.run();
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            logger.warning("MqttServletGameServer started at " + DateTime.now().toLocalDateTime().toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Singleton pattern to get the MqttServletGameServer instance.
     *
     * @return MqttServletGameServer instance
     */
    public static MqttServletGameServer getInstance() {
        if (instance == null) {
            try {
                instance = new MqttServletGameServer();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return instance;
    }

    /**
     * The Main method
     */
    public static void main(String[] args) {
        MqttServletGameServer mqttServletGameServer = MqttServletGameServer.getInstance();

    }


//	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
//	Algorithm algorithm = Algorithm.HMAC256("thisstringisverysecret");

    /**
     * The the Player into the ListPlayer.
     *
     * @param Player p
     */
    public void addPlayer(Player p) {
        if (this.getListPlayer().contains(p)) return;
        this.getListPlayer().add(p);
    }

    /**
     * Remove player (Player logout)
     */

    public void removePlayer(Player p) {
        if (!this.getListPlayer().contains(p)) return;
        if (p.getCurrentRoom() != null) p.getCurrentRoom().removePlayer(p);
        this.getListPlayer().remove(p);
    }

    /**
     * The method to add the room into the RoomListener.
     *
     * @param Room r
     */
    public void addRoom(Room r) {
        if (getListRoom().contains(r)) return;
        this.getListRoom().add(r);
        r.addRoomListener(this);
    }

    /**
     * The method to get the Name of the Player.
     *
     * @param String name
     * @return Player playerName
     */
    public Player getPlayerByName(String name) {
        return this.getListPlayer().stream().filter(x -> name.equals(x.getName())).findFirst().orElse(null);
    }

    /**
     * The method to get the Id of the Room.
     *
     * @param long id
     * @return Room roomId
     */
    public Room getRoomByID(long id) {
        return this.getListRoom().stream().filter(x -> x.getRoomID() == id).findFirst().orElse(null);
    }

    /**
     * The method to get the Query of the Map.
     *
     * @param String query
     * @return Map<String, String> map
     */
    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }


    /**
     * The simple example run the main function. Create a MQTT client. Connect  the Broker. Subscribe the server topic.
     *
     * @throws MqttException
     */
    public void run() throws MqttException {
        MqttClient myClient;
        MqttConnectOptions connOpt;
        connOpt = new MqttConnectOptions();

        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);

        // Connect to Broker
        try {
            myClient = new MqttClient(BROKER_URL, "pokerserver" + System.currentTimeMillis(), new MemoryPersistence());
            myClient.setCallback(this);
            myClient.connect(connOpt);
            this.sender = new Sender(myClient);
            int subQoS = 0;
            myClient.subscribe(SERVER_TOPIC, subQoS);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method to print the exception.
     *
     * @param Throwable cause
     */
    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub
        cause.printStackTrace(System.out);

    }

    /**
     * The callback function print the messagne when it's called.
     *
     * @param String topic, MqttMessage message
     */
    public void processingMessage(String topic, MqttMessage message) throws Exception {
        // TODO Auto-generated method stub
        String body = new String(message.getPayload(), Charset.forName("UTF-8"));
        System.out.println("topic:" + topic + " msg:" + body);

    }

    /**
     * Override the messageArrived method.
     *
     * @param String topic, MqttMessage message
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            processingMessage(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }

    /**
     * The method to get the list of the room.
     *
     * @return List<Room> listRoom
     */
    public List<Room> getListRoom() {
        return listRoom;
    }

    /**
     * The method to set the room list.
     *
     * @param List<Room> listRoom
     */
//    public void setListRoom(List<Room> listRoom) {
//        this.listRoom = listRoom;
//    }

    /**
     * The method to get the list of the player.
     *
     * @return List<Player> listPlayer
     */
    public List<Player> getListPlayer() {
        return listPlayer;
    }

    /**
     * The method to set the player list.
     *
     * @param List<Player> listPlayer
     */
//    public void setListPlayer(List<Player> listPlayer) {
//        this.listPlayer = listPlayer;
//    }


    /**
     * Override the actionPerformed to push the message to the MqttServer
     *
     * @param RoomEvent event
     */
    @Override
    public void actionPerformed(AbstractRoomEvent event) {
        logger.log(Level.SEVERE, event.toString());
        String content = "";
        if (event instanceof GameActRoomEvent) {
            content += "cmd=" + RoomAction.GAMEACTION + "&roomid=" + event.getSrc().getRoomID();
            GameActRoomEvent gare = (GameActRoomEvent) event;
            AbstractGameEvent ge = gare.getE();
            content += "&gameEvent=" + ge.getType() + "&gameid=" + ge.getSrc().getId();
            if (ge instanceof PlayerActionGameEvent) {
                PlayerActionGameEvent pge = (PlayerActionGameEvent) ge;
                AbstractPlayerEvent e = pge.getPE();

                if (e instanceof PlayerBetEvent) {
                    PlayerBetEvent pe = (PlayerBetEvent) e;
                    long amount = pe.getAmount();
                    Player p = pe.getSrc();
                    /**
                     * Minus Balance of USER and update to Datastore*/
                    User user = ofy().load().type(User.class).id(p.getName()).now();
                    checkNotNull(user, "User is null when loaded from datastore!");
                    user.setBalance(user.getBalance() - amount);
                    checkNotNull(ofy().save().entity(user).now(), "Update to datastore failed!");
                    content += "&pid=" + p.getId() + "&playeraction=bet&amount=" + amount;
                }
                if (e instanceof PlayerFoldEvent) {
                    PlayerFoldEvent pe = (PlayerFoldEvent) e;
                    Player p = pe.getSrc();
                    content += "&pid=" + p.getId() + "&playeraction=fold";
                }
                if (e instanceof PlayerCheckEvent) {
                    Player p = e.getSrc();
                    content += "&pid=" + p.getId() + "&playeraction=check";
                }
                if (e instanceof PlayerCallEvent) {
                    Player p = e.getSrc();
                    content += "&pid=" + p.getId() + "&playeraction=call";
                }
                if (e instanceof GetTurnPlayerEvent) {
                    Player p = e.getSrc();
                    content += "&pid=" + p.getId() + "&playeraction=myturn";
                }
            }

            if (ge instanceof RoundGameEvent) {
                RoundGameEvent rge = (RoundGameEvent) ge;
                Game src = ge.getSrc();
                if (rge.getType() == GameAction.WAITTING) {
                    content += "&sb=" + src.getSmallBlind().getId() + "&bb=" + src.getBigBlind().getId() + "&dealer=" + src.getDealer().getId()
                            + "&listPlayers=" + src.getListPlayer();
                }
                if (rge.getType() == GameAction.PREFLOP) {
                    List<Player> players = rge.getSrc().getListPlayer();
                    StringBuffer playerHands = new StringBuffer();
                    playerHands.append("[");
                    for (Player player : players) {
                        if (!player.isSittingOut()) playerHands.append(player.toJson() + ",");
                    }
                    playerHands.setLength(playerHands.length() - 1);
                    playerHands.append("]");
                    content += "&preflopHands=" + playerHands.toString();
                }
                if (rge.getType() == GameAction.FLOP) {
                    content += "&flopcard=" + rge.getSrc().getBoard().getFlopCards().toString();
                }
                if (rge.getType() == GameAction.TURN) {
                    content += "&turncard=" + rge.getSrc().getBoard().getTurnCard().toString();
                }
                if (rge.getType() == GameAction.RIVER) {
                    content += "&rivercard=" + rge.getSrc().getBoard().getRiverCard().toString();
                }
            }
            if (ge instanceof EndGameEvent) {
                EndGameEvent ege = (EndGameEvent) ge;
                content += "&playerwin=" + ege.getPlayerwins() + "&rank=" + ege.getRank() + "&besthand=" + ege.getBestHands();
            }

        } else if (event instanceof VisitRoomEvent) {
            VisitRoomEvent vre = (VisitRoomEvent) event;
            if (vre.getType() == RoomAction.PLAYERJOINEDROOM) {
                content += "cmd=" + RoomAction.PLAYERJOINEDROOM + "&roomid=" + event.getSrc().getRoomID();
                Player p = vre.getP();
                content += "&pid=" + p.getId() + "&balance=" + p.getBalance();
            }
            if (vre.getType() == RoomAction.PLAYERLEFT) {
                Player p = vre.getP();
                content += "cmd=" + RoomAction.PLAYERLEFT + "&roomid=" + event.getSrc().getRoomID() + "&pid=" + p.getId();
            }
        }
        System.out.println("TOPIC LINK: "+MqttServletGameServer.SERVER_TOPIC + "/room/" + event.getSrc().getRoomID());
        this.sender.add(MqttServletGameServer.SERVER_TOPIC + "/room/" + event.getSrc().getRoomID(), content);

    }

    @Override
    public String jmx_getRoomsList() {
        return this.listRoom.toString();
    }

    @Override
    public String jmx_getPlayerList() {
        return listPlayer.toString();
    }
}