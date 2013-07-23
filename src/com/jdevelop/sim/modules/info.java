package com.jdevelop.sim.modules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

public class info {
    public String ID;

    private String nick;

    private int sex = 1;

    private String age;

    private String paid;

    private String accept_chat;

    private HashSet ignorelist = new HashSet();

    public info(Connection dbh, String id, Properties runtime) {
        String SQL = "";

        try {
            SQL = "select " + runtime.getProperty("PROFILE_TABLE_USERNAME")
                    + "," + runtime.getProperty("PROFILE_TABLE_SEX") + ","
                    + runtime.getProperty("PROFILE_TABLE_AGE") + ","
                    + runtime.getProperty("PROFILE_TABLE_PAID") + ","
                    + runtime.getProperty("PROFILE_TABLE_ACCEPT_CHAT")
                    + " from " + runtime.getProperty("PROFILE_TABLE_NAME")
                    + " where " + runtime.getProperty("PROFILE_TABLE_ID")
                    + "=?";
            PreparedStatement psth = dbh.prepareStatement(SQL);
            psth.setString(1, id);
            ResultSet rs = psth.executeQuery();
            rs.next();
            ID = id;
            nick = rs.getString(1);
            sex = rs.getInt(2);
            age = rs.getString(3);
            paid = rs.getString(4);
            accept_chat = rs.getString(5);
            if (runtime.containsKey("IGNORE_TABLE_NAME")) {
                psth = dbh.prepareStatement("select "
                        + runtime.getProperty("IGNORE_TABLE_IGNORE") + " from "
                        + runtime.getProperty("IGNORE_TABLE_NAME") + " where "
                        + runtime.getProperty("IGNORE_TABLE_UID") + "=?");
                psth.setString(1, id);
                rs = psth.executeQuery();

                if (rs.next()) {
                    String ignores = rs.getString(1);

                    try {
                        if (ignores != null) {
                            StringTokenizer tokenizer = new StringTokenizer(
                                    ignores, ",");

                            while (tokenizer.hasMoreTokens()) {
                                ignorelist.add(tokenizer.nextToken());
                            }
                        }
                    } catch (Exception e) {
                    }
                }

                rs.close();
                psth.close();
            }
        } catch (SQLException e) {
            System.out.println("Error in info");
            System.out
                    .println("Exception was thrown while executing this SQL = "
                            + SQL);
            System.out.println("the ID is " + id);
            e.printStackTrace(System.out);
        }
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNick() {
        return nick;
    }

    public void saveInfo(Connection dbh, Properties runtime) {
        if (!runtime.containsKey("IGNORE_TABLE_NAME"))
            return;
        Iterator i = ignorelist.iterator();
        String update = "";

        while (i.hasNext()) {
            String id_ = (String) i.next();
            update += id_;

            if (i.hasNext()) {
                update += ",";
            }
        }
        try {
            PreparedStatement psth = null;
            psth = dbh.prepareStatement("select 1 from "
                    + runtime.getProperty("IGNORE_TABLE_NAME") + " where "
                    + runtime.getProperty("IGNORE_TABLE_UID") + "=?");
            psth.setObject(1, ID);

            ResultSet res = psth.executeQuery();

            if (res.next()) {
                psth.close();
                psth = dbh.prepareStatement("update "
                        + runtime.getProperty("IGNORE_TABLE_NAME") + " set "
                        + runtime.getProperty("IGNORE_TABLE_IGNORE") + " = ?"
                        + " where " + runtime.getProperty("IGNORE_TABLE_UID")
                        + " = ?");
            } else {
                psth.close();
                psth = dbh.prepareStatement("insert into "
                        + runtime.getProperty("IGNORE_TABLE_NAME") + "("
                        + runtime.getProperty("IGNORE_TABLE_IGNORE") + ","
                        + runtime.getProperty("IGNORE_TABLE_UID")
                        + ") values (?,?)");
            }

            psth.setObject(1, update);
            psth.setObject(2, ID);
            psth.execute();
            res.close();
            psth.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void updateIgnored(Connection dbh, Properties runtime, Object ignore) {
        if (!runtime.containsKey("EX_IGNORE_TABLE_NAME"))
            return;
        try {
            PreparedStatement psth = dbh.prepareStatement("select 1 from "
                    + runtime.getProperty("EX_IGNORE_TABLE_NAME") + " WHERE "
                    + runtime.getProperty("EX_IGNORE_TABLE_UID") + " = ? and "
                    + runtime.getProperty("EX_IGNORE_TABLE_IGNORE") + "=?");
            psth.setObject(1, ID);
            psth.setObject(2, ignore);
            ResultSet res = psth.executeQuery();
            boolean exists = res.next();
            res.close();
            psth.close();
            if (!exists) {
                psth = dbh.prepareStatement("insert into "
                        + runtime.getProperty("EX_IGNORE_TABLE_NAME") + " ("
                        + runtime.getProperty("EX_IGNORE_TABLE_UID") + ","
                        + runtime.getProperty("EX_IGNORE_TABLE_IGNORE")
                        + ") values (?,?)");
                psth.setObject(1, ID);
                psth.setObject(2, ignore);
                psth.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public HashSet getIgnoreList() {
        return ignorelist;
    }

    public void setIgnored(String UID, Connection dbh, Properties runtime) {
        ignorelist.add(UID);
        updateIgnored(dbh, runtime, UID);
    }

    public String toString() {
        return nick;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String string) {
        age = string;
    }

    public String getPaid() {
        return paid;
    }

    public String getAccept_chat() {
        return accept_chat;
    }
}
