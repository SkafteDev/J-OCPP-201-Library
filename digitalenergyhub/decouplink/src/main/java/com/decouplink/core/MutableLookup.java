package com.decouplink.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * A Lookup with extra powers. You probably want to use it as a better
 * ProxyLookup. The idea is that you perform a number of modifications to the
 * lookups contents, and then invoke commit() to publish all changes
 * at once.
 *
 * @author mrj
 */
public class MutableLookup extends ProxyLookup {

    private final Collection<Object> objects =
            Collections.synchronizedList(new ArrayList<>());

    /**
     * Add a single object.
     *
     * Invoke commit() for changes to take affect.
     */
    public void add(Object obj) {
        objects.add(obj);
    }

    /**
     * Remove a single object. Note that this method cannot be used to remove
     * objects added through a delegate lookup or with set().
     *
     * Invoke commit() for changes to take affect.
     */
    public void remove(Object obj) {
        objects.remove(obj);
    }

    /**
     * Clear the contents of the lookup.
     *
     * Invoke commit() for changes to take affect.
     */
    public void clear() {
        objects.clear();
    }

    /**
     * Make changes take affect.
     */
    public void commit() {
        setLookups(Lookups.fixed(objects.toArray()));
    }
}
