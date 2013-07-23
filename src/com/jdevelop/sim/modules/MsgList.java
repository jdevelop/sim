package com.jdevelop.sim.modules;

import java.util.HashMap;
import java.util.HashSet;


public class MsgList {
    HashMap messages;

    MsgList(  ) {
        messages = new HashMap(  );
    }

    public void addMessage( Message message, info m_from, info m_to ) {
        String priv = message.privat;
        String from = message.uid;
        Messages msgbase = ( Messages ) messages.get( priv );

        if ( msgbase == null ) {
            msgbase = new Messages( m_from );
            messages.put( priv, msgbase );
        }

        msgbase.addMessage( from, message );
        msgbase = ( Messages ) messages.get( from );

        if ( msgbase == null ) {
            msgbase = new Messages( m_to );
            messages.put( from, msgbase );
        }
    }

    public synchronized HashSet getIMSessions( info user ) {
        HashSet ret = new HashSet(  );
        Messages msgbase = ( Messages ) messages.get( user.ID );

        if ( msgbase != null ) {
            ret = msgbase.getIMSessions( user.getIgnoreList(  ) );
        }

        return ret;
    }

    public synchronized Message[] getPrivate( info inf, String idto ) {
        String from = inf.ID;
        String to = idto;
        Messages msgbase = ( Messages ) messages.get( from );

        if ( msgbase != null ) {
            Message[] msgslist = msgbase.getMessages( to,
                                                      inf.getIgnoreList(  )
                                                      );

            return msgslist;
        } else {
            return new Message[ 0 ];
        }
    }


    public void removeUser( String UID ) {
        messages.remove( UID );
    }
}
