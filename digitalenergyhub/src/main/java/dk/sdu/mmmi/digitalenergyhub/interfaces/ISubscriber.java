package dk.sdu.mmmi.digitalenergyhub.interfaces;

public interface IClient<T> {
    void startConsume();
    void addSubscriber(String topic, IMessageHandler<T> s);
    void removeSubscriber(String topic, IMessageHandler<T> s);
}
