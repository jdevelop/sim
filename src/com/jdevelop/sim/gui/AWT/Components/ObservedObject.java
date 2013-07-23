package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Event;


public interface ObservedObject {
    public void addListener( EventObserver observer );

    public void removeListener( EventObserver observer );

    public void notifyListeners( Event e );
}
