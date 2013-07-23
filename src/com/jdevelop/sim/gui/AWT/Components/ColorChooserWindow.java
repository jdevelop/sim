package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;


public class ColorChooserWindow
    extends AbstractCustomCanvas
{
    protected ImageShowDialog dialog;

    public ColorChooserWindow( AbstractPopupWindowElement[] newItems ) {
        super( newItems );
    }

    public Dimension preferredSize(  ) {
        return size;
    }

    public Dimension minimumSize(  ) {
        return preferredSize(  );
    }

    public void paint( Graphics g ) {
        if ( items[ selectedItem ] != null ) {
            g.setColor( ( ( ColorItem ) items[ selectedItem ] ).getColor(  ) );

            Dimension localSize = size(  );
            g.fillRect( 0, 0, localSize.width, localSize.height );
        }
    }

    public Color getColor(  ) {
        if ( ( items.length <= selectedItem ) || ( items[ selectedItem ] == null ) ) {
            return Color.black;
        }

        return ( ( ColorItem ) items[ selectedItem ] ).getColor(  );
    }

    public boolean handleEvent( Event e ) {
        switch ( e.id ) {
            case Event.MOUSE_UP :
                if ( dialog == null ) {
                    displayContents(  );
                }
                dialog.pack(  );
                dialog.requestFocus(  );
                dialog.show(  );

                return true;
        }

        return super.handleEvent( e );
    }

    void displayContents(  ) {
        Point p = getLocationOnScreen(  );
        dialog = new ImageShowDialog( p.x, p.y + items[ 0 ].getHeight(  ) );
    }

    void updateContent(  ) {
        repaint(  );
    }

    class ImageShowDialog
        extends Window
    {
        public ImageShowDialog( int x, int y ) {
            super( new Frame(  ) );
            setLayout( new GridLayout( items.length, 1 ) );
            setLocation( x, y );

            for ( int i = 0; i < items.length; i++ )
                add( items[ i ] );
        }

        public boolean handleEvent( Event e ) {
            switch ( e.id ) {
                default :
                    break;
                case Event.MOUSE_DOWN :
                    if ( e.target == this ) {
                        ColorItem item = ( ColorItem ) getComponentAt( e.x, e.y );
                        setSelected( item );
                        updateContent(  );
                        dispose(  );
                        dialog = null;
                        e.id = Event.ACTION_EVENT;
                        e.arg = "color";
                        notifyListeners( e );
                    }
                    break;
                case Event.MOUSE_EXIT :
                    if ( e.target == this ) {
                        dispose(  );
                        dialog = null;
                    }
                    break;
                case Event.WINDOW_DESTROY :
                    dispose(  );
                    dialog = null;

                    break;
            }

            return super.handleEvent( e );
        }
    }
}
