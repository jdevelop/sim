package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import java.util.Enumeration;
import java.util.Vector;


public class TextView
    extends Canvas
{
    public static Font DEFAULT_FONT = new Font( "Dialog", 0, 10 );
    protected Vector _runs;
    protected TextStyle _defaultStyle;
    private Image _image;
    private Graphics _graphics;
    private Dimension _bufferSize;
    private int _fullHeight;
    private ScrollView _scrollView;
    protected int _yTranslation;
    private Point _nextDrawPoint;
    private Dimension _scratchDimension;
    private boolean _autoScrolling;
    protected int _firstRun;
    SmilesParser parser;

    public TextView( boolean autoScrolling ) {
        _bufferSize = new Dimension( 0, 0 );
        _yTranslation = 0;
        _nextDrawPoint = new Point( 0, 0 );
        _scratchDimension = new Dimension( 0, 0 );
        _firstRun = 0;
        _runs = new Vector(  );
        setFont( DEFAULT_FONT );
        _autoScrolling = autoScrolling;
        reshape( 0, 0, 1024, 768 );
        _defaultStyle = new TextStyle( getFont(  ),
                                       Color.black
                                       );
    }

    public TextView(  ) {
        this( false );
    }

    public void setParser( SmilesParser parser ) {
        this.parser = parser;
    }

    public TextStyle getDefaultStyle(  ) {
        return _defaultStyle;
    }

    void setScrollView( ScrollView sv ) {
        _scrollView = sv;
    }

    public void setFont( String name ) {
        Font oldFont = getFont(  );
        Font newFont = new Font( name,
                                 oldFont.getStyle(  ),
                                 oldFont.getSize(  )
                                 );
        setFont( newFont );
    }

    public void setFontSize( int size ) {
        Font oldFont = getFont(  );
        Font newFont = new Font( oldFont.getName(  ),
                                 oldFont.getStyle(  ), size
                                 );
        setFont( newFont );
    }

    public void setFont( Font f ) {
        super.setFont( f );
        _defaultStyle = new TextStyle( f,
                                       getForeground(  )
                                       );
    }

    public void append( String s, TextStyle style ) {
        if ( s.length(  ) == 0 ) {
            return;
        } else {
            //todo parsing string for smileys
            if ( parser != null ) {
                parser.setStyle( style );

                Vector v = parser.parseInput( s );

                for ( int i = 0; i < v.size(  ); i++ )
                    append( ( DrawableComponentInterface ) v.elementAt( i ) );
            } else {
                TextRun run = new TextRun( s, style );
                append( run );
            }

            return;
        }
    }

    public synchronized void append( DrawableComponentInterface run ) {
        _runs.addElement( run );
        prepare( run );

        if ( _scrollView != null ) {
            _scrollView.boundsChanged(  );

            if ( _autoScrolling ) {
                _scrollView.scrollToBottom(  );
            }
        }

        repaint(  );
    }

    public void append( String s ) {
        append( s, _defaultStyle );
    }

    public void setYTranslation( int y ) {
        _yTranslation = y;
    }

    public void update( Graphics g ) {
        paint( g );
    }

    public void paint( Graphics g ) {
        renderViewable( g );
    }

    public void show(  ) {
        if ( _autoScrolling ) {
            _scrollView.scrollToBottom(  );
        }
    }

    public void reshape( int x, int y, int w, int h ) {
        boolean reRender = size(  ).width != w;
        super.reshape( x, y, w, h );

        if ( !createBuffer(  ) ) {
            return;
        }

        if ( reRender ) {
            prepareAll(  );
        }

        if ( _scrollView != null ) {
            _scrollView.boundsChanged(  );

            if ( _autoScrolling ) {
                _scrollView.scrollToBottom(  );
            }
        }

        repaint(  );
    }

    boolean createBuffer(  ) {
        Dimension size = size(  );

        if ( ( size.width <= 0 ) || ( size.height <= 0 ) ) {
            return false;
        }

        _image = createImage( size.width, size.height );

        if ( _image == null ) {
            return false;
        }

        if ( _graphics != null ) {
            _graphics.dispose(  );
        }

        _graphics = _image.getGraphics(  );

        return true;
    }

    public Dimension preferredSize(  ) {
        return new Dimension( size(  ).width, _fullHeight );
    }

    void prepare( DrawableComponentInterface run ) {
        int oldY = _nextDrawPoint.y;
        run.prepare( size(  ).width, _scratchDimension, _nextDrawPoint );
        _fullHeight = oldY + _scratchDimension.height;
    }

    synchronized void prepareAll(  ) {
        _nextDrawPoint.x = 0;
        _nextDrawPoint.y = 0;

        int width = size(  ).width;
        TextRun run;

        for ( Enumeration e = _runs.elements(  ); e.hasMoreElements(  );
                  prepare( run )
              )
            run = ( TextRun ) e.nextElement(  );
    }

    synchronized void renderViewable( Graphics g ) {
        Dimension size = size(  );

        if ( ( _graphics == null ) && !createBuffer(  ) ) {
            return;
        }

        _graphics.setColor( getBackground(  ) );
        _graphics.fillRect( 0, 0, size.width, size.height );

        DrawableComponentInterface run = null;
        _firstRun = -1;

        int i = 0;

        for ( Enumeration e = _runs.elements(  ); e.hasMoreElements(  ); i++ ) {
            run = ( DrawableComponentInterface ) e.nextElement(  );

            Rectangle r = run.getBoundingRect(  );

            if ( ( ( r.y - _yTranslation ) + r.height ) < 0 ) {
                continue;
            }

            if ( _firstRun == -1 ) {
                _firstRun = i;
            }

            if ( ( r.y - _yTranslation ) > size.height ) {
                break;
            }

            run.draw( _graphics, -_yTranslation );
        }

        g.drawImage( _image, 0, 0, this );
    }
}
