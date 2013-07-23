/*
 * Created on 11.05.2005 
 * @author Eugeny N. Dzhurinsky, 
 * JDevelop Software
 * eugenydzh@jdevelop.com
 */
package com.jdevelop.sim.modules.Visitor;

import java.util.Iterator;
import java.util.Vector;

import com.jdevelop.sim.modules.SortedMapContainer;
import com.jdevelop.sim.modules.info;


public class AllUsersVisitor implements UserListVisitor {
    
    private SortedMapContainer users;
    
    public AllUsersVisitor(SortedMapContainer users) {
        this.users = users;
    }

    public Vector getUserList() {
        Vector ret = new Vector();
        Iterator it = users.iterator();
        while (it.hasNext()) {
            info inf = (info) it.next();
            ret.add(inf.ID + ":" + inf.getSex() + ":" + inf.getAge()
                    + ":" + new String(inf.getNick()));
        }

        return ret;
    }

}
