package dk.sdu.mmmi.digitalenergyhub.interfaces;

import java.time.Duration;

public interface IRequester<T, C> {
    T request(T msg, Duration timeout, C connection);
}
