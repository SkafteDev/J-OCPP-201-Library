package dk.sdu.mmmi.digitalenergyhub.interfaces;

import java.time.Duration;

public interface IRequester<T> {
    T request(T msg, Duration timeout);
}
