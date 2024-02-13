package com.decouplink.core;

import com.decouplink.ServiceUnavailableException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.openide.util.Lookup;

/**
 * Implements dynamic fault containment for any interface-type. This class
 * is not expected to be used or available to users of the library.
 *
 * @author mrj
 */
public class DynamicFaultContainer implements InvocationHandler {
    
    private final Class<?> required;

    private final Lookup context;

    public DynamicFaultContainer(Lookup context, Class<?> required) {
        
        this.required = required;
        this.context = context;
    }

    public Class<?> getRequiredInterface() {
        return required;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        
        // Find candidates.
        Iterable<?> candidates = getCandidates();

        // Try invoking services until one succeseeds.
        for(Object s : candidates) {
            try {
                System.out.println("TRYING: " + s.getClass().getSimpleName());
                return method.invoke(s, args);
            } catch(InvocationTargetException x) {
                Throwable cause = x.getCause();

                // Check to see if exception matches declaration.
                for(Class declaredException : method.getExceptionTypes()) {
                    if(cause.getClass().isAssignableFrom(declaredException)) {
                        // It does, rethrow cause.
                        throw cause;
                    }
                }

                // It didn't, continue iteration to attempt containment.
            }
        }

        // No working service was found.
        throw new ServiceUnavailableException(required, method);
    }

    public Iterable<?> getCandidates() {
        // TODO fix priority.
        return context.lookupAll(required);
    }
}
