package com.jdevelop.sim.gui.AWT.Components;

import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;

public class SmilesSelectWindow extends AbstractCustomCanvas {
    protected ImageShowDialog dialog;

    public SmilesSelectWindow(AbstractPopupWindowElement[] items,
            int itemsPerRow) {
        super(items, itemsPerRow);
    }

    private SmilesSelectWindow() {
        //to protect from crash if created without the list of images
    }

    public boolean handleEvent(Event e) {
        switch (e.id) {
        case Event.MOUSE_UP:
            if (dialog == null) {
                displayContents();
            }
            dialog.pack();
            dialog.requestFocus();
            dialog.show();
            return true;
        }
        return super.handleEvent(e);
    }

    void displayContents() {
        Point p = getLocationOnScreen();
        dialog = new ImageShowDialog(p.x, p.y + items[0].getHeight());
    }

    public void paint(Graphics g) {
        items[selectedItem].paint(g);
    }

    class ImageShowDialog extends Window {
        public ImageShowDialog(int x, int y) {
            super(new Frame());

            int rows;
            int cols;
            cols = (imagesPerRow >= items.length) ? items.length : imagesPerRow;
            rows = (items.length / imagesPerRow)
                    + (((items.length % imagesPerRow) > 0) ? 1 : 0);
            setLocation(x, y);
            setLayout(new GridLayout(rows, cols));

            for (int i = 0; i < items.length; i++)
                this.add(items[i]);
        }

        public boolean handleEvent(Event e) {
            switch (e.id) {
            default:
                break;
            case Event.MOUSE_DOWN:
                if (e.target == this) {
                    e.id = Event.ACTION_EVENT;
                    e.arg = "smiley";

                    if (this.getComponentAt(e.x, e.y) instanceof SmileIcon) {
                        SmileIcon smiley = (SmileIcon) this.getComponentAt(e.x,
                                e.y);
                        e.target = smiley.getImage();
                        setSelected(smiley);
                        notifyListeners(e);
                    }
                }
                dispose();

                break;
            //case Event.LOST_FOCUS:
            case Event.MOUSE_EXIT:
                dispose();
                dialog = null;
                break;
            case Event.WINDOW_DESTROY:
                dispose();
                dialog = null;

                break;
            }

            return super.handleEvent(e);
        }
    }
}