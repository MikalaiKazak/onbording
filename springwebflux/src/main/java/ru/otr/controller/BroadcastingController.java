package ru.otr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("api/broadcast")
public class BroadcastingController {

    private final List<String> messages;

    @Autowired
    public BroadcastingController(@Value("${app.messages}") final List<String> messages) {
        this.messages = messages;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<String> broadcastMessages() {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(1000));

        Flux<String> result = Flux.generate((SynchronousSink<String> synchronousSink) -> {
            int randomIndex = new Random().nextInt(messages.size());
            synchronousSink.next(messages.get(randomIndex));
        });

        return Flux.zip(interval, result).map(Tuple2::getT2);
    }
}
