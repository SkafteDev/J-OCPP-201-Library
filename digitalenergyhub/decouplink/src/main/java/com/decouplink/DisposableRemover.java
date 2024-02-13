package com.decouplink;

import java.util.Collection;

/**
 * @author mrj
 */
public class DisposableRemover<T> extends AbstractLink<T> {

    private final Collection<T> c;

    public DisposableRemover(Collection<T> c, T item) {
        super(item);
        this.c = c;
    }

    @Override
    public void dispose() {
        c.remove(getDestination());
    }
}
