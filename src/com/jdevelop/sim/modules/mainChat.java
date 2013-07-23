package com.jdevelop.sim.modules;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import com.jdevelop.sim.events.IMSessionListEventResponse;
import com.jdevelop.sim.events.LoginEvent;
import com.jdevelop.sim.events.LoginEventResponse;
import com.jdevelop.sim.events.MessageListeEventResponse;
import com.jdevelop.sim.events.UserListEventResponse;
import com.jdevelop.sim.modules.Visitor.AllUsersVisitor;
import com.jdevelop.sim.modules.Visitor.DBFriendsWithOfflineVisitor;
import com.jdevelop.sim.modules.Visitor.UserListVisitor;

public class mainChat {
    private static final boolean IS_LIST = false;

    public static final boolean DEBUG = false;

    public static Connection dbh;

    private static int INITIALTIMEOUT = 12;

    private static int TIMEOUT = 5;

    public static int KeyLength;

    private HashMap online;

    public SortedMapContainer users;

    public MsgList msg;

    private HashMap timeout;

    private int usercnt;

    Thread t;

    Properties runtime;

    ActionController controller;

    protected String hosts = "";

    /**
     * Class constructor
     * @param properties
     *            file name to load runtime settings from
     */
    public mainChat(String properties) {
        runtime = new Properties();
        usercnt = 0;

        try {
            runtime.load(new FileInputStream(properties));
            Class.forName(runtime.getProperty("driver")).newInstance();

            String dbprovider = runtime.getProperty("dbprovider");
            String dbname = runtime.getProperty("dbname");
            String dbuser = runtime.getProperty("dbuser");
            String dbhost = runtime.getProperty("dbhost");
            String dbport = runtime.getProperty("dbport");
            String dbpassword = runtime.getProperty("dbpassword");
            String connection = "";

            if ("odbc".equalsIgnoreCase(dbprovider)) {
                connection = "jdbc:" + dbprovider + ":" + dbname;
            } else {
                connection = "jdbc:" + dbprovider + "://" + dbhost + ":"
                        + dbport + "/" + dbname;
            }

            System.out.println(connection);
            dbh = DriverManager.getConnection(connection, dbuser, dbpassword);
            KeyLength = Integer
                    .parseInt(runtime.getProperty("keylength", "12"));
        } catch (Exception E) {
            E.printStackTrace(System.out);
        }

        INITIALTIMEOUT = Integer.parseInt(runtime.getProperty(
                "initial_timeout", "12"));
        TIMEOUT = Integer.parseInt(runtime.getProperty("timeout", "5"));
        online = new HashMap();
        users = new SortedMapContainer(new UserComparator());
        msg = new MsgList();
        timeout = new HashMap();
        controller = new ActionController(this);
        hosts = runtime.getProperty("acceptedHosts", "locahost");
    }

    /**
     * Register the user and generate the key
     * @param UID
     *            object for user ID
     * @param login
     *            user name
     * @param payed
     *            "1" if user is payed, "0" - otherwise
     * @return LoginEventResponse
     */
    public LoginEventResponse registerUser(String UID, LoginEvent login,
            String payed) {
        String KeyString = "qazwsxedcrfvtgbyhnujmikolpPOLIKMUJNYHBTGVRFCEDXWSZQA";
        String s;

        do {
            s = "";

            for (int i = 0; i <= KeyLength; i++) {
                int ind = (int) (Math.random() * 100D) % KeyString.length();
                s = s.concat(KeyString.substring(ind, ind + 1));
            }
        } while (online.containsKey(s));
        info inf = null;
        try {
            inf = new info(dbh, UID, runtime);
            synchronized (users) {
                users.put(UID, inf);
            }
            if (online.containsValue(UID)) {
                synchronized (online) {
                    Set set = online.entrySet();

                    for (Iterator i = set.iterator(); i.hasNext();) {
                        java.util.Map.Entry me = (java.util.Map.Entry) i.next();

                        if ((me.getValue()).equals(UID)) {
                            s = (String) me.getKey();

                            break;
                        }
                    }
                }
            }

            online.put(s, UID);
            synchronized (timeout) {
                timeout.put(UID, new Integer(INITIALTIMEOUT));
            }
            usercnt++;
            if (1 == usercnt) {
                System.out
                        .println("We are about to create the new controller session...");
                t = new Controller();
                t.setPriority(Thread.NORM_PRIORITY);
                t.start();
            }

        } catch (Exception SQLE) {
            SQLE.printStackTrace(System.out);

            return new LoginEventResponse("");
        }

        LoginEventResponse resp = new LoginEventResponse(s, inf.getNick(),
                hosts, payed);

        return resp;
    }

