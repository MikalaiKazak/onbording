package ru.otr;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import static ru.otr.App.COUNT_IN_SYSTEM;

@Service
@Slf4j
public class Producer {

    private final Counter counter = Metrics.counter("producer");

    @SneakyThrows
    private static void sleep(int targetRate) {
        Thread.sleep(1000L / targetRate);
    }

    public Flux<Message<Long>> produce(int targetRate, long upto) {
        return Flux
                .generate(
                        () -> 1L,
                        (Long state, SynchronousSink<Message<Long>> sink) -> {
                            sleep(targetRate);
                            long nextState = state + 1;
                            if (state > upto) {
                                sink.complete();
                            } else {
                                log.info("Emitted {}", state);
                                COUNT_IN_SYSTEM.decrementAndGet();
                                sink.next(new Message<>(state));
                                counter.increment();
                            }
                            return nextState;
                        }
                );
    }
}