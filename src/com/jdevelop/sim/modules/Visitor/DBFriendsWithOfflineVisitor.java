/*
 * Created on 12.05.2005 
 * @author Eugeny N. Dzhurinsky, 
 * JDevelop Software
 * eugenydzh@jdevelop.com
 */
package com.jdevelop.sim.modules.Visitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;

import com.jdevelop.sim.modules.SortedMapContainer;
import com.jdevelop.sim.modules.info;

public class DBFriendsWithOfflineVisitor implements UserListVisitor {

    private Connection dbh;

    private Properties runtime;

    private Object UID;

    private SortedMapContainer users;

    public DBFriendsWithOfflineVisitor(Connection dbh, Properties runtime,
            Object UID, SortedMapContainer users) {
        this.dbh = dbh;
        this.runtime = runtime;
        this.UID = UID;
        this.users = users;
    }

    public Vector getUserList() {
        LinkedList online = new LinkedList();
        LinkedList offline = new LinkedList();
        try {
            String SQL = "select " + runtime.getProperty("BUDDY_TABLE_BUDDY")
                    + " from " + runtime.getProperty("BUDDY_TABLE_NAME")
                    + " where " + runtime.getProperty("BUDDY_TABLE_ID") + "=?";
	    if (com.jdevelop.sim.modules.mainChat.DEBUG)
		System.out.println(SQL+" ("+UID+")");
            PreparedStatement psth = dbh.prepareStatement(SQL);
            psth.setObject(1, UID);
            ResultSet res = psth.executeQuery();
            while (res.next()) {
                String ID = res.getString(1);
                info inf = (info) users.get(ID);
                if (inf != null && !inf.getIgnoreList().contains(UID)) {
                    online.add(ID + ":" + inf.getSex() + ":" + inf.getAge()
                            + ":" + inf.getNick());
                } else {
                    inf = new info(dbh, ID, runtime);
                    offline
                            .add(ID + ":3:" + inf.getAge() + ":"
                                    + inf.getNick());
                }
            }
            res.close();
            psth.close();
        } catch (SQLException e) {
            System.out.println("Exception " + e.toString() + " occured ");
        }
        Vector v = new Vector(online.size() + offline.size() + 1);
        v.addAll(online);
        v.add("::delimiter::");
        v.addAll(offline);
	System.out.println(v);
        return v;

    }

}
