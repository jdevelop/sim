package com.jdevelop.sim.test;

import com.jdevelop.sim.connectors.SocketConnector;
import com.jdevelop.sim.events.AbstractEvent;
import com.jdevelop.sim.events.IMSessionsListEvent;
import com.jdevelop.sim.events.LoginEvent;
import com.jdevelop.sim.events.LoginEventResponse;
import com.jdevelop.sim.events.MessageListEvent;
import com.jdevelop.sim.events.UsersListEvent;


public class loadTestThread
    extends Thread
{
    protected String key = "";
    protected String host;
    protected int port;
    protected String login;
    protected String password;

    public loadTestThread( String login, String password, String host, int port ) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public void run(  ) {
        LoginEventResponse ler = ( LoginEventResponse ) sendRequest( new LoginEvent( login, password ) );
        if ( ler != null && ler.getKey(  ) != null && !"".equals( ler.getKey(  ) ) ) {
            this.key = ler.getKey(  );
        } else {
            System.out.println( "Login failed for user " + login );
            return;
        }
        while ( true ) {
            try {
                sendRequest( new UsersListEvent( key ) );
                Thread.sleep( 5000 );
                sendRequest( new IMSessionsListEvent( key ) );
                //sendRequest(new MessageListEvent())
            } catch ( Exception e ) {
                System.out.println( "Got exception " + e.toString(  ) );
            } finally {
                try {
                    Thread.sleep( Math.round( Math.random(  ) * 2000 ) );
                } catch ( Exception e ) {
                    System.out.println( "interrupted" );
                }
            }
        }
    }

    protected AbstractEvent sendRequest( AbstractEvent evt ) {
        SocketConnector connector = null;
        connector = new SocketConnector( host, port );
        return connector.getResponse( evt );
    }
}
