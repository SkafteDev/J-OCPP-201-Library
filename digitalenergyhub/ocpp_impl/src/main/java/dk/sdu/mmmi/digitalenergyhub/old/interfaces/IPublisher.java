package dk.sdu.mmmi.digitalenergyhub.old.interfaces;

public interface IPublisher<T> {
    boolean publish(String subject, T msg);
}
