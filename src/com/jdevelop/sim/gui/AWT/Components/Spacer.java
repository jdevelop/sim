package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;


public class Spacer
    extends Component
{
    protected Dimension size;
    protected Color color;

    public Spacer( int width, int height ) {
        this.size = new Dimension( width, height );
    }
    
    public Spacer(int width, int height, String color) {
    	this(width,height);
    	try {
    		this.color = Color.decode(color);
    	} catch (Exception e) {
    	}
    }

    public Dimension preferredSize(  ) {
        return size;
    }

    public Dimension minimumSize(  ) {
        return preferredSize(  );
    }
    
    public void paint(Graphics g) {
    	if (color != null) {
    		g.setColor(color);
    		g.fillRect(0,0,size.width,size.height);
    	}
    }
}
