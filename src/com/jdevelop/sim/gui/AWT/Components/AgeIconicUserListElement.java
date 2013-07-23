package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class AgeIconicUserListElement extends IconicUserListElement {
    protected String age;

    public AgeIconicUserListElement(String userID, Image img, String name,
            Font font, Color color, String age) {
        super(userID, img, name, font, color);
        this.age = age;

        int width = img.getWidth(this) + fm.stringWidth(" ")
                + fm.stringWidth(userName + age);
        int height = fm.getHeight();

        if (height < icon.getHeight(this)) {
            height = icon.getHeight(this);
        }

        size = new Dimension(width, height);
    }

    public void paint(Graphics g, int x, int y) {
        this.x = x;
        this.y = y;
        int disp = fm.getHeight() - icon.getHeight(this);
        g.drawImage(icon, x, y, this);
        Font temp = g.getFont();
        g.setFont(_font);
        g.drawString(userName + age, x + icon.getWidth(this)
                + fm.stringWidth(" "), y + size.height + disp);
        if (IS_UNDERLINED) {
            int _x = x + icon.getWidth(this) + fm.stringWidth(" ");
            int _y = y + size.height + 2;
            g.drawLine(_x, _y, _x + fm.stringWidth(userName), _y);
        }
        g.setFont(temp);
    }
}