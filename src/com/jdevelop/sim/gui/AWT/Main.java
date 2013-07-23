/*
 * Created on 15.02.2005 @author Eugeny N. Dzhurinsky, JDevelop Software
 * eugenydzh@jdevelop.com
 */
package com.jdevelop.sim.gui.AWT;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import com.jdevelop.sim.events.DeclineEvent;
import com.jdevelop.sim.events.IMSessionListEventResponse;
import com.jdevelop.sim.events.IMSessionsListEvent;
import com.jdevelop.sim.events.IgnoreEvent;
import com.jdevelop.sim.events.LoginEvent;
import com.jdevelop.sim.events.LoginEventResponse;
import com.jdevelop.sim.events.LogoutEvent;
import com.jdevelop.sim.gui.AWT.Components.AcceptDialog;
import com.jdevelop.sim.gui.AWT.Components.AlertWindow;
import com.jdevelop.sim.gui.AWT.Components.BannerRotation;
import com.jdevelop.sim.gui.utils.Connector;
import com.jdevelop.sim.gui.utils.RuntimeProperties;
import com.jdevelop.sim.modules.Message;

public class Main extends Applet {

    class Controller extends Thread {
        public void run() {
            if (rotate != null)
                rotate.initThread();
            while (keepRunning) {
                processIMSessions();
                try {
                    sleep(5 * 1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if (rotate != null)
                rotate.stopThread();
        }
    }

    class PopupWindow extends Frame {

        public PopupWindow() {
            setLayout(new BorderLayout());
            add(ul, BorderLayout.CENTER);
        }

        public boolean handleEvent(Event evt) {
            switch (evt.id) {
            case Event.WINDOW_DESTROY:
                closeAllMessageWindows();
                ul.destroy();
                frame = null;
                dispose();
                return true;
            }
            return super.handleEvent(evt);
        }
    }

    private static boolean BUDDY_LIST_INSIDE_APPLET = false;

    private static final boolean HAS_ACCEPT_WINDOW = true;

    private int client_ignore_timeout;

    private Connector connector;

    private boolean isAcceptedURL;

    private boolean isAllowedToStartChat;

    private int maxusers;

    private AudioClip newSessionClip;

    private Hashtable privatlist, timeout;

    private UserList ul;

    private String username;

    private PopupWindow frame;

    protected Controller controller;

    protected BannerRotation rotate;

    boolean keepRunning = true;

    String key;

    RuntimeProperties runtime;

    public Main() {
        super();
        privatlist = new Hashtable();
        timeout = new Hashtable();
        key = "";
        username = "";
    }

    public void closeWindow(String id) {
        privatlist.remove(id);
    }

    public void destroy() {
        closeAllMessageWindows();
        logOut();
    }

    public Image getImage(String name) {
        try {
            Image i = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/resources/" + name));
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(i, 0);
            tracker.waitForAll();
            return i;
        } catch (Exception e) {
            System.out.println("Unable to load image " + name);
        }
        return null;
    }

    public void init() {
        URL docbase = getDocumentBase();
        connector = new Connector(docbase.getProtocol(), docbase.getHost(),
                docbase.getPort() == -1 ? 80 : docbase.getPort(),
                getParameter("port"), getParameter("prefix"));
        setLayout(new BorderLayout());
        Properties newRuntime = new Properties();
        try {
            newRuntime.load(getClass().getResourceAsStream(
                    "/resources/layout.conf"));
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        this.runtime = new RuntimeProperties(newRuntime);
        try {
            newSessionClip = getAudioClip(getClass().getResource(
                    runtime.getString("newSessionWav")));
        } catch (Exception e) {
            // no exception
        }
        String banners = getParameter("bannersURL");
        if (banners != null) {
            int timeout = 60;
            try {
                timeout = Integer.parseInt(getParameter("bannersTimeout"));
            } catch (NumberFormatException e1) {
            }
            Color bg = Color.white;
            try {
                bg = Color.decode(getParameter("bannersBackground"));
            } catch (NumberFormatException e2) {
            }
            try {
                rotate = new BannerRotation(new URL(banners), timeout, bg, this);
            } catch (MalformedURLException e3) {
                e3.printStackTrace(System.out);
            }
        }
        maxusers = runtime.getInt("maxsessions");
        BUDDY_LIST_INSIDE_APPLET = runtime.getInt("userlist_outside_applet",
                "0") == 0;
        ul = new UserList(this, connector);
        ul.init();
        if (BUDDY_LIST_INSIDE_APPLET) {
            add(ul, BorderLayout.CENTER);
            ul.start();
        }
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void logOut() {
        if (key != null && !"".equals(key)
                && runtime.getInt("nologout", "0") == 0) {
            connector.getObjectData(new LogoutEvent(key));
        }
        keepRunning = false;
    }

    public void openBuddyList() {
        if (frame == null) {
            frame = new PopupWindow();
            frame.pack();
            frame.show();
            ul.start();
        }
    }

    public void openWindow(String to, String name) {
        // System.out.println( "UserList.openWindow() " + isAllowedToStartChat +
        // ":" + !privatlist.containsKey( new Long( to ) ) );
        if (isAllowedToStartChat && !privatlist.containsKey(to)) {
            openMsn(to, name);
        }
    }

    public Image profileImage(String username) {
        String profileImage = "";
        try {
            URLConnection connection = new URL(getParameter("profileURL")
                    + username).openConnection();
            connection.setUseCaches(false);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (!"".equals(line.trim())) {
                    profileImage = line;
                    break;
                }
            }
            br.close();
        } catch (Exception e1) {
            System.out.println(e1.toString());
            System.out.println(getParameter("profileURL"));
        }
        Image img = null;
        AlertWindow alert = new AlertWindow(new Frame(), "Image loader",
                "Loading profile image, please wait....", false);
        alert.show();
        try {
            img = getImage(profileImage);
            MediaTracker mt = new MediaTracker(this);
            mt.addImage(img, 0);
            mt.waitForAll();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        if (alert != null && alert.isShowing()) {
            alert.dispose();
        }
        return img;
    }

    public void start() {
        // System.out.println("UserList.start() called - getting login data");
        LoginEventResponse ler = null;
        ler = (LoginEventResponse) connector.getObjectData(new LoginEvent(
                getParameter("login"), getParameter("password")));
        System.out.println(getParameter("login"));
        // System.out.println("UserList.start() - got login data");
        try {
            if ((ler == null) || ler.getKey().equals("")) {
                /*
                 * Dialog dlg = new AlertWindow(new Frame(), "Error", "Failed to
                 * connect to chat server", true); dlg.show();
                 */
                throw new Exception("Login failed " + ler);
            }
            username = ler.getLogin();
            this.key = ler.getKey();
            String thisHost = getDocumentBase().getHost();
            if (ler.getAllowedHosts().indexOf(thisHost) > -1) {
                isAcceptedURL = true;
            }
            if (!isAcceptedURL || (key == null) || key.equals("")) {
                // System.out.println("UserList.start() - showing dialog with
                // error");
                Dialog dlg = new AlertWindow(new Frame(), "Error",
                        "Wrong hostname", true);
                dlg.show();
                throw new Exception("URL not accepted");
            }
            isAllowedToStartChat = "1".equals(ler.getPayed());
            // System.out.println("UserList.start() - creating Thread, setting
            // priority");
        } catch (Exception exception) {
            // System.out.println("getting exception here");
            exception.printStackTrace(System.out);
        }
        controller = new Controller();
        controller.start();
        // System.out.println("UserList.start() finished");
    }

    public void stop() {
        keepRunning = false;
    }
/*
    public boolean handleEvent(Event evt) {
        switch (evt.id) {
        case Event.MOUSE_UP:
            openBuddyList();
        }
        return super.handleEvent(evt);
    }
*/
    private void closeAllMessageWindows() {
        synchronized (privatlist) {
            Enumeration keys = privatlist.keys();
            while (keys.hasMoreElements()) {
                String winID = (String) keys.nextElement();
                MessengerWindow win = (MessengerWindow) privatlist.get(winID);
                win.active = false;
                win.dispose();
                closeWindow(winID);
            }
        }
    }

    private void processIMSessions() throws NumberFormatException {
        String[] imsessions = ((IMSessionListEventResponse) connector
                .getObjectData(new IMSessionsListEvent(key))).getSessions();
        for (int i = 0; imsessions != null && i < imsessions.length; i++) {
            String reqID = imsessions[i];
            int pos = reqID.indexOf(":");
            if (pos != -1) {
                String who = reqID.substring(pos + 1, reqID.length());
                String id = "";
                try {
                    id = reqID.substring(0, pos);
                } catch (NumberFormatException numberformatexception) {
                    numberformatexception.printStackTrace(System.out);
                }
                if (!privatlist.containsKey(id)) {
                    int status = -1;
                    if (timeout.containsKey(String.valueOf(id))) {
                        long timeleft = Long
                                .parseLong((String) timeout.get(id));
                        if ((new java.util.Date().getTime() - timeleft) < (client_ignore_timeout * 1000)) {
                            status = AcceptDialog.DECLINE;
                        }
                    }
                    if (status == -1) {
                        if (newSessionClip != null) {
                            newSessionClip.play();
                        }
                        if (HAS_ACCEPT_WINDOW) {
                            AcceptDialog dialog = new AcceptDialog(new Frame(),
                                    who, runtime, this);
                            dialog.setBounds(runtime
                                    .getLocation("AcceptWindow"));
                            dialog.setResizable(false);
                            status = dialog.showDialog();
                        } else {
                            status = AcceptDialog.ACCEPT;
                        }
                    }
                    switch (status) {
                    case AcceptDialog.ACCEPT:
                        openMsn(id, who);
                        break;
                    case AcceptDialog.DECLINE:
                        timeout.put(String.valueOf(id), String
                                .valueOf(new java.util.Date().getTime()));
                        connector.getObjectData(new DeclineEvent(key,
                                new Message("0", id, new MessageFormat(runtime
                                        .getString("offline_message"))
                                        .format(new Object[] { username }),
                                        false, false, false, "#ff0000", 14,
                                        "Times")));
                        break;
                    case AcceptDialog.IGNORE:
                        connector.getObjectData(new IgnoreEvent(key, id));
                        break;
                    }
                }
            }
        }
    }

    void openMsn(String to, String name) {
        if (!isAcceptedURL) {
            return;
        }
        if (privatlist.size() > maxusers || privatlist.contains(to)) {
            return;
        }
        privatlist.put(to, "1");
        MessengerWindow im = new MessengerWindow(name, connector, runtime,
                rotate);
        im.init(to.toString(), this, username);
        im.start(getAppletContext());
    }

    public void ignoreUser(String target) {
        connector.getObjectData(new IgnoreEvent(key, target));
    }

}
