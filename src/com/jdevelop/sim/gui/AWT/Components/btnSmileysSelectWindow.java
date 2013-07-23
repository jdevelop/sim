package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Graphics;
import java.awt.Image;


public class btnSmileysSelectWindow
    extends SmilesSelectWindow
{
    private Image button;

    public btnSmileysSelectWindow( AbstractPopupWindowElement[] items, int rows ) {
        super( items, rows );
    }

    public Image getButton(  ) {
        return button;
    }

    public void setButton( Image button ) {
        this.button = button;
    }

    public void paint( Graphics g ) {
        g.drawImage( button, 0, 0, this );
    }
}
