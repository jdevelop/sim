package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;


public interface DrawableComponentInterface {
    Rectangle getBoundingRect(  );

    boolean contains( int x, int y, int yTranslation );

    void draw( Graphics g, int yOff );

    void prepare( int xMax, Dimension preferredSize, Point p );
}
