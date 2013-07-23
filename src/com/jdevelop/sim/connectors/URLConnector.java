package com.jdevelop.sim.connectors;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.jdevelop.sim.events.AbstractEvent;

public class URLConnector extends AbstractConnector {
    protected URL connectionURL;

    public URLConnector(String protocol, String host, int port, String file) {
        try {
            System.err.println(protocol + ":" + host + ":"
                    + (port <= 0 ? 80 : port) + ":" + file);
            connectionURL = new URL(protocol, host, port <= 0 ? 80 : port, file);
        } catch (MalformedURLException e) {
            e.printStackTrace(System.out);
        }
    }

    public AbstractEvent getResponse(AbstractEvent evt) {
        try {
            //System.out.println("Sending request....");
            URLConnection conn = connectionURL.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            //added the correct content-length
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            evt.writeObject(new DataOutputStream(bos));
            conn.setRequestProperty("Content-Length", String
                    .valueOf(bos.size()));
            //System.out.println("Content-length="+bos.size());
            OutputStream out = conn.getOutputStream();
            bos.writeTo(out);
            out.flush();
            //System.out.println("Done");
            //System.out.println("Getting response...");
            InputStream ois = conn.getInputStream();
            AbstractEvent event = AbstractEvent.getEvent(ois);
            ois.close();
            out.close();
            //System.out.println("Done");
            return event;
        } catch (Exception e) {
            //e.printStackTrace(System.out);
        }

        return null;
    }
}