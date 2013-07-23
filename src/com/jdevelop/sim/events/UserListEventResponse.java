package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class UserListEventResponse extends AbstractEvent {
    public static final int EVENT_NAME = 9;

    protected Vector userList;

    public UserListEventResponse() {
        super(EVENT_NAME);
    }

    public UserListEventResponse(Vector userList) {
        this();
        key = "i";
        this.userList = userList;
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
        stream.writeInt(userList.size());
        for (int i=0; i<userList.size(); i++ )
            stream.writeUTF((String) userList.elementAt(i));
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
        int size = tok.readInt();
        userList = new Vector();
        for (int i = 0; i < size; i++)
            userList.addElement(tok.readUTF());

    }

    public Vector getUserList() {
        return userList;
    }
}
