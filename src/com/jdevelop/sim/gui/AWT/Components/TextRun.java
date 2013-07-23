package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.util.Enumeration;
import java.util.Vector;

public class TextRun implements DrawableComponentInterface {
    private String _text;

    private TextStyle _style;

    private char[] _chars;

    private FontMetrics _fm;

    private int _fmMaxDescent;

    private int _fmMaxAscent;

    private int _fmLeading;

    private int _fmHeight;

    private Vector _substrings;

    private boolean _newLineFixed;

    private Rectangle _boundingRect;

    private int beginningHeight = 0;

    public TextRun(String text, TextStyle style) {
        _boundingRect = new Rectangle(0, 0, 0, 0);
        _text = text;
        _style = style;
        _fm = Toolkit.getDefaultToolkit().getFontMetrics(style.getFont());
        _fmMaxDescent = _fm.getMaxDescent();
        _fmMaxAscent = _fm.getMaxAscent();
        _fmLeading = _fm.getLeading();
        _fmHeight = _fm.getHeight();
        _chars = _text.toCharArray();
        _substrings = new Vector();
    }

    public TextRun(String text, TextStyle style, int height) {
        this(text, style);
        beginningHeight = height;
    }

    public TextStyle getStyle() {
        return _style;
    }

    public String getText() {
        return _text;
    }

    public void setFont(Font f) {
        _style.setFont(f);
    }

    public Rectangle getBoundingRect() {
        return _boundingRect;
    }

    int findEnd(int begin, int xOff, int xMax) {
        int end = begin;
        int width = xOff;

        for (; end != _chars.length; end++) {
            if (end >= _chars.length
                    || ((width + _fm.charWidth(_chars[end])) > xMax)
                    || (_chars[end] == '\n')) {
                break;
            }

            width += _fm.charWidth(_chars[end]);
        }

        if (end == _chars.length) {
            return end;
        }

        if (_chars[end] == '\n') {
            _newLineFixed = true;

            return end;
        }

        int oldEnd = end;

        for (; end > begin; end--)
            if (_chars[end - 1] == ' ') {
                return end;
            }

        return (xOff != 0) ? end : oldEnd;
    }

    public void prepare(int xMax, Dimension preferredSize, Point upperLeftCorner) {
        _substrings.removeAllElements();
        _boundingRect.x = 0;
        _boundingRect.y = upperLeftCorner.y;

        int x = upperLeftCorner.x;
        int y = upperLeftCorner.y;

        if (xMax <= 0) {
            xMax = 600;
        }

        int yOff = y + _fmMaxAscent;
        int xOff = x;
        int begin = 0;
        int len = _chars.length;
        int end = 0;
        int width = 0;

        do {
            if (_chars[begin] == '\n') {
                begin++;

                if (!_newLineFixed) {
                    if (beginningHeight > _fmHeight) {
                        yOff += beginningHeight;
                        beginningHeight = 0;
                    } else {
                        yOff += _fmHeight;
                    }

                    xOff = 0;
                } else {
                    _newLineFixed = false;
                }

                if (begin == (_chars.length - 1)) {
                    break;
                }
            }

            end = findEnd(begin, xOff, xMax);
            width = xOff + _fm.charsWidth(_chars, begin, end - begin);
            _substrings
                    .addElement(new Substring(begin, end - begin, xOff, yOff));
            xOff = 0;

            if (end != len) {
                if (beginningHeight > _fmHeight) {
                    yOff += beginningHeight;
                    beginningHeight = 0;
                } else {
                    yOff += _fmHeight;
                }

                begin = end;
            }
        } while (end != len);

        preferredSize.width = xMax;
        preferredSize.height = (yOff - y) + _fmMaxDescent;
        upperLeftCorner.x = width;
        upperLeftCorner.y = yOff - _fmMaxAscent;
        _boundingRect.width = preferredSize.width;
        _boundingRect.height = preferredSize.height;
    }

    public boolean contains(int x, int y, int yTranslation) {
        if (_boundingRect.inside(x, y + yTranslation)) {
            Substring target = null;

            for (Enumeration e = _substrings.elements(); e.hasMoreElements();) {
                Substring sub = (Substring) e.nextElement();

                if ((x >= sub.xOff)
                        && ((y + yTranslation) >= (sub.yOff - _fmHeight))) {
                    target = sub;
                }
            }

            if ((target != null)
                    && (x < (target.xOff + _fm.stringWidth(new String(_chars,
                            target.begin, target.length))))) {
                return true;
            }
        }

        return false;
    }

    public void draw(Graphics g, int yOff) {
        g.setColor(_style.getColor());
        g.setFont(_style.getFont());

        Substring substring;

        for (Enumeration e = _substrings.elements(); e.hasMoreElements();) {
            substring = (Substring) e.nextElement();
            g.drawChars(_chars, substring.begin, substring.length,
                    substring.xOff, substring.yOff + yOff);

            if (_style.isUnderlined()) {
                int lineY = substring.yOff + yOff + _fm.getDescent();
                int lineLength = _fm.stringWidth(substring.toString());
                g.setColor(_style.getColor());
                g.drawLine(substring.xOff, lineY, substring.xOff + lineLength,
                        lineY);
            }
        }
    }

    public String toString() {
        return "TextRun[\"" + _text + "\"]; Bounds = " + _boundingRect;
    }
}