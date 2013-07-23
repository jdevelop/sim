package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IgnoreEvent extends AbstractEvent {
    public static final int EVENT_NAME = 5;

    protected String target;

    public IgnoreEvent() {
        super(EVENT_NAME);
    }

    public IgnoreEvent(String key) {
        super(EVENT_NAME, key);
    }

    public IgnoreEvent(String key, String target) {
        this(key);
        this.target = target;
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
        stream.writeUTF(getTarget());
    }

    public void readObjectBody(DataInputStream tok) {
        try {
            target = tok.readUTF();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public String getTarget() {
        return target;
    }
}