    public String loginUser(String login, String password) throws SQLException {
        LoginEvent loginEvt = new LoginEvent(login, password);
        LoginEventResponse resp = loginUser(loginEvt, "n/a");
        String key = resp.getKey();
        return key;
    }

    /**
     * Check user against the database, get's UID and performs the register user
     * operation
     * @param login
     *            LoginEvent object
     * @param IP
     *            IP address
     * @return LoginEventResponse
     */
    public LoginEventResponse loginUser(LoginEvent login, String IP)
            throws SQLException {
        try {
            synchronized (online) {
                PreparedStatement sth = dbh.prepareStatement("select "
                        + runtime.getProperty("AUTH_TABLE_ID") + " from "
                        + runtime.getProperty("AUTH_TABLE_NAME") + " where "
                        + runtime.getProperty("AUTH_TABLE_LOGIN") + "=? AND "
                        + runtime.getProperty("AUTH_TABLE_PASSWORD") + "=?");
                sth.setString(1, login.getLogin());
                sth.setString(2, login.getPassword());

                ResultSet rs = sth.executeQuery();

                if (rs.next()) {
                    String id = rs.getString(1);
                    rs.close();
                    sth.close();

                    info inf = new info(dbh, id, runtime);

                    return registerUser(id, login, inf.getPaid());
                }
                rs.close();
                sth.close();
            }
        } catch (Exception E) {
            E.printStackTrace(System.out);
        }

        System.out.println("Wrong username/password :" + login.getLogin() + "/"
                + login.getPassword());

        return null;
    }

    /**
     * Returns the UID of user, identified by key
     * @param key
     *            secure key
     * @return user identifier
     */
    public String getUID(String key) {
        return (String) online.get(key);
    }

    /**
     * Returns the online users list
     */
    public synchronized UserListEventResponse getOnlineList(String key) {
        Vector ret = new Vector();
        synchronized (users) {
            UserListVisitor visitor = null;
            if (IS_LIST) {
                /*
                 * visitor = new DBFriendsListVisitor(dbh, runtime, getUID(key),
                 * users);
                 */
                visitor = new DBFriendsWithOfflineVisitor(dbh, runtime,
                        getUID(key), users);
            } else {
                visitor = new AllUsersVisitor(users);
            }
            if (visitor != null)
                ret = visitor.getUserList();
            refreshUser(key);
        }
        return new UserListEventResponse(ret);
    }

    public IMSessionListEventResponse getIMSessions(String key) {
        String[] ret = new String[0];

        try {
            info inf = (info) users.get(online.get(key));

            if (inf == null) {
                return new IMSessionListEventResponse(ret);
            }

            HashSet tmp = msg.getIMSessions(inf);
            Iterator it = tmp.iterator();
            int index = 0;
            ret = new String[tmp.size()];

            while (it.hasNext()) {
                String id = (String) it.next();
                info tmpinfo = (info) users.get(id);
                ret[index++] = tmpinfo.ID + ":" + tmpinfo.getNick();
            }
            refreshUser(key);
        } catch (Exception exception) {
        }

        return new IMSessionListEventResponse(ret);
    }

    public HashMap getTimeoutList() {
        return timeout;
    }

    public MessageListeEventResponse getMessageList(String key, String target) {
        String UID = getUID(key);
        if (UID == null) {
            return null;
        } else {
            info usr = (info) users.get(UID);
            Message[] msgs = msg.getPrivate(usr, target);
            return new MessageListeEventResponse(msgs);
        }
    }

