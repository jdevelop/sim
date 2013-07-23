package com.jdevelop.sim.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

import java.util.Vector;


public class loadTestMain {
    public static void main( String[] args )
                     throws Exception
    {
        BufferedReader br = new BufferedReader( new FileReader( args[ 0 ] ) );
        String line;
        Vector threads = new Vector(  );
        String host = args[ 1 ];
        int port = Integer.parseInt( args[ 2 ] );
        while ( ( line = br.readLine(  ) ) != null ) {
            String login = line.substring( 0,
                                           line.indexOf( ':' )
                                           );
            String password = line.substring( line.indexOf( ':' ) + 1 );
            threads.add( new loadTestThread( login, password, host, port ) );
        }
        System.out.println( "starting " + threads.size(  ) + " threads..." );
        for ( int i = 0; i < threads.size(  ); i++ ) {
            ( ( Thread ) threads.elementAt( i ) ).start(  );
        }
        System.out.println( "Awaiting input...." );
        br = new BufferedReader( new InputStreamReader( System.in ) );
        br.readLine(  );
    }
}
