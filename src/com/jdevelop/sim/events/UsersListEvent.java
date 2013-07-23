package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UsersListEvent extends AbstractEvent {
    public static final int EVENT_NAME = 2;

    public UsersListEvent() {
        super(EVENT_NAME);
    }

    public UsersListEvent(String key) {
        super(EVENT_NAME, key);
    }

    public int getSize() {
        return 0;
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
    }
}