package com.decouplink.core;

import com.decouplink.Context;
import com.google.common.collect.MapMaker;
import java.util.Map;

/**
 * AssociationMap is singleton providing access a global map maintaining links
 * between Java Objects and their corresponding Context objects. The map is
 * weak, thus providing seamless integration with garbage collection.
 *
 * @author mrj
 */
public class AssociationMap {

    private static AssociationMap instance = new AssociationMap();

    public static AssociationMap getInstance() {
        return instance;
    }
    /**
     * Soft values are used to avoid memory leaks that can emerge in case of
     * cyclic references where context object refers indirectly to its key
     * object.
     */
    private Map<Object, Context> map = new MapMaker().weakKeys().softValues().makeMap();

    private AssociationMap() {
    }

    public synchronized Context get(Object obj) {
        Context r = map.get(obj);
        if (r == null) {
            r = new ContextImpl();
            map.put(obj, r);
        }
        return r;
    }
}
