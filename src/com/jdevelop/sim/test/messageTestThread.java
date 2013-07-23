package com.jdevelop.sim.test;

import java.sql.SQLException;

import com.jdevelop.sim.events.*;
import com.jdevelop.sim.modules.Message;
import com.jdevelop.sim.modules.mainChat;


public class messageTestThread
    extends Thread
{
    private String login;
    private String password;
    private String target;
    private mainChat main;
    private String targetName;

    public messageTestThread( mainChat chat, String _login, String _password, String _target, String targetName ) {
        login = _login;
        password = _password;
        target = _target;
        main = chat;
        this.targetName = targetName;
    }

    public void run(  ) {
    	String key = null;
		try {
			key = main.loginUser(new LoginEvent(login, password), "123.123.123.123").getKey();
		} catch (SQLException e2) {
			System.out.println("Exception " + e2.toString() + " occured ");
		}
    	System.out.println("Start running thread for user "+login);
    	while(true) {
    		MessageListeEventResponse mer = main.getMessageList(key,target);
    		Message[] msgs = mer.getMessages();
    		System.out.println("Getting messages for "+login);
    		if (msgs != null) {
    			for (int i=0;i<msgs.length;i++) {
    				System.out.println(login+" -> "+msgs[i].getMsg());
    			}
    		} else {
    			System.out.println("Null for "+login);
    		}
    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("Exception " + e.toString() + " occured ");
			}
			Message msg = new Message("0",target.toString(),"Hello, "+targetName,true,true,true,"",12,"");
			main.addMessage(key,msg);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				System.out.println("Exception " + e1.toString() + " occured ");
			}
    	}
    }
}
