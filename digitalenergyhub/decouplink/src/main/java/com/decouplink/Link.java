package com.decouplink;

/**
 * @author mrj
 */
public interface Link<T> extends Disposable {

    T getDestination();
}
