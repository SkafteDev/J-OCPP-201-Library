package com.decouplink;

/**
 *
 * @author mrj
 */
public abstract class AbstractLink<T> implements Link<T> {

    private final T destination;

    public AbstractLink(T destination) {
        this.destination = destination;
    }

    @Override
    public T getDestination() {
        return destination;
    }
}
