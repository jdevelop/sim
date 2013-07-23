package com.jdevelop.sim.gui.AWT.Components;

import com.jdevelop.sim.gui.AWT.MessengerWindow;

import java.awt.Event;
import java.awt.TextArea;
import java.awt.event.KeyEvent;


public class KeyTextArea
    extends TextArea
{
    private MessengerWindow parent;
    private boolean isControlPressed = false;

    public KeyTextArea( MessengerWindow parent, String s, int i1, int i2, int i3 ) {
        super( s, i1, i2, i3 );
        this.parent = parent;
    }

    public boolean handleEvent( Event e ) {
        if ( ( e.modifiers & ( Event.CTRL_MASK | Event.SHIFT_MASK ) ) > 0 ) {
            isControlPressed = true;
        } else {
            isControlPressed = false;
        }

        if ( ( e.id == Event.KEY_PRESS ) && ( e.key == KeyEvent.VK_ENTER ) ) {
            if ( !isControlPressed ) {
                parent.addMessage(  );
                isControlPressed = false;
		return true;
            } else {
                int pos = getCaretPosition(  );
                insert( "\n", pos );
            }
        }
        return super.handleEvent( e );
    }
}
