package com.jdevelop.sim.events;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractEvent {
    protected int message = 0;

    protected String key;

    protected int cSize;

    public AbstractEvent(int message) {
        this.message = message;
    }

    public AbstractEvent(int message, String key) {
        this.message = message;
        this.key = key;
    }

    public int getMessageCode() {
        return message;
    }

    public String getKey() {
        return key;
    }

    public DataInputStream parseCommonHeader(DataInputStream stream)
            throws IOException {
        cSize = stream.readInt();
        if (com.jdevelop.sim.modules.mainChat.DEBUG)
            System.out.println("size is " + cSize);
        int offset = 0;
        byte[] b = new byte[cSize];
        while (offset < cSize) {
            offset += stream.read(b, offset, cSize - offset);
        }
        if (com.jdevelop.sim.modules.mainChat.DEBUG)
            System.out.println("All data read...");
        DataInputStream raw = new DataInputStream(new ByteArrayInputStream(b));
        key = raw.readUTF();
        if (com.jdevelop.sim.modules.mainChat.DEBUG)
            System.out.println("key is " + key);
        return raw;
    }

    public void readObject(InputStream stream) {
        try {
            readObjectBody(parseCommonHeader(new DataInputStream(stream)));
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public abstract void readObjectBody(DataInputStream tok) throws IOException;

    public void writeObject(DataOutputStream stream) {
        try {
            ByteArrayOutputStream helper = new ByteArrayOutputStream();
            DataOutputStream bos = new DataOutputStream(helper);
            System.err.println("Key is:" + key);
            bos.writeUTF(key);
            writeObjectBody(bos);
            bos.flush();
            stream.write(message);
            stream.writeInt(helper.size());
            stream.write(helper.toByteArray());
            helper.close();
            bos.close();
            stream.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public abstract void writeObjectBody(DataOutputStream stream)
            throws IOException;

    public static AbstractEvent getEvent(InputStream stream) {
        AbstractEvent event = null;

        try {
            int message = stream.read();
            if (com.jdevelop.sim.modules.mainChat.DEBUG)
                System.out.println("Code of message is " + message);
            switch (message) {
            case AddMessageEvent.EVENT_NAME:
                event = new AddMessageEvent();
                break;
            case DeclineEvent.EVENT_NAME:
                event = new DeclineEvent();
                break;
            case IgnoreEvent.EVENT_NAME:
                event = new IgnoreEvent();
                break;
            case IMSessionsListEvent.EVENT_NAME:
                event = new IMSessionsListEvent();
                break;
            case LoginEvent.EVENT_NAME:
                event = new LoginEvent();
                break;
            case MessageListEvent.EVENT_NAME:
                event = new MessageListEvent();
                break;
            case UsersListEvent.EVENT_NAME:
                event = new UsersListEvent();
                break;
            case IMSessionListEventResponse.EVENT_NAME:
                event = new IMSessionListEventResponse();
                break;
            case LoginEventResponse.EVENT_NAME:
                event = new LoginEventResponse();
                break;
            case MessageListeEventResponse.EVENT_NAME:
                event = new MessageListeEventResponse();
                break;
            case UserListEventResponse.EVENT_NAME:
                event = new UserListEventResponse();
                break;
            case LogoutEvent.EVENT_NAME:
                event = new LogoutEvent();
                break;
            }
            if (com.jdevelop.sim.modules.mainChat.DEBUG)
                System.out.println("Working with " + event);
            if (event != null) {
                event.readObject(stream);
            }
        } catch (IOException e) {
            if (com.jdevelop.sim.modules.mainChat.DEBUG)
                System.out.println("Exception while reading message " + e);
        }
        if (com.jdevelop.sim.modules.mainChat.DEBUG)
            System.out.println("Finished with " + event);
        return event;
    }
}