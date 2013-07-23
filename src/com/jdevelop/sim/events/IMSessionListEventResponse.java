package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IMSessionListEventResponse extends AbstractEvent {
    public static final int EVENT_NAME = 10;

    protected String[] sessions;

    public IMSessionListEventResponse() {
        super(EVENT_NAME);
    }

    public IMSessionListEventResponse(String[] sessions) {
        this();
        key = "i";
        this.sessions = sessions;
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
        int count = tok.readInt();
        sessions = new String[count];

        for (int i = 0; i < count; i++) {
            sessions[i] = tok.readUTF();
        }
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
        stream.writeInt(sessions.length);
        for (int i = 0; i < sessions.length; i++)
            stream.writeUTF(sessions[i]);
    }

    public String[] getSessions() {
        return sessions;
    }
}