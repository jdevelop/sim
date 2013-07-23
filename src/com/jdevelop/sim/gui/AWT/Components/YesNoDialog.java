package com.jdevelop.sim.gui.AWT.Components;

import com.jdevelop.sim.gui.AWT.Main;
import com.jdevelop.sim.gui.utils.RuntimeProperties;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;


public class YesNoDialog
    extends Dialog
{
    public static final int YES = 0;
    public static final int NO = 1;
    int status = -1;
    ImageButton btnYes;
    ImageButton btnNo;
	ImageButton logo;

    public YesNoDialog( Frame frame, String user, RuntimeProperties runtime, Main list ) {
        super( frame,
               runtime.getString( "YesNoWindow.Title" ), true
               );
        setBackground( Color.decode( runtime.getString( "Global.bgcolor" ) ) );
        btnYes = new ImageButton( list.getImage( runtime.getString( "YesNoWindow.btnYes" ) ),
                                  "Yes"
                                  );
        btnNo = new ImageButton( list.getImage( runtime.getString( "YesNoWindow.btnNo" ) ),
                                 "No"
                                 );
		logo = new ImageButton( list.getImage( runtime.getString( "YesNoWindow.logo" ) ),
								"profile"
								);

        Image img = list.getImage( runtime.getString( "YesNoWindow.bgImage" ) );
        Rectangle rect = runtime.getLocation( "YesNoWindow" );
        ImagePanel backpanel = ( img != null ) ? new ImagePanel( img )
                               : new ImagePanel( Color.decode(runtime.getString("Global.bgcolor")),
                                                 new Dimension( rect.width, rect.height )
                                                 );
        backpanel.setLayout( null );
        backpanel.add( btnYes );
        backpanel.add( btnNo );
		backpanel.add( logo );
        btnYes.setBounds( runtime.getLocation( "YesNoWindow.btnYes" ) );
        btnNo.setBounds( runtime.getLocation( "YesNoWindow.btnNo" ) );
		logo.setBounds( runtime.getLocation( "YesNoWindow.logo" ) );
        add( backpanel, "Center" );
    }

    public boolean handleEvent( Event e ) {
        if ( "Yes".equals( e.arg ) ) {
            status = YES;
            dispose(  );
        } else if ( "No".equals( e.arg ) ) {
            status = NO;
            dispose(  );
        }

        return super.handleEvent( e );
    }

    public int showDialog(  ) {
        show(  );

        return status;
    }
}
