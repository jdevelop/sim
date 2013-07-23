package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;


public class ImageButton
    extends Canvas
{
    private Image image1;
    private Image image2;
    private Image currentImage;
    private String command;
    private boolean selected;
    private Dimension size;
    protected boolean isMouseSelected = false;
    protected boolean actOnMouse = true;

    public ImageButton( Image img1, Image img2, String command ) {
        selected = false;
        image1 = img1;
        image2 = img2;
        currentImage = img1;
        this.command = command;

        if ( currentImage == null ) {
            size = new Dimension( 0, 0 );
        } else {
            size = new Dimension( currentImage.getWidth( this ),
                                  currentImage.getHeight( this )
                                  );
        }
    }

    public ImageButton( Image img1, String command ) {
        selected = false;
        currentImage = image1 = image2 = img1;
        this.command = command;

        if ( currentImage == null ) {
            size = new Dimension( 0, 0 );
        } else {
            size = new Dimension( currentImage.getWidth( this ),
                                  currentImage.getHeight( this )
                                  );
        }
    }

    public Dimension preferredSize(  ) {
        return minimumSize(  );
    }

    public Dimension minimumSize(  ) {
        return size;
    }

    public void update( Graphics g ) {
        paint( g );
    }

    public void paint( Graphics g ) {
        g.setPaintMode(  );
        g.drawImage( currentImage, 0, 0, this );
    }

    public boolean handleEvent( Event e ) {
        if ( actOnMouse ) {
            if ( e.id == Event.MOUSE_ENTER && !isMouseSelected ) {
                setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
                isMouseSelected = true;
            } else if ( e.id == Event.MOUSE_EXIT && isMouseSelected ) {
                setCursor( Cursor.getDefaultCursor(  ) );
                isMouseSelected = false;
            }
        }
        if ( ( e.id == 502 ) && selected && actOnMouse) {
            e.id = 1001;
            if ( currentImage == image1 ) {
                currentImage = image2;
               	e.arg = command;
            } else if ( currentImage == image2 ) {
                currentImage = image1;
                e.arg = "de" + command;
            }
            repaint(  );
        }

        if ( e.id == 504 ) {
            selected = true;
            repaint(  );

            return true;
        }

        if ( e.id == 505 ) {
            selected = false;
            repaint(  );

            return true;
        } else {
            return super.handleEvent( e );
        }
    }

    public boolean isSelected(  ) {
        return currentImage != image1;
    }

    public boolean isActOnMouse(  ) {
        return actOnMouse;
    }

    public void setActOnMouse( boolean b ) {
        actOnMouse = b;
    }
}