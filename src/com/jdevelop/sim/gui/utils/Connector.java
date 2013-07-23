/*
 * Created on 15.02.2005 
 * @author Eugeny N. Dzhurinsky, 
 * JDevelop Software
 * eugenydzh@jdevelop.com
 */
package com.jdevelop.sim.gui.utils;

import com.jdevelop.sim.connectors.AbstractConnector;
import com.jdevelop.sim.connectors.SocketConnector;
import com.jdevelop.sim.connectors.URLConnector;
import com.jdevelop.sim.events.AbstractEvent;

public class Connector {

    private String protocol, host, wrapper;

    private int port, serverPort;

    public Connector(String protocol, String host, int port, String serverPort,
            String wrapper) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        try {
            this.serverPort = Integer.parseInt(serverPort);
        } catch (NumberFormatException e) {
            this.serverPort = -1;
        }
        this.wrapper = wrapper;
    }

    public AbstractEvent getObjectData(AbstractEvent evt) {
        AbstractConnector connector = null;

        if (wrapper == null) {
            connector = new SocketConnector(host, serverPort);
        } else {
            connector = new URLConnector(this.protocol, this.host, this.port,
                    wrapper);
        }
        return connector.getResponse(evt);
    }

}