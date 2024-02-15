package dk.sdu.mmmi;

public class RequestImpl implements IRequest {
    @Override
    public String getData() {
        return "My data";
    }

    @Override
    public void doSomething() {
        System.out.println("RequestImpl: Doing something...");
    }
}
