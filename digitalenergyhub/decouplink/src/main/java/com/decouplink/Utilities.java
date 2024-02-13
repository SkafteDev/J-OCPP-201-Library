package com.decouplink;

import com.decouplink.core.AssociationMap;
import java.util.Collection;

/**
 * @author mrj
 */
public class Utilities {

    /**
     * Method for obtaining the Context object for an object. Use this when
     * creating or obtaining dynamic links.
     */
    public static Context context(Object obj) {
        return AssociationMap.getInstance().get(obj);
    }

    /**
     * Adds an item, t, to a Collection and returns a Disposable that will
     * remove the item. This method is useful in order to avoid remove-methods.
     */
    public static <T> Disposable life(T item, Collection<T> c) {
        c.add(item);
        return new DisposableRemover(c, item);
    }    
}
