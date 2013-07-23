package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;


public class SmileIcon
    extends AbstractPopupWindowElement
{
    protected Image image;

    public SmileIcon( Image newImage ) {
        image = newImage;
        size = new Dimension( image.getWidth( this ),
                              image.getHeight( this )
                              );
    }

    public Image getImage(  ) {
        return image;
    }

    public void setImage( Image image ) {
        this.image = image;
    }

    public Dimension preferredSize(  ) {
        return size;
    }

    public Dimension minimumSize(  ) {
        return preferredSize(  );
    }

    public void paint( Graphics g ) {
        g.drawImage( image, x, y, this );
    }

    public void paint( Graphics g, int x, int y ) {
        this.x = x;
        this.y = y;
        g.drawImage( image, x, y, this );
    }
}
