package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class IconicUserListElement extends AbstractPopupWindowElement {
    protected String userID;

    protected Image icon;

    protected String userName;

    protected Font _font;

    protected Color _color;

    protected FontMetrics fm;

    protected static final boolean IS_UNDERLINED = false;

    public IconicUserListElement(String userID, Image img, String name,
            Font font, Color color) {
        this.userID = userID;
        this.icon = img;
        this.userName = name;
        this._font = font;
        this._color = color;
        fm = getFontMetrics(_font);

        int width = img.getWidth(this) + fm.stringWidth(" ")
                + fm.stringWidth(userName);
        int height = fm.getHeight();

        if (height < icon.getHeight(this)) {
            height = icon.getHeight(this);
        }

        size = new Dimension(width, height);
    }

    public Dimension preferredSize() {
        return size;
    }

    public Dimension minimumSize() {
        return preferredSize();
    }

    public void paint(Graphics g, int x, int y) {
        this.x = x;
        this.y = y;
        int disp = fm.getHeight() - icon.getHeight(this);
        g.drawImage(icon, x, y, this);
        g.drawString(userName, x + icon.getWidth(this) + fm.stringWidth(" "), y
                + size.height + disp);
        if (IS_UNDERLINED) {
            System.out.println("Drawing line");
            int _x = x + icon.getWidth(this) + fm.stringWidth(" ");
            int _y = y + size.height + 2;
            g.drawLine(_x, _y, _x + fm.stringWidth(userName), _y);
        }
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Font get_font() {
        return _font;
    }

    public void set_font(Font _font) {
        this._font = _font;
    }

    public Color get_color() {
        return _color;
    }

    public void set_color(Color _color) {
        this._color = _color;
    }

    public FontMetrics getFm() {
        return fm;
    }

    public void setFm(FontMetrics fm) {
        this.fm = fm;
    }

    public boolean containsPoint(int x, int y) {
        Rectangle rect = new Rectangle(this.x, this.y, size.width, size.height);

        if (rect.contains(x, y)) {
            return true;
        }

        return false;
    }

    public boolean isInProfile(int x, int y) {
        Rectangle rect = new Rectangle(this.x, this.y, icon.getWidth(this),
                icon.getHeight(this));

        if (rect.contains(x, y)) {
            return true;
        }

        return false;
    }
}