// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst
// Source File Name:   LightLabel.java
package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;


public class LightLabel
    extends Component
{
    private String text;
    private Color color;
    private Font font;

    public LightLabel( Color color, Font font ) {
        text = "";
        this.color = color;
        this.font = font;
    }

    public void paint( Graphics g ) {
        g.setFont( font );
        g.setColor( color );

        FontMetrics metrix = getFontMetrics( font );
        g.drawString( text,
                      0,
                      metrix.getHeight(  )
                      );
    }

    public void setText( String text ) {
        this.text = text;
    }

	public Font getFont() {
		return font;
	}

	public String getMessage() {
		return text;
	}
}
