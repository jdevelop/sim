package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;


public class ColorItem
    extends AbstractPopupWindowElement
{
    protected Color color;
    protected int width;
    protected int height;

    public ColorItem( Color color, int width, int height ) {
        this.color = color;
        this.width = width;
        this.height = height;
        size = new Dimension( width, height );
    }

    public void paint( Graphics g, int x, int y ) {
        g.drawRect( x, y, width, height );
        g.setColor( color );
        g.fillRect( x, y, width, height );
    }

    public void paint( Graphics g ) {
        paint( g, 0, 0 );
    }

    public Dimension minimumSize(  ) {
        return size;
    }

    public Dimension preferredSize(  ) {
        return size;
    }

    public Color getColor(  ) {
        return color;
    }
}
