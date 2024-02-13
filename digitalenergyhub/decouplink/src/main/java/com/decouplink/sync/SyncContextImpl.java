package com.decouplink.sync;

import com.decouplink.Context;
import com.decouplink.Link;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;

/**
 * @author mrj
 */
public class SyncContextImpl implements Context {

    // Make sure links are sorted by priority.
    private final ArrayList<LinkImpl<?>> links = new ArrayList<>();
    private final MutableLookup lookup = new MutableLookup();
    private final Map<Class, Boolean> isFaultTolerantCache = new HashMap<>();

    private synchronized void update() {
        lookup.clear();
        for (Link<?> l : links) {
            lookup.add(l.getDestination());
        }
        lookup.commit();
    }

    //
    // Accessors.
    //
    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public synchronized <T> T one(Class<T> clazz) {
        StatisticsManager.getInstance().recordFollowLink(clazz);

        return createSyncProxy(getLookup().lookup(clazz));
    }

    @Override
    public synchronized <T> Collection<T> all(Class<T> clazz) {
        StatisticsManager.getInstance().recordFollowLink(clazz);
        Iterable<? extends T> allObjectsOfClass = getLookup().lookupAll(clazz);
        //Sync. list to protect collection containing the sync proxies.
        Collection<T> c = Collections.synchronizedList(new ArrayList<T>());
        for (T object : allObjectsOfClass) {
            c.add(createSyncProxy(object));
        }
        return c;
    }

    //
    // Modifiers.
    //
    @Override
    public synchronized <T> Link<T> add(Class< T> clazz, T value) {
        return add(clazz, value, 0);
    }

    @Override
    public synchronized <T> Link<T> add(Class< T> clazz, T value, int priority) {
        StatisticsManager.getInstance().recordAddLink(clazz, value.getClass());

        LinkImpl<T> r = new LinkImpl<>(value, priority);
        links.add(r);
        Collections.sort(links);
        update();
        return r;
    }

    private synchronized <T> T createSyncProxy(final T o)
            throws IllegalArgumentException {

        // Create dynamic proxy instance.
        return (T) Proxy.newProxyInstance(
                o.getClass().getClassLoader(),
                o.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        synchronized (o) {
                            try {
                                return method.invoke(o, args);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                Logger.getLogger(SyncContextImpl.class.getName()).log(Level.SEVERE, "Error occured in Sync Proxy.", ex);
                                throw ex;
                            }
                        }

                    }
                });
    }

    private class LinkImpl<T> implements Link, Comparable<LinkImpl> {

        private final T destination;
        private final int priority;

        public LinkImpl(T destination, int priority) {
            this.destination = destination;
            this.priority = priority;
        }

        @Override
        public T getDestination() {
            return destination;
        }

        @Override
        public void dispose() {
            synchronized (SyncContextImpl.this) {
                links.remove(this);
                update();
            }
        }

        @Override
        public int compareTo(LinkImpl o) {
            return new Integer(o.priority).compareTo(priority);
        }
    }
}
