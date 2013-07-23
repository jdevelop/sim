package com.jdevelop.sim.modules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


class Messages {
    private HashMap IMsessions;
    info myID;

    public Messages( info UID ) {
        IMsessions = new HashMap(  );
        myID = UID;
    }

    public synchronized void addMessage( String UID, Message msg ) {
        if ( ( myID != null ) && myID.getIgnoreList(  ).contains( UID ) ) {
            return;
        }

        if ( IMsessions.containsKey( UID ) ) {
            LinkedList messages = ( LinkedList ) IMsessions.get( UID );

            if ( messages == null ) {
                messages = new LinkedList(  );
                IMsessions.put( UID, messages );
            }

            messages.addLast( msg );
        } else {
            LinkedList messages;

            if ( IMsessions.containsKey( UID ) ) {
                messages = ( LinkedList ) IMsessions.get( UID );
            } else {
                messages = new LinkedList(  );
                IMsessions.put( UID, messages );
            }

            messages.addLast( msg );
        }
    }

    public synchronized Message[] getMessages( String UID, HashSet ignoreList ) {
        LinkedList tmp = ( LinkedList ) IMsessions.get( UID );

        if ( tmp == null ) {
            tmp = new LinkedList(  );
        }

        int i = 0;

        while ( i < tmp.size(  ) ) {
            Message tst = ( Message ) tmp.get( i );

            if ( ignoreList.contains( tst.getUID(  ) ) ) {
                tmp.remove( i );
            } else {
                i++;
            }
        }

        IMsessions.remove( UID );

        Message[] msgarr = new Message[ tmp.size(  ) ];

        for ( i = 0; i < msgarr.length; i++ )
            msgarr[ i ] = ( Message ) tmp.get( i );

        return msgarr;
    }

    public HashSet getIMSessions( HashSet ignoreList ) {
        HashSet ret = new HashSet( IMsessions.keySet(  ) );
        ret.removeAll( ignoreList );

        return ret;
    }
}
