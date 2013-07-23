package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoginEventResponse extends AbstractEvent {
    public static final int EVENT_NAME = 8;

    protected String allowedHosts;

    protected String login;

    protected String payed;

    public LoginEventResponse() {
        super(EVENT_NAME);
    }

    public LoginEventResponse(String key) {
        super(EVENT_NAME, key);
    }

    public LoginEventResponse(String key, String login, String allowedHosts,
            String payed) {
        this(key);
        this.allowedHosts = allowedHosts;
        this.login = login;
        this.payed = payed;
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
        login = tok.readUTF();
        allowedHosts = tok.readUTF();
        payed = tok.readUTF();
        if (com.jdevelop.sim.modules.mainChat.DEBUG)
            System.out.println(login + ":" + allowedHosts + ":" + payed);
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
        stream.writeUTF(login);
        stream.writeUTF(allowedHosts);
        stream.writeUTF(payed);
    }

    public String getAllowedHosts() {
        return allowedHosts;
    }

    public String getLogin() {
        return login;
    }

    public String getPayed() {
        return payed;
    }
}