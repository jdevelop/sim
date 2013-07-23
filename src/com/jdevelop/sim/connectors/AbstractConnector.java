package com.jdevelop.sim.connectors;

import com.jdevelop.sim.events.AbstractEvent;


public abstract class AbstractConnector {
    public AbstractConnector(  ) {
    }

    public abstract AbstractEvent getResponse( AbstractEvent abstractevent );
}
