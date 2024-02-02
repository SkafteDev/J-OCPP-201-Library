package dk.sdu.mmmi.digitalenergyhub.interfaces;

public interface IPublisher<T> {
    boolean publish(String subject, T msg);

    boolean publish(T msg);
}
