package dk.sdu.mmmi.digitalenergyhub.old.interfaces;

public interface ISubscriber<T> {
    void startConsume();
    void addSubscriber(String topic, IMessageHandler<T> s);
    void removeSubscriber(String topic, IMessageHandler<T> s);
}
