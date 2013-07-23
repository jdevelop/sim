package com.jdevelop.sim.gui.AWT;

import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.ScrollPane;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import com.jdevelop.sim.events.UserListEventResponse;
import com.jdevelop.sim.events.UsersListEvent;
import com.jdevelop.sim.gui.AWT.Components.AbstractPopupWindowElement;
import com.jdevelop.sim.gui.AWT.Components.AgeIconicUserListElement;
import com.jdevelop.sim.gui.AWT.Components.EventObserver;
import com.jdevelop.sim.gui.AWT.Components.IconicUserList;
import com.jdevelop.sim.gui.AWT.Components.IconicUserListElement;
import com.jdevelop.sim.gui.AWT.Components.ImagePanel;
import com.jdevelop.sim.gui.utils.Connector;

public class UserList extends Panel implements EventObserver {
    public static final int MAX_ERRORS = 5;

    private static final boolean USE_ID = true;

    ScrollPane UserListScroll;

    ImagePanel bgPanel;

    IconicUserList jList1;

    Thread T;

    protected Image commonUserIconMale;

    protected Image commonUserIconFemale;

    protected Image commonUserIconOffline;

    protected Image backgroundImage;

    protected Main parent;

    Connector connector;

    private boolean keepRunning;

    public UserList(Main parent, Connector _connector) {
        this.parent = parent;
        this.connector = _connector;
        //System.out.println("UserList.UserList()");
    }

    public void start() {
        System.err.println("Staring thread");
        T = new Controller(this);
        keepRunning = true;
        T.start();
    }

    URL getDocumentBase() {
        return parent.getDocumentBase();
    }

    String getParameter(String string) {
        return parent.getParameter(string);
    }

    public void init() {
        //System.out.println("UserList.init() started");
        try {
            backgroundImage = parent.getImage(parent.runtime
                    .getString("Applet.bgImage"));
            bgPanel = new ImagePanel(backgroundImage);
            bgPanel.setBackground(Color.decode(parent.runtime
                    .getString("Applet.bgColor")));
            setLayout(new BorderLayout());
            bgPanel.setLayout(null);
            UserListScroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
            UserListScroll.setBackground(Color.decode(parent.runtime
                    .getString("UserList.bgColor")));
            jbInit();
            add(bgPanel, BorderLayout.CENTER);
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }

        //System.out.println("UserList.init() finished");
    }

    private void jbInit() throws Exception {
        //System.out.println("UserList.jbInit() started");
        Image listBg = parent.getImage(parent.runtime
                .getString("UserList.bgImage"));
        if (listBg == null) {
            jList1 = new IconicUserList(new IconicUserListElement[0],
                    parent.runtime.getInt("buddiesPerRow"));
        } else {
            jList1 = new IconicUserList(new IconicUserListElement[0],
                    parent.runtime.getInt("buddiesPerRow"), listBg);
        }
        jList1.setPadding(parent.runtime.getInt("buddyPaddingLeft"),
                parent.runtime.getInt("buddyPaddingTop"));
        jList1.addListener(this);
        commonUserIconMale = parent.getImage("userMale.gif");
        commonUserIconFemale = parent.getImage("userFemale.gif");
        commonUserIconOffline = parent.getImage("userOffline.gif");
        T = new Controller(this);
        this.setLayout(new BorderLayout());
        Image logoImage = parent.getImage(parent.runtime.getString("logo"));
        Color c1 = null;
        try {
            c1 = Color.decode(parent.runtime.getString("Applet.paddingColor"));
        } catch (Exception e) {
        }
        ImagePanel logoPanel = new ImagePanel(logoImage, c1);
        logoPanel.setBounds(parent.runtime.getLocation("UserList.logo"));
        UserListScroll.add(jList1);
        UserListScroll.setBounds(parent.runtime.getLocation("UserList.list"));
        bgPanel.add(logoPanel);
        bgPanel.add(UserListScroll);
        //System.out.println("UserList.jbInit() - finished");
    }

    AudioClip getAudioClip(URL resource) {
        return parent.getAudioClip(resource);
    }

    URL getCodeBase() {
        return parent.getCodeBase();
    }

