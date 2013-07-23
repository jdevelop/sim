package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.jdevelop.sim.modules.Message;

public class MessageListeEventResponse extends AbstractEvent {
    public static final int EVENT_NAME = 12;

    protected Message[] messages;

    public MessageListeEventResponse() {
        super(EVENT_NAME);
    }

    public MessageListeEventResponse(Message[] msg) {
        this();
        key = "i";
        messages = msg;
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
        int msgCount = tok.readInt();
        messages = new Message[msgCount];
        for (int i = 0; i < msgCount; i++)
            messages[i] = new Message(tok);
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
        stream.writeInt(messages.length);
        for (int i = 0; i < messages.length; i++)
            messages[i].writeSerialized(stream);
    }

    public Message[] getMessages() {
        return messages;
    }
}