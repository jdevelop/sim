package com.jdevelop.sim.modules;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Message implements Serializable {
    public String uid;

    public String privat;

    private String msg;

    public boolean isBold;

    public boolean isItalic;

    public boolean isUnderline;

    public String color;

    public int size;

    public String family;

    private int msgSize;

    public Message(String ifrom, String privat, String mess, boolean isBold,
            boolean isItalic, boolean isUnderline, String color, int size,
            String family) {
        if (mess.length() > 1024) {
            mess = mess.substring(0, 1023);
        }

        this.privat = privat;
        uid = ifrom;
        msg = mess;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isUnderline = isUnderline;
        this.color = color;
        this.size = size;
        this.family = family;
        calculateSize();
    }

    public Message(DataInputStream tok) throws IOException {
        this(tok.readUTF(), tok.readUTF(), tok.readUTF(), tok.readBoolean(),
                tok.readBoolean(), tok.readBoolean(), tok.readUTF(), tok
                        .readShort(), tok.readUTF());
        calculateSize();
    }

    public void writeSerialized(DataOutputStream raw) {
        try {
            if ("".equals(uid))
                raw.writeUTF("0");
            else
                raw.writeUTF(uid);
            raw.writeUTF(privat);
            raw.writeUTF(getMsg());
            raw.writeBoolean(isBold);
            raw.writeBoolean(isItalic);
            raw.writeBoolean(isUnderline);
            raw.writeUTF(color);
            raw.writeShort(size);
            raw.writeUTF(family);
            raw.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    protected void calculateSize() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeSerialized(new DataOutputStream(bos));
        msgSize = bos.size();
        try {
            bos.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public int getMsgSize() {
        return msgSize;
    }

    public String getUID() {
        return uid;
    }

    public String getMsg() {
        return msg;
    }
}