package com.jdevelop.sim.connectors;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

import com.jdevelop.sim.events.AbstractEvent;

public class SocketConnector extends AbstractConnector {
    private String host;

    private int port;

    public SocketConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public AbstractEvent getResponse(AbstractEvent evt) {
        try {
            System.out.println("Sending request to server: " + host + ":"
                    + port);
            Socket client = new Socket(host, port);
            DataOutputStream out = new DataOutputStream(client
                    .getOutputStream());
            evt.writeObject(out);
            System.out.println("Done");
            System.out.println("Getting response from server ....");
            InputStream is = client.getInputStream();
            AbstractEvent event = AbstractEvent.getEvent(is);
            is.close();
            out.close();
            client.close();
            System.out.println("Done");
            return event;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
}