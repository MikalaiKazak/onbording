package ru.otr.reactor;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

@Log
public class ExceptionsHandlingTest {

    @Test
    public void handleException() {
        Mono<Object> mono = Mono.defer(() -> Mono.just(getDate()));

        StepVerifier.create(mono)
                .expectErrorMessage("Exception")
                .verify();
    }

    @Test
    public void handleExceptionWithFallback() {
        LocalDate now = LocalDate.now();

        Mono<LocalDate> mono = Mono
                .defer(() -> Mono.just(getDate()))
                .onErrorResume(throwable -> Mono.just(now));

        StepVerifier.create(mono)
                .expectNext(now)
                .verifyComplete();
    }

    @Test
    public void handExceptionWithContinue() {
        Flux<Integer> flux = Flux.range(1, 4)
                .map(i -> i == 2 ? i / 0 : i * 2)
                .onErrorContinue((err, i) -> log.info("onErrorContinue= " + i));

        StepVerifier.create(flux)
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    public void handleExceptionWithStop() {
        Flux<Integer> flux = Flux.range(1, 4)
                .map(i -> i == 2 ? i / 0 : i * 2)
                .onErrorStop()
                .onErrorContinue((err, i) -> log.info("onErrorContinue"));

        StepVerifier.create(flux)
                .expectNextCount(1)
                .expectError(ArithmeticException.class)
                .verify();
    }

    private static LocalDate getDate() {
        throw new RuntimeException("Exception");
    }
}
