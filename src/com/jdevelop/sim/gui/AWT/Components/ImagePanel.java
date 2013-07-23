package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;


public class ImagePanel
    extends Panel
{
    private Image bgImage;
    private Dimension size;
    private Color bgColor;

    public ImagePanel( Image image ) {
        bgImage = image;
        size = new Dimension( image.getWidth( this ),
                              image.getHeight( this )
                              );
    }
    
    public ImagePanel(Image image, Color background) {
    	this(image);
    	this.bgColor = background;
    }

    public ImagePanel( Color color, Dimension newSize ) {
        bgImage = Toolkit.getDefaultToolkit(  ).createImage( new byte[ newSize.width * newSize.height ] );
        bgImage.getGraphics(  ).setColor( color );
        bgImage.getGraphics(  ).fillRect( 0, 0, newSize.width, newSize.height );
        size = newSize;
    }

    public void paintThis( Graphics g ) {
        if ( bgImage != null ) {
        	if (bgColor != null) {
        		g.setColor(bgColor);
        		g.fillRect(0,0,size.width,size.height);
        	}
            g.drawImage( bgImage, 0, 0, this );
        }
    }

	public void paint( Graphics g ) {
		System.out.println("Painting");
		paintThis(g);
		super.paint(g);
	}

    public Dimension preferredSize(  ) {
        return minimumSize(  );
    }

    public Dimension minimumSize(  ) {
        return size;
    }
}
