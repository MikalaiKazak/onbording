package ru.otr;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.otr.App.COUNT_IN_SYSTEM;

@Service
@Slf4j
@RequiredArgsConstructor
public class Consumer {

    public final Producer producer;
    private final Counter consumerCounter = Metrics.counter("consumer");
    private final Timer messageTimer = Metrics.timer("message");
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final Scheduler publishOnScheduler = Schedulers.newElastic("publish");
    private final Scheduler subscribeOnScheduler = Schedulers.newElastic("subscribe");
    private final Scheduler flatMapScheduler = Schedulers.newBoundedElastic(10, 100, "flatmap");

    @SneakyThrows
    private static void sleep(long consumerRate) {
        Thread.sleep(1000L / consumerRate);
    }

    public Mono<ServerResponse> handleMessageScenario1(ServerRequest req) {
        long count = Long.parseLong(req.queryParam("count")
                .orElse("100"));
        int producerRate = Integer.parseInt(req.queryParam("producerRate")
                .orElse("5"));
        int consumerRate = Integer.parseInt(req.queryParam("consumerRate")
                .orElse("1"));

        executorService.submit(() -> {
            producer.produce(producerRate, count)
                    .subscribe((Message<Long> message) -> {

                        sleep(consumerRate);

                        log.info("Consumed {}", message);

                        COUNT_IN_SYSTEM.decrementAndGet();

                        consumerCounter.increment();

                        messageTimer.record(Duration.ofNanos(System.nanoTime() - ((Long) message.headers().get("CREATED_DATE"))));
                    });
        });
        return ServerResponse.accepted()
                .build();
    }

    public Mono<ServerResponse> handleMessageScenario2(ServerRequest req) {
        long count = Long.parseLong(req.queryParam("count")
                .orElse("100"));
        int producerRate = Integer.parseInt(req.queryParam("producerRate")
                .orElse("5"));
        int consumerRate = Integer.parseInt(req.queryParam("consumerRate")
                .orElse("1"));
        int prefetch = req.queryParam("prefetch")
                .map(Integer::parseInt)
                .orElse(Queues.SMALL_BUFFER_SIZE);


        executorService.submit(() -> {
            producer.produce(producerRate, count)
                    .subscribeOn(subscribeOnScheduler)
                    .publishOn(publishOnScheduler)
                    .subscribe((Message<Long> message) -> {
                        sleep(consumerRate);

                        log.info("Consumed {}", message);

                        COUNT_IN_SYSTEM.decrementAndGet();

                        consumerCounter.increment();

                        messageTimer.record(Duration.ofNanos(System.nanoTime() - ((Long) message.headers().get("CREATED_DATE"))));
                    });
        });
        return ServerResponse.accepted()
                .build();
    }

    public Mono<ServerResponse> handleMessageScenario3(ServerRequest req) {
        long count = Long.parseLong(req.queryParam("count")
                .orElse("100"));
        int producerRate = Integer.parseInt(req.queryParam("producerRate")
                .orElse("5"));
        int consumerRate = Integer.parseInt(req.queryParam("consumerRate")
                .orElse("1"));
        int prefetch = req.queryParam("prefetch")
                .map(Integer::parseInt)
                .orElse(Queues.SMALL_BUFFER_SIZE);
        int concurrency = req.queryParam("concurrency")
                .map(Integer::parseInt)
                .orElse(5);

        executorService.submit(() -> {
            producer.produce(producerRate, count)
                    .subscribeOn(subscribeOnScheduler)
                    .publishOn(publishOnScheduler, prefetch)
                    .flatMap((Message<Long> message) -> Mono.fromSupplier(() -> {
                        sleep(consumerRate);

                        log.info("Consumed {}", message.payload());

                        COUNT_IN_SYSTEM.decrementAndGet();

                        consumerCounter.increment();

                        messageTimer.record(Duration.ofNanos(System.nanoTime() - ((Long) message.headers().get("CREATED_DATE"))));

                        return Mono.empty();
                    }).subscribeOn(flatMapScheduler), concurrency)
                    .subscribe();
        });
        return ServerResponse.accepted()
                .build();
    }
}