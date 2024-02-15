package dk.sdu.mmmi.proxies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractDynamicProxy implements InvocationHandler {

    protected Object realObject;

    protected AbstractDynamicProxy(Object obj) {
        this.realObject = obj;
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result;
        try {
            beforeInvocation(proxy, m, args);
            result = m.invoke(realObject, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " +
                    e.getMessage());
        } finally {
            afterInvocation(proxy, m, args);
        }
        return result;
    }

    protected abstract void beforeInvocation(Object proxy, Method m, Object[] args);

    protected abstract void afterInvocation(Object proxy, Method m, Object[] args);
}
