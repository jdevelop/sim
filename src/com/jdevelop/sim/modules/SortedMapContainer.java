/*
 * Created on 11.05.2005 
 * @author Eugeny N. Dzhurinsky, 
 * JDevelop Software
 * eugenydzh@jdevelop.com
 */
package com.jdevelop.sim.modules;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedMapContainer {
    SortedSet set;

    HashMap map;

    public SortedMapContainer(Comparator c) {
        set = new TreeSet(c);
        map = new HashMap();
    }

    public synchronized Object put(Object key, Object value) {
        Object retVal = map.put(key, value);
        if (retVal != null)
            set.remove(retVal);
        getSortedSet().add(value);
        return retVal;
    }

    // provides fast access to value by key
    public synchronized Object get(Object key) {
        return map.get(key);
    }

    public synchronized SortedSet getSortedSet() {
        return set;
    }

    // return values in sorted order
    public synchronized Iterator iterator() {
        return getSortedSet().iterator();
    }

    public synchronized boolean containsKey(Object o) {
        return map.containsKey(o);
    }

    public synchronized void remove(Object o) {
        Object retval = map.get(o);
        map.remove(o);
        set.remove(retval);
    }
    
    public String toString() {
        return map.toString();
    }

}