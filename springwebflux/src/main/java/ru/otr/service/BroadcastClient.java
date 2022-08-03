package ru.otr.service;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;

@Service
@Slf4j
public class BroadcastClient {

    private final WebClient webClient;

    public BroadcastClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("localhost:8080/api/broadcast").build();
    }

    public void receiveMessages() {
        webClient.get()
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(new SampleSubscriber<>());
    }

    static class SampleSubscriber<T> extends BaseSubscriber<T> {

        public void hookOnSubscribe(Subscription subscription) {
            log.info("Subscribed");
            request(1);
        }

        public void hookOnNext(T value) {
            log.info(value.toString());
            request(1);
        }
    }
}
