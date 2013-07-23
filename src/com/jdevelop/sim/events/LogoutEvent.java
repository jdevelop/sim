package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LogoutEvent extends AbstractEvent {
    public static final int EVENT_NAME = 11;

    public LogoutEvent() {
        super(EVENT_NAME);
    }

    public LogoutEvent(String key) {
        super(EVENT_NAME, key);
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
    }

}