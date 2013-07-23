package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.jdevelop.sim.modules.Message;

public class AddMessageEvent extends AbstractEvent {
    public static final int EVENT_NAME = 3;

    protected Message message;

    public AddMessageEvent() {
        super(EVENT_NAME);
    }

    public AddMessageEvent(String key) {
        super(EVENT_NAME, key);
    }

    public AddMessageEvent(Message message, String key) {
        super(EVENT_NAME, key);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void readObjectBody(DataInputStream tok) {
        try {
            message = new Message(tok);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void writeObjectBody(DataOutputStream stream) {
        message.writeSerialized(stream);
    }

}