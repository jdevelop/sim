package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.jdevelop.sim.modules.Message;

public class DeclineEvent extends AbstractEvent {
    public static final int EVENT_NAME = 6;

    protected Message message;

    public DeclineEvent() {
        super(EVENT_NAME);
    }

    public DeclineEvent(String key) {
        super(EVENT_NAME, key);
    }

    public DeclineEvent(String key, Message message) {
        this(key);
        this.message = message;
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
        try {
            message = new Message(tok);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
        message.writeSerialized(stream);
    }

    public Message getMessage() {
        return message;
    }
}