    public boolean handleEvent(Event e) {
        if (e.id == Event.ACTION_EVENT) {
            IconicUserListElement info = (IconicUserListElement) e.target;
            if (info.isInProfile(e.x, e.y)) {
                try {
                    if (UserList.USE_ID) {
                        getAppletContext().showDocument(
                                new URL(parent.runtime.getString("profileURL")
                                        + info.getUserID()),
                                parent.runtime.getString("profileURLTarget"));
                    } else {
                        getAppletContext().showDocument(
                                new URL(parent.runtime.getString("profileURL")
                                        + info.getUserName()),
                                parent.runtime.getString("profileURLTarget"));
                    }
                } catch (MalformedURLException e1) {
                }
            } else {
                parent.openWindow(info.getUserID(), info.getUserName());
            }
        }
        return super.handleEvent(e);
    }

    public AppletContext getAppletContext() {
        return parent.getAppletContext();
    }

    public void acceptEvent(Event e) {
        handleEvent(e);
    }

    public void destroy() {
        keepRunning = false;
    }

    public Dimension preferredSize() {
        return new Dimension(parent.runtime.getInt("UserList.W"),
                parent.runtime.getInt("UserList.H"));
    }

    class Controller extends Thread {
        private UserList list;

        private int delay;

        private int client_ignore_timeout;

        private int errors = 0;

        public Controller(UserList newList) {
            //System.out.println("Controller.Controller() created");
            this.list = newList;
            delay = parent.runtime.getInt("client_pull_delay");
            client_ignore_timeout = parent.runtime
                    .getInt("client_ignore_timeout");
        }

        public void run() {
            System.out.println("start running controller");
            while (keepRunning)
                try {
                    Vector infos = ((UserListEventResponse) connector
                            .getObjectData(new UsersListEvent(parent.key)))
                            .getUserList();
                    if (infos != null) {
			Vector int_list = new Vector();
                        Font f = new Font(parent.runtime
                                .getString("UserList.font.name"), 0,
                                parent.runtime.getInt("UserList.font.size"));
                        for (int i = 0; i < infos.size(); i++) {
                            String infosElement = (String) infos.elementAt(i);
                            if (!"::delimiter::".equals(infosElement)) {
                                lightinfo li = new lightinfo(infosElement);
                                Image ci = null;
                                if ("0".equals(li.getSex()))
                                    ci = commonUserIconFemale;
                                else if ("1".equals(li.getSex()))
                                    ci = commonUserIconMale;
                                else
                                    ci = commonUserIconOffline;
                                int_list.addElement(new AgeIconicUserListElement(
                                        li.getID(), ci, li.getName(), f,
                                        Color.black, li.getAge()));
                            }
                        }
                        AbstractPopupWindowElement[] elements = new AbstractPopupWindowElement[int_list.size()];
			int_list.copyInto(elements);
                        Point scrollPos = UserListScroll.getScrollPosition();
                        jList1.setItems(elements);
                        UserListScroll.doLayout();
                        UserListScroll.setScrollPosition(scrollPos);
                    }
                    errors = 0;
                } catch (Exception exception) {
                    exception.printStackTrace(System.out);
                    errors++;
                } finally {
                    if (errors > MAX_ERRORS) {
                        /*AlertWindow alert = new AlertWindow(
                         new Frame(),
                         "Connection error",
                         "You were disconnected, please refresh page to re-connect",
                         true);
                         alert.pack();
                         int posX;
                         int posY;
                         posX = (Toolkit.getDefaultToolkit().getScreenSize().width - alert
                         .getBounds().width) / 2;
                         posY = (Toolkit.getDefaultToolkit().getScreenSize().height - alert
                         .getBounds().height) / 2;
                         alert.setBounds(posX, posY, alert.getBounds().width,
                         alert.getBounds().height);
                         alert.show();*/
                        keepRunning = false;
                    }
                    try {
                        Thread.sleep(delay * 1000);
                    } catch (InterruptedException e) {
                    }
                }
        }
    }

    class lightinfo {
        String id;

        String sex;

        String name;

        String age;

        public lightinfo(String toparse) {
            id = "";
            name = "";
            int pos = toparse.indexOf(":");
            if (pos > -1) {
                id = toparse.substring(0, pos);
                name = toparse.substring(pos + 1);
            }
            pos = name.indexOf(":");
            if (pos > -1) {
                sex = name.substring(0, pos);
                name = name.substring(pos + 1);
            }
            pos = name.indexOf(":");
            if (pos > -1) {
                age = name.substring(0, pos);
                name = name.substring(pos + 1);
            }
        }

        public String getID() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSex() {
            return sex;
        }

        public String toString() {
            return name;
        }

        public String getAge() {
            return age;
        }
    }
}
