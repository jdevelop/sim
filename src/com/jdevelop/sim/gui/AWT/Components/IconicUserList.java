package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;


public class IconicUserList
    extends AbstractCustomCanvas
{
    protected Color color;
    protected boolean isMouseSelected = false;
    protected Image background;
    protected int paddingX = 0;
    protected int paddingY = 0;

    public IconicUserList( AbstractPopupWindowElement[] items ) {
        super( items );
    }

    public IconicUserList( AbstractPopupWindowElement[] items, int cols ) {
        super( items, cols );
    }
    
    public IconicUserList ( AbstractPopupWindowElement[] items, int cols, Image bg ) {
    	this(items,cols);
    	background = bg;
    }
    
    public void setPadding(int px, int py) {
    	this.paddingX = px;
    	this.paddingY = py;
    }

    public void paint( Graphics g ) {
        if ( color != null ) {
            setColor( color );
            g.fillRect( 0, 0, size.width, size.height );
        }
        
        if (background != null) {
        	g.drawImage(background,0,0,this);
        }

        if ( items.length == 0 ) {
            return;
        }

        items[ 0 ].paint( g, paddingX, paddingY );

        for ( int i = 1; i < items.length; i++ ) {
	    if (items[ i ] != null) {
		if ( ( i % imagesPerRow ) == 0 ) {
		    items[ i ].paint( g, paddingX, paddingY + ( i / imagesPerRow * items[ i - imagesPerRow ].getHeight(  ) ) + 3 );
		} else {
		    items[ i ].paint( g, ( size.width / imagesPerRow ) + 10, i / imagesPerRow * items[ ( ( i - imagesPerRow ) > 0 ) ? ( i - imagesPerRow )
													   : 1 ].getHeight(  )
				      );
		}
	    }
        }

        return;
    }

    public boolean handleEvent( Event e ) {
        switch ( e.id ) {
            case Event.MOUSE_MOVE :
                if ( getListElement( e ) != null ) {
                    if ( !isMouseSelected ) {
                        setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
                        isMouseSelected = true;
                    }
                } else {
                    if ( isMouseSelected ) {
                        setCursor( Cursor.getDefaultCursor(  ) );
                        isMouseSelected = false;
                    }
                }
                break;
            case Event.MOUSE_DOWN :
                AbstractPopupWindowElement element = getListElement( e );
                if ( element != null ) {
                    e.id = Event.ACTION_EVENT;
                    e.target = element;
                    notifyListeners( e );
                }
        }

        return true;
    }

    protected AbstractPopupWindowElement getListElement( Event e ) {
        for ( int i = 0; i < items.length; i++ ) {
            if ( items[ i ].containsPoint( e.x, e.y ) ) {
                return items[ i ];
            }
        }

        return null;
    }

    public void setItems( AbstractPopupWindowElement[] newItems ) {
        this.items = newItems;
        calculateSize(  );
        repaint(  );
    }

    public Color getColor(  ) {
        return color;
    }

    public void setColor( Color color ) {
        this.color = color;
    }
}
