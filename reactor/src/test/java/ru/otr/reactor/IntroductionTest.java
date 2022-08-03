package ru.otr.reactor;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log
public class IntroductionTest {

    @SneakyThrows
    public static LocalDateTime successfulDateFetching() {
        Thread.sleep(500);
        log.info("GETTING DATE");
        return LocalDateTime.now();
    }

    @Test
    public Mono<LocalDateTime> monoJustWithoutSubscriptionTest() {
        return Mono.just(successfulDateFetching());
    }

    @Test
    public void monoJustSubscriptionTest() {
        Mono<LocalDateTime> mono = Mono.just(successfulDateFetching());

        mono.subscribe(localDateTime -> log.info(localDateTime.toString()));
        mono.subscribe(localDateTime -> log.info(localDateTime.toString()));
        mono.subscribe(localDateTime -> log.info(localDateTime.toString()));
    }

    @Test
    public void monoJustInstantiationTest() {
        Mono.just(successfulDateFetching());
    }

    @Test
    public void monoDeferSubscriptionTest() {
        Mono<LocalDateTime> mono = Mono.defer(() -> Mono.just(successfulDateFetching()));

        mono.subscribe(localDateTime -> log.info(localDateTime.toString()));
        mono.subscribe(localDateTime -> log.info(localDateTime.toString()));
        mono.subscribe(localDateTime -> log.info(localDateTime.toString()));
    }

    @Test
    public void monoDeferInstantiationTest() {
        Mono.defer(() -> Mono.just(successfulDateFetching()));
    }

    @Test
    public void unresponsiveServiceTest() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Mono<String> serviceResult = Mono.never();
            String result = serviceResult.block(Duration.ofSeconds(1));
        });

        String expectedMessage = "Timeout on blocking read for 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void emptyServiceTest() {
        AtomicReference<Boolean> emptyServiceIsCalled = new AtomicReference<>(false);

        Mono<String> serviceResult = Mono.defer(() -> {
            emptyServiceIsCalled.set(true);
            return Mono.empty();
        });

        Optional<String> optionalServiceResult = serviceResult.blockOptional();

        assertTrue(optionalServiceResult.isEmpty());
        assertTrue(emptyServiceIsCalled.get());
    }

    @Test
    public void createFluxUsingJustTest() {
        Flux<Integer> justFlux = Flux.just(1, 2, 3);

        StepVerifier.create(justFlux)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    @Test
    public void createFluxUsingFromIterableTest() {
        Flux<Integer> fromIterableFlux = Flux.fromIterable(List.of(1, 2, 3));

        StepVerifier.create(fromIterableFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void createFluxUsingRangeTest() {
        Flux<Integer> fromIterableFlux = Flux.range(1, 5);

        StepVerifier.create(fromIterableFlux)
                .expectNextSequence(List.of(1, 2, 3, 4, 5))
                .verifyComplete();
    }
}
