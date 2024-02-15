package dk.sdu.mmmi;

import dk.sdu.mmmi.proxies.LoggingDynamicProxy;

public class Main {
    public static void main(String[] args) {
        IRequest req = (IRequest) LoggingDynamicProxy.newInstance(new RequestImpl());

        System.out.println(req.getData());

        req.doSomething();
    }
}