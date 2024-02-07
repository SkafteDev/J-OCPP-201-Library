package dk.sdu.mmmi.digitalenergyhub.interfaces;

public interface IMessageHandler<T> {

    void onMessageReceived(String subject, T msg, Object connection);
}
