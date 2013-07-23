package com.jdevelop.sim.gui.AWT.Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Panel;
import java.awt.Scrollbar;


public class ScrollView
    extends Panel
{
    static boolean WORKAROUND_ENABLED = ( new Scrollbar( 0, 20, 10, 0, 20 ) ).getValue(  ) == 20;
    TextView _view;
    Scrollbar _scrollbar;

    public ScrollView( TextView view ) {
        _view = view;
        _scrollbar = new Scrollbar( 1 );
        _scrollbar.setLineIncrement( 10 );
        _scrollbar.setBackground( Color.lightGray );
        setLayout( new BorderLayout(  ) );
        add( "Center", view );
        add( "East", _scrollbar );
        view.setScrollView( this );
    }

    public void scrollToBottom(  ) {
        int max = _scrollbar.getMaximum(  );
        _scrollbar.setValue( max );
        scroll(  );
    }

    public void boundsChanged(  ) {
        int viewableHeight = _view.size(  ).height;
        int fullHeight = _view.preferredSize(  ).height;

        if ( WORKAROUND_ENABLED ) {
            fullHeight -= viewableHeight;
        }

        _scrollbar.setValues( _scrollbar.getValue(  ),
                              viewableHeight,
                              0,
                              fullHeight
                              );
        scroll(  );
    }

    void scroll(  ) {
        _view.setYTranslation( _scrollbar.getValue(  ) );
        _view.repaint(  );
    }

    public boolean handleEvent( Event event ) {
        if ( event.target == _scrollbar ) {
            scroll(  );

            return true;
        } else {
            return super.handleEvent( event );
        }
    }
}
