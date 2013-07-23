/*
 * Created on 11.05.2005 
 * @author Eugeny N. Dzhurinsky, 
 * JDevelop Software
 * eugenydzh@jdevelop.com
 */
package com.jdevelop.sim.modules.Visitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import com.jdevelop.sim.modules.SortedMapContainer;
import com.jdevelop.sim.modules.info;

public class DBFriendsListVisitor implements UserListVisitor {

    private Connection dbh;

    private Properties runtime;

    private Object UID;

    private SortedMapContainer users;

    public DBFriendsListVisitor(Connection dbh, Properties runtime, Object UID,
            SortedMapContainer users) {
        this.dbh = dbh;
        this.runtime = runtime;
        this.UID = UID;
        this.users = users;
    }

    public Vector getUserList() {
        Vector ret = new Vector();
        try {
            PreparedStatement psth = dbh.prepareStatement("select "
                    + runtime.getProperty("BUDDY_TABLE_BUDDY") + " from "
                    + runtime.getProperty("BUDDY_TABLE_NAME") + " where "
                    + runtime.getProperty("BUDDY_TABLE_ID") + "=?");
            psth.setObject(1, UID);
            ResultSet res = psth.executeQuery();
            while (res.next()) {
                String ID = res.getString(1);
                if (users.containsKey(ID)) {
                    info inf = (info) users.get(ID);
                    ret.add(ID + ":" + inf.getSex() + ":" + inf.getAge() + ":"
                            + inf.getNick());
                }
            }
            res.close();
            psth.close();
        } catch (SQLException e) {
            System.out.println("Exception " + e.toString() + " occured ");
        }
        return ret;
    }

}