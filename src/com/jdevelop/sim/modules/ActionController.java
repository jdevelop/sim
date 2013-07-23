package com.jdevelop.sim.modules;

import com.jdevelop.sim.events.AbstractEvent;
import com.jdevelop.sim.events.AddMessageEvent;
import com.jdevelop.sim.events.DeclineEvent;
import com.jdevelop.sim.events.IMSessionsListEvent;
import com.jdevelop.sim.events.IgnoreEvent;
import com.jdevelop.sim.events.LoginEvent;
import com.jdevelop.sim.events.LogoutEvent;
import com.jdevelop.sim.events.MessageListEvent;
import com.jdevelop.sim.events.UsersListEvent;


public class ActionController {
    private mainChat chat;

    public ActionController( mainChat chat ) {
        this.chat = chat;
    }

    public AbstractEvent processEvent( AbstractEvent evt, String IP )
                               throws Exception
    {
        int message = evt.getMessageCode(  );
        switch ( message ) {
            case LoginEvent.EVENT_NAME : // '\0'
             {
                LoginEvent logevt = ( LoginEvent ) evt;

                return chat.loginUser( logevt,
                                       IP
                                       );
            }
            case UsersListEvent.EVENT_NAME : // '\002'
                return chat.getOnlineList( evt.getKey(  ) );
            case IMSessionsListEvent.EVENT_NAME : // '\001'
                return chat.getIMSessions( evt.getKey(  ) );
            case AddMessageEvent.EVENT_NAME : // '\003'
             {
                chat.addMessage( evt.getKey(  ),
                                 ( ( AddMessageEvent ) evt ).getMessage(  )
                                 );

                return null;
            }
            case MessageListEvent.EVENT_NAME : // '\004'
             {
                MessageListEvent mle = ( MessageListEvent ) evt;
                return chat.getMessageList( (String) mle.getKey(  ),
                                            (String) mle.getTarget(  )
                                            );
            }
            case DeclineEvent.EVENT_NAME : {
                DeclineEvent decl = ( DeclineEvent ) evt;
                Message msg = decl.getMessage(  );
                chat.addMessage( decl.getKey(  ),
                                 msg
                                 );
                chat.removeRequest( decl.getKey(  ),
                                    msg.privat
                                    );

                return null;
            }
            case IgnoreEvent.EVENT_NAME : {
                IgnoreEvent ign = ( IgnoreEvent ) evt;
                chat.ignoreUser( (String) ign.getKey(  ),
                                 (String) ign.getTarget(  )
                                 );
                chat.removeRequest( (String) ign.getKey(  ),
                                    (String) ign.getTarget(  )
                                    );

                return null;
            }
            case LogoutEvent.EVENT_NAME : {
                chat.removeOnlineUser( evt.getKey(  ) );

                return null;
            }
        }
        return null;
    }
}
