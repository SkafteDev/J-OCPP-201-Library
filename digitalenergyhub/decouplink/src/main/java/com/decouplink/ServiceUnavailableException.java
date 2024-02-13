package com.decouplink;

import java.lang.reflect.Method;

/**
 * Indicates that the invoked method could not be executed. This may be
 * because no providing object of the method was found, or all the found
 * providers failed to satisfy the declared interface required by the caller.
 *
 * @author mrj
 */
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException() {
        super("Service Unavailable.");
    }

    public ServiceUnavailableException(Class requiredClass, Method method) {
        super(
                "Service unavailable for \"" + requiredClass +
                "\" while calling \"" + method + "\".");
    }
}
