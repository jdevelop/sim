package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoginEvent extends AbstractEvent {

    public static final int EVENT_NAME = 0;

    protected String login;

    protected String password;

    public LoginEvent() {
        super(EVENT_NAME);
    }

    public LoginEvent(String key) {
        super(EVENT_NAME, key);
    }

    public LoginEvent(String login, String password) {
        this("i");
        this.login = login;
        this.password = password;
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
        login = tok.readUTF();
        password = tok.readUTF();
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
        stream.writeUTF(login);
        stream.writeUTF(password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}