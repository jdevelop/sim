package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;


public abstract class AbstractPopupWindowElement
    extends Component
{
    protected Dimension size;
    protected int x;
    protected int y;

    public abstract void paint( Graphics g, int x, int y );

    public int getWidth(  ) {
        return size.width;
    }

    public int getHeight(  ) {
        return size.height;
    }

    public boolean containsPoint( int x, int y ) {
        return false;
    }

    public void setLocation( int x, int y ) {
        this.x = x;
        this.y = y;
    }
}
