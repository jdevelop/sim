package com.jdevelop.sim.tests;

import junit.framework.TestCase;

import com.jdevelop.sim.modules.mainChat;

public class TestMainChat extends TestCase {

    private static final String USERID = "1";

    private static final String TARGETID = "2";

    private static final String PASSWORD = "test";

    private static final String USERNAME = "bofh";

    private mainChat chat;

    protected void setUp() throws Exception {
        chat = new mainChat("/home/bofh/workspace/sim/runtime.props");
        chat.loginUser(USERNAME, PASSWORD);
    }

    public void testGetUserByID() throws Exception {
        assertNotNull("User is not here!", chat.getUserByID(USERID));
    }

    public void testBlockUser() throws Exception {
        chat.block(USERID, TARGETID);
        assertTrue("Blocking won't work", chat.getUserByID(USERID)
                .getIgnoreList().contains(TARGETID));
    }

    public void testUnblockUser() throws Exception {
        chat.unblock(USERID, TARGETID);
        assertFalse("Unblocking won't work", chat.getUserByID(USERID)
                .getIgnoreList().contains(TARGETID));
    }

}
