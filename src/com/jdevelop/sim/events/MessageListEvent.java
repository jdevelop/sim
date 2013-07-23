package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessageListEvent extends AbstractEvent {
    public static final int EVENT_NAME = 4;

    protected String target;

    public MessageListEvent() {
        super(EVENT_NAME);
    }

    public MessageListEvent(String key) {
        super(EVENT_NAME, key);
    }

    public MessageListEvent(String key, String target) {
        this(key);
        this.target = target;
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
        target = tok.readUTF();
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
        stream.writeUTF(getTarget());
    }

    public String getTarget() {
        return target;
    }
}