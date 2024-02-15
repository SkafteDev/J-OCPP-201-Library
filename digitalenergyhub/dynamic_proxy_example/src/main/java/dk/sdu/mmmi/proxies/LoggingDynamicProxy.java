package dk.sdu.mmmi.proxies;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class LoggingDynamicProxy extends AbstractDynamicProxy {

    private final Logger logger = Logger.getLogger(realObject.getClass().getName());

    public static Object newInstance(Object obj) {
        return java.lang.reflect.Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new LoggingDynamicProxy(obj));
    }

    protected LoggingDynamicProxy(Object realObject) {
        super(realObject);
    }

    @Override
    protected void beforeInvocation(Object proxy, Method m, Object[] args) {
        logger.info("Before invocation of " + m.getName());
    }

    @Override
    protected void afterInvocation(Object proxy, Method m, Object[] args) {
        logger.info("After invocation of " + m.getName());
    }
}
