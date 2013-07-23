package com.jdevelop.sim.server;

import com.jdevelop.sim.modules.mainChat;
import com.jdevelop.sim.modules.xmlrpc.XmlRpcChatModule;

import java.io.FileInputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Properties;


public class LiveSIMServer
    extends Thread
{
    protected int port;
    protected int maxConnections;
    protected Properties runtime;
    protected mainChat chat;

    public LiveSIMServer( int port, int connections, Properties runtime, mainChat chat ) {
        this.port = port;
        this.maxConnections = connections;
        this.runtime = runtime;
        this.chat = chat;
    }

    public static void main( String[] args )
                     throws Exception
    {
        int port = 8088;

        if ( args.length != 2 ) {
            System.out.println( "usage: <program> port conf_file" );

            return;
        }

        if ( args.length > 0 ) {
            try {
                port = Integer.parseInt( args[ 0 ] );
            } catch ( NumberFormatException numberformatexception ) {
            }
        }

        System.out.println( "Starting LiveSIMServer on port " + port );

        Properties runtime = new Properties(  );
        runtime.load( new FileInputStream( args[ 1 ] ) );

        mainChat chat = new mainChat( args[ 1 ] );
        LiveSIMServer server = new LiveSIMServer( port, 100, runtime, chat );
        server.setPriority( Thread.NORM_PRIORITY );
        server.start(  );
        if (runtime.getProperty("xml-rpc.port") != null) {
        	XmlRpcChatModule module = new XmlRpcChatModule(chat,runtime);
        	module.start();
        }
    }

    public void run(  ) {
        try {
            ServerSocket ss = new ServerSocket( port );
            while ( true ) {
                Socket s = ss.accept(  );
                serverThread thread = new serverThread( chat, s );
                thread.setPriority( Thread.MIN_PRIORITY );
                thread.start(  );
            }
        } catch ( IOException e ) {
            // TODO Auto-generated catch block, fill with desired value
        }
    }
}
