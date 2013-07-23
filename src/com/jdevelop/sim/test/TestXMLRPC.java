/*
 * Created on 7/4/2006 
 * @author Eugeny N. Dzhurinsky, 
 * JDevelop Software
 * eugenydzh@jdevelop.com
 */
package com.jdevelop.sim.test;

import java.util.Vector;

import junit.framework.TestCase;

import org.apache.xmlrpc.XmlRpcClientLite;

public class TestXMLRPC extends TestCase {

    public void testRPC() throws Exception {
        XmlRpcClientLite client = new XmlRpcClientLite("http://www.loadstop.com:2003/");
        Vector params = new Vector();
        Object obj = client.execute("getAllMembers",params);
        System.out.println(obj);
    }
    
}
