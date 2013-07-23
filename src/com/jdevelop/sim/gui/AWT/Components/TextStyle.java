package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Color;
import java.awt.Font;


public class TextStyle {
    private Font _font;
    private Color _color;
    private boolean isUnderlined = false;

    public TextStyle( TextStyle t ) {
        _font = t._font;
        _color = t._color;
    }

    public TextStyle( Font f, Color c ) {
        _font = f;
        _color = c;
    }

    public TextStyle( Font f, Color c, boolean isUnderlined ) {
        this( f, c );
        this.isUnderlined = isUnderlined;
    }

    public Font getFont(  ) {
        return _font;
    }

    public boolean isUnderlined(  ) {
        return isUnderlined;
    }

    public void setUnderlined( boolean underlined ) {
        isUnderlined = underlined;
    }

    public void setFont( Font f ) {
        _font = f;
    }

    public Color getColor(  ) {
        return _color;
    }

    public void setColor( Color c ) {
        _color = c;
    }
}
