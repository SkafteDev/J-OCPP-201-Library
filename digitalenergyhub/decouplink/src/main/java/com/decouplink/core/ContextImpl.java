package com.decouplink.core;

import com.decouplink.Context;
import com.decouplink.FaultTolerant;
import com.decouplink.Link;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;

/**
 * @author mrj
 */
public class ContextImpl implements Context {

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

        if (isFaultTolerant(clazz)) {
            // With fault tolerance.
            return createFaultContainer(clazz);
        } else {
            // Without fault tolerance.
            return getLookup().lookup(clazz);
        }
    }

    @Override
    public synchronized <T> Collection<? extends T> all(Class<T> clazz) {
        StatisticsManager.getInstance().recordFollowLink(clazz);

        if (isFaultTolerant(clazz)) {
            // With fault tolerance.
            ArrayList<T> r = new ArrayList<>();
            r.add(one(clazz));
            return r;
        } else {
            // Without fault tolerance.
            return getLookup().lookupAll(clazz);
        }
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

    //
    // Private.
    //
    private synchronized boolean isFaultTolerant(Class<?> clazz) {
        Boolean r = isFaultTolerantCache.get(clazz);
        if (r == null) {
            r = clazz.getAnnotation(FaultTolerant.class) != null;
            isFaultTolerantCache.put(clazz, r);
        }
        return r;
    }

    private synchronized <T> T createFaultContainer(Class<T> clazz)
            throws IllegalArgumentException {

        // Create fault container.
        DynamicFaultContainer fc = new DynamicFaultContainer(getLookup(), clazz);

        // Create dynamic proxy instance.
        Object inst = Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                fc);

        return (T) inst;
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
            synchronized (ContextImpl.this) {
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
