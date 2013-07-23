package com.jdevelop.sim.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.jdevelop.sim.events.AbstractEvent;
import com.jdevelop.sim.modules.mainChat;

public class serverThread extends Thread {
    protected mainChat chat;

    protected Socket server;

    public serverThread(mainChat chat, Socket s) {
        this.chat = chat;
        this.server = s;
    }

    public void run() {
        AbstractEvent result = null;
        try {
            InputStream br = server.getInputStream();

            try {
                AbstractEvent event = AbstractEvent.getEvent(br);
                result = chat.getActionController().processEvent(event,
                        server.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace(System.out);
            } catch (ClassNotFoundException e) {
                e.printStackTrace(System.out);
            }

            OutputStream ous = server.getOutputStream();

            if (result != null) {
                result.writeObject(new DataOutputStream(ous));
            }
            ous.flush();
            ous.close();
            server.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}