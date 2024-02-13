package com.decouplink;

import java.util.Collection;
import org.openide.util.Lookup;

/**
 * A Context is representing the open part of Object. It allows the user to
 * access and modify its state. Clients with the need to observe state
 * reactively (observer pattern) may obtain a Lookup through getLookup().
 *
 * @author mrj
 */
public interface Context {

    /**
     * Get the state of this Context as a Lookup. The lookup allows for advanced
     * queries, listening to query results and more.
     */
    Lookup getLookup();

    /**
     * Add a dynamic link.
     *
     * Implementation note: The value objects in a WeakHashMap are held by
     * ordinary strong references. Thus care should be taken to ensure that
     * value objects do not strongly refer to their own keys, either directly or
     * indirectly, since that will prevent the keys from being discarded. Note
     * that a value object may refer indirectly to its key via the WeakHashMap
     * itself; that is, a value object may strongly refer to some other key
     * object whose associated value object, in turn, strongly refers to the key
     * of the first value object. One way to deal with this is to wrap values
     * themselves within WeakReferences before inserting, as in: m.add(key, new
     * WeakReference(value))
     */
    <T> Link<T> add(Class<T> clazz, T value);

    <T> Link<T> add(Class<T> clazz, T value, int priority);

    /**
     * Get access to exactly one link of a given type. Returns null of no such
     * link exists. This method is similar to Lookup.lookup(..).
     */
    <T> T one(Class<T> clazz);

    /**
     * Get access to all links for a given type. This method is similar to
     * Lookup.lookupAll(..).
     */
    <T> Collection<? extends T> all(Class<T> clazz);
}
