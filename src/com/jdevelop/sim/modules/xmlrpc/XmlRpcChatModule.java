package com.jdevelop.sim.modules.xmlrpc;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.xmlrpc.WebServer;

import com.jdevelop.sim.modules.info;
import com.jdevelop.sim.modules.mainChat;

public class XmlRpcChatModule {

    private static final int DEFAULT_PORT = 2003;

    static WebServer server;

    protected mainChat chat;

    public XmlRpcChatModule(mainChat chat, Properties runtime) {
        try {
            server = new WebServer(Integer.parseInt(runtime
                    .getProperty("xml-rpc.port")));
        } catch (NumberFormatException e) {
            System.out
                    .println("Port is no-numeric, setting to " + DEFAULT_PORT);
            server = new WebServer(DEFAULT_PORT);
        } finally {
            this.chat = chat;
        }
    }

    public void start() {
        server.addHandler("$default", this);
        server.start();
    }

    public Vector onlineMembers(Vector ids) {
        Vector results = new Vector();
        synchronized (chat.users) {
            for (int i = 0; i < ids.size(); i++) {
                results
                        .addElement(chat.users.containsKey(ids.elementAt(i)) ? "1"
                                : "0");
            }
        }
        return results;
    }

    public Vector getAllMembers() {
        Vector results = new Vector();
        synchronized (chat.users) {
            Iterator it = chat.users.iterator();
            while (it.hasNext()) {
                info inf = (info) it.next();
                results.addElement(inf.ID);
            }
        }
        return results;
    }

    public void refreshUser(String key) {
        chat.refreshUser(key);
    }

    public String loginUser(String username, String password) {
        String key = null;
        try {
            key = chat.loginUser(username, password);
        } catch (SQLException e) {
            System.err.println(e);
        }
        return key;
    }

    public void logoutUser(String key) {
        chat.removeOnlineUser(key);
    }

    public void unblock(String user, String target) {
        chat.unblock(user, target);
    }

    public void block(String user, String target) {
        chat.block(user, target);
    }

}