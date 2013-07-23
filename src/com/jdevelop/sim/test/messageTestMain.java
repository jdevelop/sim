package com.jdevelop.sim.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.jdevelop.sim.modules.mainChat;


public class messageTestMain {
	public static void main(String[] args) throws Exception {
		mainChat chat = new mainChat(args[0]);
		messageTestThread thread1 = new messageTestThread(chat,"bofh","test","1","chris1");
		messageTestThread thread2 = new messageTestThread(chat,"chris1","test1","3","bofh");
		thread1.start();
		thread2.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.equals("q"))
			return;
		}
	}
}
