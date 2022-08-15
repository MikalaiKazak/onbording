package ru.otr;

import java.util.Map;

public record Message<T>(
        Map<String, Object> headers,
        T payload
) {

    public Message(T payload) {
        this(Map.of("CREATED_DATE", System.nanoTime()), payload);
    }
}