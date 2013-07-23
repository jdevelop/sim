package com.jdevelop.sim.gui.utils;

import java.awt.Rectangle;

import java.util.Properties;


public class RuntimeProperties {
    private static Properties runtime;

    public RuntimeProperties( Properties newRuntime ) {
        runtime = newRuntime;
    }

    public int getInt( String name ) {
        return Integer.parseInt( runtime.getProperty( name, "0" ) );
    }
    
    public int getInt( String name, String defaultValue ) {
        return Integer.parseInt( runtime.getProperty( name, defaultValue ) );
    }

    public String getString( String name ) {
        return runtime.getProperty( name, "" );
    }

    public Rectangle getLocation( String name ) {
        return new Rectangle( getInt( name + ".X" ),
                              getInt( name + ".Y" ),
                              getInt( name + ".W" ),
                              getInt( name + ".H" )
                              );
    }
}
