package dk.sdu.mmmi.digitalenergyhub.interfaces;

public interface ISubscriber<T> {

    void onMessageReceived(String subject, T msg, Object connection);
}
