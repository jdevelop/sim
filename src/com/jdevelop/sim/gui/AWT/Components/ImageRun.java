package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;


public class ImageRun
    implements DrawableComponentInterface
{
    protected Image image;
    protected Rectangle boundingRect;
    protected boolean isLast;

    public ImageRun( Image newImage ) {
        image = newImage;
        boundingRect = new Rectangle( 0,
                                      0,
                                      image.getWidth( null ),
                                      image.getHeight( null )
                                      );
    }

    public ImageRun( Image newImage, boolean isLast ) {
        this( newImage );
        this.isLast = isLast;
    }

    public Rectangle getBoundingRect(  ) {
        return boundingRect;
    }

    public boolean contains( int x, int y, int yTranslation ) {
        return false;
    }

    public void draw( Graphics g, int yOff ) {
        g.drawImage( image, boundingRect.x, boundingRect.y + yOff, boundingRect.width, boundingRect.height, null );
    }

    public void prepare( int xMax, Dimension preferredSize, Point p ) {
        boundingRect.x = p.x;
        boundingRect.y = p.y;
        preferredSize.width = boundingRect.width;
        preferredSize.height = boundingRect.height;
        p.x += boundingRect.width;

        if ( isLast ) {
            p.y += boundingRect.height;
        }
    }
}
