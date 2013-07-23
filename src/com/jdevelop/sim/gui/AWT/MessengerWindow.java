package com.jdevelop.sim.gui.AWT;

import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import com.jdevelop.sim.events.AddMessageEvent;
import com.jdevelop.sim.events.MessageListEvent;
import com.jdevelop.sim.events.MessageListeEventResponse;
import com.jdevelop.sim.gui.AWT.Components.BannerRotation;
import com.jdevelop.sim.gui.AWT.Components.ColorChooserWindow;
import com.jdevelop.sim.gui.AWT.Components.ColorItem;
import com.jdevelop.sim.gui.AWT.Components.EventObserver;
import com.jdevelop.sim.gui.AWT.Components.HyperlinkReceiver;
import com.jdevelop.sim.gui.AWT.Components.HyperlinkTextView;
import com.jdevelop.sim.gui.AWT.Components.ImageButton;
import com.jdevelop.sim.gui.AWT.Components.ImagePanel;
import com.jdevelop.sim.gui.AWT.Components.KeyTextArea;
import com.jdevelop.sim.gui.AWT.Components.LightLabel;
import com.jdevelop.sim.gui.AWT.Components.ScrollView;
import com.jdevelop.sim.gui.AWT.Components.SmileIcon;
import com.jdevelop.sim.gui.AWT.Components.SmilesParser;
import com.jdevelop.sim.gui.AWT.Components.TextStyle;
import com.jdevelop.sim.gui.AWT.Components.btnSmileysSelectWindow;
import com.jdevelop.sim.gui.utils.Connector;
import com.jdevelop.sim.gui.utils.RuntimeProperties;
import com.jdevelop.sim.modules.Message;

