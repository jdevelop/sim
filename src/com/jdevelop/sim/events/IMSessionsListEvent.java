package com.jdevelop.sim.events;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IMSessionsListEvent extends AbstractEvent {
    public static final int EVENT_NAME = 1;

    public IMSessionsListEvent() {
        super(EVENT_NAME);
    }

    public IMSessionsListEvent(String key) {
        super(EVENT_NAME, key);
    }

    public void readObjectBody(DataInputStream tok) throws IOException {
    }

    public void writeObjectBody(DataOutputStream stream) throws IOException {
    }

}