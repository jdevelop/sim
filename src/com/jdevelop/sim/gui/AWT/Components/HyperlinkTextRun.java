package com.jdevelop.sim.gui.AWT.Components;

public class HyperlinkTextRun
    extends TextRun
{
    protected String _href;

    public HyperlinkTextRun( String text, String href, TextStyle style ) {
        super( text, style );
        _href = href;
    }

    public String getHref(  ) {
        return _href;
    }
}