public class MessengerWindow extends Frame implements HyperlinkReceiver,
        EventObserver {
    private String UID;

    private String key = "";

    boolean active;

    HyperlinkTextView jtpFrame;

    ImageButton btnSend;

    ScrollView jScrollPane1;

    ImageButton btnBold;

    ImageButton btnItalic;

    ImageButton btnUnderline;

    ImageButton btnProfile;

    Choice jFont;

    Choice jFontSize;

    ColorChooserWindow btnColor;

    KeyTextArea text;

    LightLabel jLabel2;

    LightLabel jLabel3;

    String user_name;

    String remote_user;

    Thread T;

    Main main;

    AppletContext context;

    RuntimeProperties runtime = null;

    Color localColor;

    btnSmileysSelectWindow smileys;

    SmilesParser parser;

    AudioClip clip;

    Color myColor;

    Color hisColor;

    BannerRotation rotation;

    Connector connector;

    public MessengerWindow(String name, Connector _connector,
            RuntimeProperties props, BannerRotation br) {
        super(MessageFormat.format(props.getString("window.caption"),
                new Object[] { name }));
        active = true;
        user_name = "";
        remote_user = "";
        localColor = Color.black;
        runtime = props;
        remote_user = name;
        this.connector = _connector;
        this.rotation = br;
    }

    public void init(String uid, Main p, String user_name) {
        this.key = p.key;
        UID = uid;
        this.user_name = user_name;
        main = p;

        try {
            clip = p.getAudioClip(getClass().getResource(
                    getString("incomingMessageWav")));
        } catch (Exception e) {
            System.out.println("Audiofile not found");
        }

        try {
            myColor = Color.decode(runtime.getString("myColor"));
        } catch (NumberFormatException e1) {
            myColor = Color.blue;
        }

        try {
            hisColor = Color.decode(runtime.getString("hisColor"));
        } catch (NumberFormatException e1) {
            hisColor = Color.pink;
        }

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void start(AppletContext ctx) {
        context = ctx;

        refresher rf = new refresher();
        rf.setPriority(Thread.MIN_PRIORITY);
        rf.start();
    }

    private String getString(String name) {
        return runtime.getString(name);
    }

    private Rectangle getLocation(String name) {
        return runtime.getLocation(name);
    }

    private void jbInit() throws Exception {
        jtpFrame = new HyperlinkTextView(true, this);
        parser = new SmilesParser("codes.txt");
        jtpFrame.setParser(parser);
        jScrollPane1 = new ScrollView(jtpFrame);
        jFont = new Choice();
        jFontSize = new Choice();
        setLayout(new BorderLayout());

        ColorItem[] items = new ColorItem[] {
                new ColorItem(Color.black, 40, 10),
                new ColorItem(Color.blue, 40, 10),
                new ColorItem(Color.cyan, 40, 10),
                new ColorItem(Color.darkGray, 40, 10),
                new ColorItem(Color.gray, 40, 10),
                new ColorItem(Color.green, 40, 10),
                new ColorItem(Color.lightGray, 40, 10),
                new ColorItem(Color.magenta, 40, 10),
                new ColorItem(Color.orange, 40, 10),
                new ColorItem(Color.pink, 40, 10),
                new ColorItem(Color.red, 40, 10) };
        btnColor = new ColorChooserWindow(items);
        text = new KeyTextArea(this, "", 3, 60, 1);
        jLabel2 = new LightLabel(Color.decode(getString("companyText.Color")),
                new Font("Dialog", 1, 12));
        jLabel3 = new LightLabel(Color.decode(getString("customText.Color")),
                new Font("Dialog", 1, 12));

        Image bgimage = getImage("Global.bgImage");
        ImagePanel backpanel;

        if (bgimage != null) {
            backpanel = new ImagePanel(bgimage);
        } else {
            backpanel = new ImagePanel(Color
                    .decode(getString("Global.bgcolor")), new Dimension(800,
                    600));
        }

        backpanel.setLayout(null);

        Image[] objs = parser.getSmileysAsArray();
        SmileIcon[] sml = new SmileIcon[objs.length];
        int imgcnt = parser.getSmileysCount();

        for (int i = 0; i < imgcnt; i++) {
            sml[i] = new SmileIcon(objs[i]);
        }

        smileys = new btnSmileysSelectWindow(sml, runtime
                .getInt("smileys_per_row"));
        smileys.setButton(getImage("selSmiles.btnImage"));
        smileys.setBounds(getLocation("selSmiles"));
        smileys.addListener(this);
        btnSend = new ImageButton(getImage("btnSend.image"), "Send");
        btnBold = new ImageButton(getImage("btnBold.normalImage"),
                getImage("btnBold.pressedImage"), "bold");
        btnItalic = new ImageButton(getImage("btnItalic.normalImage"),
                getImage("btnItalic.pressedImage"), "italic");
        btnUnderline = new ImageButton(getImage("btnUnderline.normalImage"),
                getImage("btnUnderline.pressedImage"), "underline");

        Image profileImage = main.profileImage(remote_user);

        boolean isBig = true;

        if (profileImage == null) {
            profileImage = getImage("btnProfile");
            isBig = false;
        }

        btnProfile = new ImageButton(profileImage, "profile");
        if ("".equals(runtime.getString("profileURL"))) {
            btnProfile.setActOnMouse(false);
        }
        String availFonts[] = java.awt.Toolkit.getDefaultToolkit()
                .getFontList();
        for (int i = 0; i < availFonts.length; i++)
            jFont.addItem(availFonts[i]);
        jFontSize.addItem("8");
        jFontSize.addItem("10");
        jFontSize.addItem("12");
        jFontSize.addItem("14");
        jFontSize.addItem("16");
        jFontSize.addItem("18");
        jFontSize.addItem("20");
        jFontSize.select(runtime.getInt("selFontSize"));
        btnColor.setBackground(Color.decode(getString("btnColor.bgcolor")));
        btnColor.setBounds(getLocation("btnColor"));
        btnColor.addListener(this);
        setBackground(Color.decode(getString("Global.bgcolor")));
        btnSend.setBounds(getLocation("btnSend"));
        jScrollPane1.setBackground(Color.decode(getString("ListArea.bgcolor")));
        jScrollPane1.setBounds(getLocation("ListArea"));
        btnBold.setBounds(getLocation("btnBold"));
        btnProfile.setBounds(getLocation("btnProfile"));
        if (isBig) {
            Rectangle rect = getLocation("btnProfile");
            rect.width = profileImage.getWidth(this);
            rect.height = profileImage.getHeight(this);
            // System.out.println(rect);
            btnProfile.setBounds(rect);
        }
        text.setBounds(getLocation("MsgArea"));
        try {
            text.setBackground(Color.decode(getString("MsgArea.bgcolor")));
        } catch (NumberFormatException e) {
        }
        jFont.setBounds(getLocation("selFont"));
        jFontSize.setBounds(getLocation("selFontSize"));
        jLabel2.setText(getString("companyText"));
        jLabel2.setBounds(getLocation("companyText"));
        jLabel2.setVisible(true);
        jLabel3.setText(getString("customText"));
        jLabel3.setBounds(getLocation("customText"));
        jLabel3.setVisible(true);
        btnItalic.setBounds(getLocation("btnItalic"));
        btnUnderline.setBounds(getLocation("btnUnderline"));
        if (rotation != null) {
            backpanel.add(rotation);
            rotation.setBounds(getLocation("banner"));
        }
        backpanel.add(jScrollPane1);
        backpanel.add(btnBold);
        backpanel.add(btnItalic);
        backpanel.add(btnUnderline);
        backpanel.add(btnColor);
        backpanel.add(jFont);
        backpanel.add(text);
        backpanel.add(jFontSize);
        backpanel.add(btnSend);
        backpanel.add(jLabel2);
        backpanel.add(jLabel3);
        backpanel.add(smileys);
        backpanel.add(btnProfile);
        Rectangle globalRect = runtime.getLocation("Global");
        if (isBig) {
            int height = btnProfile.getBounds().height;
            for (int i = 0; i < backpanel.getComponentCount(); i++) {
                Component c = backpanel.getComponent(i);
                if (c != btnProfile) {
                    Rectangle rect = c.getBounds();
                    rect.y += height;
                    c.setBounds(rect);
                }
            }
            globalRect.height += height;
        }
        add(backpanel, BorderLayout.CENTER);
        setBounds(globalRect);
        setResizable(false);
        show();
    }

    public void addMessage() {
        Thread msgt = new messageSender();
        msgt.setPriority(Thread.NORM_PRIORITY);
        msgt.start();
    }

    void chooseFont() {
        Font font = text.getFont();
        text.setFont(new Font(jFont.getSelectedItem(), font.getStyle(), font
                .getSize()));
    }

    void setBold(boolean isActive) {
        int style = Font.BOLD;
        Font font = text.getFont();
        text.setFont(new Font(jFont.getSelectedItem(), isActive ? (font
                .getStyle() | style) : (font.getStyle() ^ style), font
                .getSize()));
    }

    public void setColor(Color color) {
        text.setForeground(color);
        localColor = color;
    }

    void setItalic(boolean isActive) {
        int style = Font.ITALIC;
        Font font = text.getFont();
        text.setFont(new Font(jFont.getSelectedItem(), isActive ? (font
                .getStyle() | style) : (font.getStyle() ^ style), font
                .getSize()));
    }

    void setFontSize() {
        int size = 20;

        try {
            size = Integer.parseInt(jFontSize.getSelectedItem());
        } catch (NumberFormatException numberformatexception) {
        }

        Font font = text.getFont();
        text.setFont(new Font(jFont.getSelectedItem(), font.getStyle(), size));
    }

    /**
     * @see java.awt.Container#preferredSize()
     */
    public Dimension preferredSize() {
        return new Dimension(runtime.getInt("Global.W"), runtime
                .getInt("Global.H"));
    }

    /**
     * @see java.awt.Container#getMaximumSize()
     */
    public Dimension getMaximumSize() {
        return preferredSize();
    }

    /**
     * @see java.awt.Container#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        return preferredSize();
    }

    /**
     * @see java.awt.Container#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        return preferredSize();
    }

    /**
     * @see java.awt.Container#minimumSize()
     */
    public Dimension minimumSize() {
        return preferredSize();
    }

    synchronized void addMessage(Message light) {
        String message = light.getMsg();

        if (!message.endsWith("\n")) {
            message = message + "\n";
        }

        int style = 0;

        if (light.isBold) {
            style |= 1;
        }

        if (light.isItalic) {
            style |= 2;
        }

        jtpFrame.append(("0".equals(light.getUID())) ? "System message "
                : (remote_user + ":"), new TextStyle(new Font(light.family,
                style, light.size), hisColor));

        if (clip != null) {
            clip.play();
        }

        jtpFrame.append(message, new TextStyle(new Font(light.family, style,
                light.size), Color.decode(light.color), light.isUnderline));
        this.setVisible(true);
    }

    public boolean handleEvent(Event e) {
        switch (e.id) {
        default:
            break;
        case Event.WINDOW_DESTROY:
            synchronized (UID) {
                main.closeWindow(UID);
            }
            active = false;
            dispose();

            break;
        case Event.ACTION_EVENT:
            if ("Send".equals(e.arg)) {
                addMessage();
            } else if ("bold".equals(e.arg)) {
                setBold(true);
                text.requestFocus();
            } else if ("italic".equals(e.arg)) {
                setItalic(true);
                text.requestFocus();
            } else if ("debold".equals(e.arg)) {
                setBold(false);
                text.requestFocus();
            } else if ("deitalic".equals(e.arg)) {
                setItalic(false);
                text.requestFocus();
            } else if ("color".equals(e.arg)) {
                setColor(btnColor.getColor());
                text.requestFocus();
            } else if ("smiley".equals(e.arg)) {
                String code = parser.getSmileyCode((Image) e.target);
                text.append(code);
                text.requestFocus();
            } else if ("profile".equals(e.arg)) {
                try {
                    context.showDocument(new URL(runtime
                            .getString("profileURL")
                            + UID), runtime.getString("profileURLTarget"));
                } catch (Exception e1) {
                    System.out.println(main);
                    System.out.println(runtime);
                    System.out.println(e1.toString());
                }
            }
            break;
        case Event.LIST_SELECT:
            if (e.target == jFont) {
                chooseFont();
                break;
            }
            if (e.target == jFontSize) {
                setFontSize();
            }
            break;
        }
        return super.handleEvent(e);
    }

    public Image getImage(String name) {
        return main.getImage(getString(name));
    }

    public void handleHyperlink(String url) {
        try {
            context.showDocument(new URL(url), "_blank");
        } catch (MalformedURLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void acceptEvent(Event e) {
        handleEvent(e);
    }

    class refresher extends Thread {
        public synchronized void run() {
            while (active && main.isKeepRunning()) {
                Message[] lights = null;

                try {
                    MessageListeEventResponse response = (MessageListeEventResponse) connector
                            .getObjectData(new MessageListEvent(key, UID
                                    .toString()));
                    lights = response.getMessages();
                    if (lights != null) {
                        for (int i = 0; i < lights.length; i++)
                            addMessage(lights[i]);
                    }
                } catch (Exception E) {
                    E.printStackTrace(System.out);
                } finally {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        active = false;
                    }
                }
            }
        }
    }

    class messageSender extends Thread {
        public void run() {
            text.setEnabled(false);
            btnSend.setEnabled(false);
            if (text.getText().equals("")) {
                text.setEnabled(true);
                btnSend.setEnabled(true);
                return;
            }
            String message = text.getText();
            sendPostData(message);
            if (!message.endsWith("\n"))
                message = message + "\n";
            int style = 0;
            if (btnBold.isSelected())
                style |= 1;
            if (btnItalic.isSelected())
                style |= 2;
            text.setText("");
            text.setEnabled(true);
            btnSend.setEnabled(true);
            jtpFrame.append(user_name + ":", new TextStyle(new Font(jFont
                    .getSelectedItem(), style, Integer.parseInt(jFontSize
                    .getSelectedItem())), myColor));
            jtpFrame.append(message,
                    new TextStyle(new Font(jFont.getSelectedItem(), style,
                            Integer.parseInt(jFontSize.getSelectedItem())),
                            localColor, btnUnderline.isSelected()));
            text.requestFocus();
        }

        void sendPostData(String send) {
            String red = Integer.toHexString(localColor.getRed());

            if (red.length() < 2) {
                red = "0" + red;
            }

            String green = Integer.toHexString(localColor.getGreen());

            if (green.length() < 2) {
                green = "0" + green;
            }

            String blue = Integer.toHexString(localColor.getBlue());

            if (blue.length() < 2) {
                blue = "0" + blue;
            }

            Message message = new Message("", UID, send, btnBold.isSelected(),
                    btnItalic.isSelected(), btnUnderline.isSelected(), "#"
                            + red + green + blue, Integer.parseInt(jFontSize
                            .getSelectedItem()), jFont.getSelectedItem());
            connector.getObjectData(new AddMessageEvent(message, key));
        }
    }

    class Link {
        private String link;

        public Link(String s) {
            link = s;
        }

        public String getLink() {
            return link;
        }
    }
}
