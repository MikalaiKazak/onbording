package ru.otr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("api/broadcast")
@Slf4j
public class BroadcastingController {

    private final List<String> messages;

    @Autowired
    public BroadcastingController(@Value("${app.messages}") final List<String> messages) {
        this.messages = messages;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<ServerSentEvent<String>> broadcastMessages() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(data -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(data))
                        .event("EVENT_TYPE")
                        .data(generateMessage())
                        .build())
                .doOnCancel(() -> log.info("Interrupted by client"));
    }

    private String generateMessage() {
        int randomIndex = new Random().nextInt(messages.size());
        return messages.get(randomIndex);
    }
}
