package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Panel;

import java.util.Vector;


public abstract class AbstractCustomCanvas
    extends Panel
    implements ObservedObject
{
    protected int selectedItem = 0;
    protected int imagesPerRow = 1;
    protected Vector observersList;
    protected AbstractPopupWindowElement[] items;
    protected Dimension size;

    public AbstractCustomCanvas(  ) {
        observersList = new Vector(  );
    }

    public AbstractCustomCanvas( AbstractPopupWindowElement[] newItems ) {
        this( newItems, 1 );
    }

    public AbstractCustomCanvas( AbstractPopupWindowElement[] newItems, int imagePerRow ) {
        imagesPerRow = imagePerRow;
        observersList = new Vector(  );
        items = newItems;
        calculateSize(  );
    }

    protected void calculateSize(  ) {
        int tmpHeight = 0;
        int tmpWidth = 0;

        for ( int i = 0; i < items.length; i++ ) {
	    if (items[i] != null) {
		if ( tmpWidth < items[ i ].getWidth(  ) ) {
		    tmpWidth = items[ i ].getWidth(  );
		}

		if ( tmpHeight < items[ i ].getHeight(  ) ) {
		    tmpHeight = items[ i ].getHeight(  );
		}
	    }
        }

        tmpHeight += 3;
        this.size = new Dimension( ( ( imagesPerRow < items.length ) ? imagesPerRow
                                     : items.length ) * tmpWidth, ( ( items.length / imagesPerRow ) + ( items.length % imagesPerRow ) ) * tmpHeight
                                   );
    }

    public void addListener( EventObserver observer ) {
        if ( observersList.indexOf( observer ) == -1 ) {
            observersList.addElement( observer );
        }
    }

    public void removeListener( EventObserver observer ) {
        observersList.removeElement( observer );
    }

    public void notifyListeners( Event e ) {
        for ( int i = 0; i < observersList.size(  ); i++ ) {
            ( ( EventObserver ) observersList.elementAt( i ) ).acceptEvent( e );
        }
    }

    public void setSelected( AbstractPopupWindowElement item ) {
        for ( int i = 0; i < items.length; i++ ) {
            if ( items[ i ] != item ) {
                continue;
            }

            selectedItem = i;

            break;
        }

        repaint(  );
        ;
    }

    public Dimension preferredSize(  ) {
        return size;
    }

    public Dimension minimumSize(  ) {
        return preferredSize(  );
    }
}
