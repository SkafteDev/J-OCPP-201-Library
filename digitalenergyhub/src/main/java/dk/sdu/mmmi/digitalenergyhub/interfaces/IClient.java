package dk.sdu.mmmi.digitalenergyhub.interfaces;

public interface IClient<T> {
    void startConsume();
    void addSubscriber(String topic, ISubscriber<T> s);
    void removeSubscriber(String topic, ISubscriber<T> s);
}
