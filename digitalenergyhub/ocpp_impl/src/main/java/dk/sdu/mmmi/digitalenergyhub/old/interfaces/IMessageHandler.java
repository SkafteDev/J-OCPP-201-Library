package dk.sdu.mmmi.digitalenergyhub.old.interfaces;

public interface IMessageHandler<T> {

    void onMessageReceived(String subject, T msg, Object connection);
}