    public void removeUserDueTimeout(String UID) {
        if ((UID == null) || "".equals(UID)) {
            return;
        }

        Set ou = online.entrySet();

        for (Iterator i = ou.iterator(); i.hasNext();) {
            java.util.Map.Entry me = (java.util.Map.Entry) i.next();

            if (UID.equals(me.getValue())) {
                info inf = (info) users.get(UID);
                if (DEBUG) {
                    System.out.println("Timeout: Logging out user "
                            + inf.getNick());
                }
                inf.saveInfo(dbh, runtime);
                online.remove(me.getKey());
                users.remove(UID);
                timeout.remove(UID);
                msg.removeUser(UID);
                usercnt--;

                break;
            }
        }
    }

    public void removeOnlineUser(String key) {
        String UID = (String) online.get(key);

        if (UID == null) {
            return;
        } else {
            info inf = (info) users.get(UID);
            if (DEBUG) {
                System.out.println("LogOut: Logging out user " + inf.getNick());
            }
            inf.saveInfo(dbh, runtime);
            online.remove(key);
            users.remove(UID);
            timeout.remove(UID);
            msg.removeUser(UID);
            usercnt--;

            return;
        }
    }

    public void ignoreUser(String key, String UID) {
        try {
            info inf = (info) users.get(getUID(key));
            inf.setIgnored(UID, dbh, runtime);
            inf.saveInfo(dbh, runtime);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void pushAll() {
        synchronized (timeout) {
            Set ts = timeout.entrySet();
            Iterator ti = ts.iterator();

            try {
                System.out.println("Here " + usercnt + " users");
                while (ti.hasNext()) {
                    java.util.Map.Entry me = (java.util.Map.Entry) ti.next();
                    int ctime = ((Integer) me.getValue()).intValue();
                    if (DEBUG)
                        System.out.println(me.getKey() + ":" + ctime);
                    ctime--;

                    if (me.getKey() != null) {
                        if (ctime <= 0) {
                            removeUserDueTimeout((String) me.getKey());
                        } else {
                            timeout.put(me.getKey(), new Integer(ctime));
                        }
                    }
                }
            } catch (ConcurrentModificationException concurrentmodificationexception) {
            }
        }
    }

    public void addMessage(String key, Message message) {
        String ifrom = (String) getUID(key);
        message.uid = ifrom;
        msg.addMessage(message, (info) users.get(getUID(key)), (info) users
                .get(message.privat));
        if (!users.containsKey(message.privat)) {
            Message messsg = new Message(message.privat, message.uid, runtime
                    .getProperty("offline_message",
                            "Sorry, user is offline for now"), true, true,
                    false, "#ff0000", 14, "Times");
            msg.addMessage(messsg, new info(dbh, message.privat, runtime),
                    (info) users.get(getUID(key)));
        }

        refreshUser(key);
    }

    public void removeRequest(String key, String id) {
        try {
            info uid = null;
            uid = (info) users.get(getUID(key));
            msg.getPrivate(uid, id);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public ActionController getActionController() {
        return controller;
    }

    public void removeUser(String key, String id) {
        msg.removeUser(getUID(key));
    }

    public void refreshUser(String key) {
        timeout.put(getUID(key), new Integer(TIMEOUT));
    }

    class Controller extends Thread implements Runnable {
        public Controller() {
            System.out.println("Creating the controller");
        }

        public void run() {
            if (DEBUG)
                System.out.println("Starting the controller thread... ("
                        + usercnt + ")");
            while (usercnt > 0)
                try {
                    if (DEBUG)
                        System.out.println("Running pushAll ...");
                    Thread.sleep(15000);
                    pushAll();
                    System.out.println("Done");
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            if (DEBUG)
                System.out.println("Controller ended.");
        }
    }

    /**
     * @return user by provided used ID
     * @param ID
     *            user ID
     */
    public info getUserByID(String ID) {
        return (info) users.get(ID);
    }

    /**
     * @param ID
     *            user id
     * @param target
     *            user to block
     */
    public void block(String ID, String target) {
        info inf = getUserByID(ID);
        inf.setIgnored(target, dbh, runtime);
    }

    /**
     * Unblocks user
     * @param ID
     *            user ID
     * @param target
     *            target user
     */
    public void unblock(String ID, String target) {
        info inf = getUserByID(ID);
        inf.getIgnoreList().remove(target);
        inf.saveInfo(dbh, runtime);
    }

    class UserComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            info u1 = (info) o1;
            info u2 = (info) o2;
            return u1.getNick().compareTo(u2.getNick());
        }
    }

}
