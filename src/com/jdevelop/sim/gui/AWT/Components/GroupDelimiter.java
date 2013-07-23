/*
 * Created on 12.05.2005 
 * @author Eugeny N. Dzhurinsky, 
 * JDevelop Software
 * eugenydzh@jdevelop.com
 */
package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Dimension;
import java.awt.Graphics;

public class GroupDelimiter extends AbstractPopupWindowElement {

    public GroupDelimiter() {
        size = new Dimension(100, 10);
    }

    public void paint(Graphics g, int x, int y) {
        g.drawLine(x, y, x + 100, y);
    }

}