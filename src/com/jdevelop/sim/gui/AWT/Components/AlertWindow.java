package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Toolkit;


public class AlertWindow
    extends Dialog
{
    public AlertWindow( Frame frame, String name, String error, boolean isModal ) {
        super( frame, name, isModal );
        setLayout( new GridLayout( 2, 1 ) );
        add( new Label( error ) );
        add( new Button( "Close" ) );
		pack(  );
		Dimension screensize = Toolkit.getDefaultToolkit(  ).getScreenSize(  );
		Rectangle rect = getBounds(  );
		setBounds( ( screensize.width - rect.width ) / 2, ( screensize.height - rect.height ) / 2, rect.width, rect.height );

    }

    public boolean handleEvent( Event e ) {
        switch ( e.id ) {
            case Event.ACTION_EVENT :
            case Event.WINDOW_DESTROY :
                dispose(  );
                return true;
        }

        return super.handleEvent( e );
    }
}
