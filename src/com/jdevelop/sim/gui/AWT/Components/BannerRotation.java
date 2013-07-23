package com.jdevelop.sim.gui.AWT.Components;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Event;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class BannerRotation
    extends Canvas
{
    Image img;
    URL url;
    URL targetURL;
    int timeout;
    Color bgcolor;
    Applet parent;
	boolean isRunning = true;

    public BannerRotation( URL url,
                           int timeout,
                           Color background,
                           Applet app
                           ) {
        this.url = url;
        this.timeout = timeout;
        this.bgcolor = background;
        this.parent = app;
    }

    public void paint( Graphics g ) {
        Dimension d = getSize(  );
        if ( img == null ) {
            g.setColor( bgcolor );
            g.fillRect( 0, 0, d.width, d.height );
        } else {
            g.drawImage( img, 0, 0, d.width, d.height, this );
        }
    }
    
    public void initThread() {
    	refresher r = new refresher();
    	r.start();
    }
    
    public void stopThread() {
    	isRunning = false;
    }

	public boolean handleEvent(Event e) {
		switch (e.id) {
			case Event.MOUSE_DOWN: 
				if (targetURL != null) {
					parent.getAppletContext().showDocument(targetURL,"_blank");
				}
				return true;
			default:
		}
		return super.handleEvent(e);
	}

    class refresher
        extends Thread
    {
        public void run(  ) {
            while ( isRunning ) {
                try {
                    URLConnection conn = url.openConnection(  );
                    conn.setDoOutput( false );
                    BufferedReader br =
                        new BufferedReader( new InputStreamReader( conn.getInputStream(  ) ) );
                    targetURL = new URL( br.readLine(  ) );
                    img = parent.getImage( new URL( br.readLine(  ) ) );
                    repaint(  );
                    br.close(  );
                    Thread.sleep( timeout * 1000 );
                } catch ( Exception e ) {
                    System.out.println( "Exception in banner thread " +
                                        e.toString(  ) + " occured "
                                        );
                    isRunning = false;
                }
            }
        }
    }
}
