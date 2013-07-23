package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class SmilesParser {
    protected Hashtable smiles;
    protected TextStyle style;

    public SmilesParser( String smilesFile ) {
        smiles = new Hashtable(  );

        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader( getClass(  ).getResource( "/resources/" + smilesFile ).openStream(  ) ) );

            for ( String line; ( line = reader.readLine(  ) ) != null; ) {
                int split = line.indexOf( ' ' );

                if ( split > 0 ) {
                    String smileyCode = line.substring( 0, split );
                    String imgName = line.substring( split + 1 );
                    Image img = Toolkit.getDefaultToolkit(  ).getImage( getClass(  ).getResource( "/resources/" + imgName ) );
                    MediaTracker tracker = new MediaTracker( new Label(  ) );
                    tracker.addImage( img, 0 );

                    try {
                        tracker.waitForAll(  );
                    } catch ( InterruptedException e ) {
                        e.printStackTrace( System.out );
                    }

                    smiles.put( smileyCode, img );
                }
            }
        } catch ( java.io.IOException e ) {
            e.printStackTrace( System.out );
        }
    }

    public void setStyle( TextStyle style ) {
        this.style = style;
    }

    public Vector parseInput( String input ) {
        Vector v = new Vector(  );
        StringBuffer collector = new StringBuffer(  );
        StringBuffer checker = new StringBuffer(  );
        StringBuffer inputBuffer = new StringBuffer( input );
        int imageHeight = 0;

        for ( int i = 0; i < inputBuffer.length(  ); i++ ) {
            char testChar = inputBuffer.charAt( i );
            collector.append( testChar );

            if ( testChar != ' ' ) {
                checker.append( testChar );
            }

            Enumeration enum = smiles.keys(  );
            int smileLength = 0;
            String smileyCode = "";

            while ( enum.hasMoreElements(  ) ) {
                String elSmile = enum.nextElement(  ).toString(  );

                if ( checker.toString(  ).endsWith( elSmile ) ) {
                    if ( smileLength < elSmile.length(  ) ) {
                        smileLength = elSmile.length(  );
                        smileyCode = elSmile;
                    }
                }
            }

            if ( smileLength > 0 ) {
                splitString( collector,
                             smileyCode.charAt( 0 )
                             );

                if ( collector.length(  ) > 0 ) {
                    v.addElement( new TextRun( collector.toString(  ),
                                               style,
                                               imageHeight
                                               )
                                  );
                }

                ImageRun run = new ImageRun( ( Image ) smiles.get( smileyCode ) );
                imageHeight = run.getBoundingRect(  ).width;
                v.addElement( run );
                checker.setLength( 0 );
                collector.setLength( 0 );
            }
        }

        if ( collector.length(  ) > 0 ) {
            v.addElement( new TextRun( collector.toString(  ),
                                       style,
                                       imageHeight
                                       )
                          );
        }

        return v;
    }

    void splitString( StringBuffer buffer, char smileyStart ) {
        for ( int i = buffer.length(  ) - 1; i >= 0; i-- ) {
            char ch = buffer.charAt( i );
            buffer.setLength( i );

            if ( ch == smileyStart ) {
                break;
            }
        }
    }

    public Image[] getSmileysAsArray(  ) {
        int imgcnt = smiles.size(  );
        Image[] obj = new Image[ imgcnt ];
        Enumeration enum = smiles.keys(  );

        for ( int i = 0; i < imgcnt; i++ ) {
            String key = enum.nextElement(  ).toString(  );
            obj[ i ] = ( Image ) smiles.get( key );
        }

        return obj;
    }

    public int getSmileysCount(  ) {
        return smiles.size(  );
    }

    public String getSmileyCode( Image img ) {
        Enumeration enum = smiles.keys(  );

        while ( enum.hasMoreElements(  ) ) {
            String key = enum.nextElement(  ).toString(  );

            if ( smiles.get( key ) == img ) {
                return key;
            }
        }

        return "";
    }
}
