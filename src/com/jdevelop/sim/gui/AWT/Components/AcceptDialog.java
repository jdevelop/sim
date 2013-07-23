package com.jdevelop.sim.gui.AWT.Components;

import com.jdevelop.sim.gui.AWT.Main;
import com.jdevelop.sim.gui.utils.RuntimeProperties;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.Frame;
import java.awt.Image;

import java.net.MalformedURLException;
import java.net.URL;

public class AcceptDialog
    extends Dialog
{
    public static final int ACCEPT = 0;
    public static final int DECLINE = 1;
    public static final int IGNORE = 2;
    int status = -1;
    ImageButton btnAccept;
    ImageButton btnDecline;
    ImageButton btnIgnore;
    ImageButton btnProfile;
    LightLabel message;
    Frame frame;
    String user;
    Main list;
    RuntimeProperties runtime;

    public AcceptDialog( Frame frame, String user, RuntimeProperties runtime, Main list ) {
        super( frame,
               runtime.getString( "AcceptWindow.Title" ), true
               );
        this.frame = frame;
        this.user = user;
        this.runtime = runtime;
        this.list = list;
        setBackground( Color.decode( runtime.getString( "Global.bgcolor" ) ) );
        btnAccept = new ImageButton( list.getImage( runtime.getString( "AcceptWindow.btnAccept" ) ),
                                     "Accept"
                                     );
        btnDecline = new ImageButton( list.getImage( runtime.getString( "AcceptWindow.btnDecline" ) ),
                                      "Decline"
                                      );
        btnIgnore = new ImageButton( list.getImage( runtime.getString( "AcceptWindow.btnIgnore" ) ),
                                     "Ignore"
                                     );
        Image profileImage = list.getImage( runtime.getString( "AcceptWindow.btnProfile" ) );


        btnProfile = new ImageButton( profileImage, "Profile" );
        message = new LightLabel( Color.decode( runtime.getString( "AcceptWindow.fontcolor" ) ),
                                  new Font( "Times",
                                            Font.PLAIN,
                                            runtime.getInt( "AcceptWindow.fontsize" )
                                            )
                                  );
        message.setText( runtime.getString( "AcceptWindow.message" ) + user );

        Image img = list.getImage( runtime.getString( "AcceptWindow.bgImage" ) );
        ImagePanel backpanel = ( img != null ) ? new ImagePanel( img )
                               : new ImagePanel( Color.decode(runtime.getString("Global.bgcolor")),
                                                 new Dimension( 
                                                                runtime.getInt( "AcceptWindow.W" ),
                                                                runtime.getInt( "AcceptWindow.H" )
                                                                )
                                                 );
        backpanel.setLayout( null );
        backpanel.add( message );
        backpanel.add( btnAccept );
        backpanel.add( btnDecline );
        backpanel.add( btnIgnore );
        backpanel.add( btnProfile );
        btnAccept.setBounds( runtime.getLocation( "AcceptWindow.btnAccept" ) );
        btnDecline.setBounds( runtime.getLocation( "AcceptWindow.btnDecline" ) );
        btnIgnore.setBounds( runtime.getLocation( "AcceptWindow.btnIgnore" ) );
		Rectangle msgBounds = runtime.getLocation("AcceptWindow.message");
		FontMetrics _fm = Toolkit.getDefaultToolkit().getFontMetrics(message.getFont());
		int x=(runtime.getLocation("AcceptWindow").width-_fm.stringWidth(message.getMessage()))/2;
		msgBounds.x=x;
        message.setBounds( msgBounds );
		btnProfile.setBounds( runtime.getLocation( "AcceptWindow.btnProfile" ) );
        add( backpanel, "Center" );
    }

    public boolean handleEvent( Event e ) {
        if ( e.id == Event.WINDOW_DESTROY ) {
            e.arg = "Decline";
        }

        if ( "Accept".equals( e.arg ) ) {
            status = ACCEPT;
            dispose(  );
        } else if ( "Decline".equals( e.arg ) ) {
            status = DECLINE;
            dispose(  );
        } else if ( "Ignore".equals( e.arg ) ) {
            YesNoDialog dlg = new YesNoDialog( frame, user, runtime, list );
            dlg.setBounds( runtime.getLocation( "YesNoWindow" ) );

            int localStatus = dlg.showDialog(  );

            if ( localStatus == YesNoDialog.YES ) {
                status = IGNORE;
            } else {
                status = -1;
            }

            dispose(  );
        } else if ( "Profile".equals( e.arg ) ) {
            try {
                list.getAppletContext(  ).showDocument( new URL( runtime.getString( "profileURL" ) + user ),
                                                        runtime.getString( "profileURLTarget" )
                                                        );
            } catch ( MalformedURLException e1 ) {
            }

            dispose(  );
        }

        return super.handleEvent( e );
    }

    public int showDialog(  ) {
        show(  );

        return status;
    }
}
