package ru.otr.reactor;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log
public class TransformingSequenceTest {

    @Test
    public void transformSequenceUsingMapTest() {
        Flux<Integer> sequenceFlux = Flux.range(1, 10)
                .map(i -> i + 1);

        StepVerifier.create(sequenceFlux)
                .expectNext(2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
                .verifyComplete();
    }

    @Test
    public void castObjectToIntegerTest() {
        Flux<Object> objectFlux = Flux.just(1, 2, 3, 4, 5);

        Flux<Integer> numberFlux = objectFlux.cast(Integer.class);

        StepVerifier.create(numberFlux)
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

    @Test
    public void sequenceSumTest() {
        Mono<Integer> sum = Flux.just(1, 2, 3)
                .reduce(0, Integer::sum);

        StepVerifier.create(sum)
                .expectNext(6)
                .verifyComplete();
    }

    @Test
    public void sequenceSumSuccessiveTest() {
        Flux<Integer> sum = Flux.just(1, 2, 3, 4)
                .scan(Integer::sum);

        StepVerifier.create(sum)
                .expectNext(1, 3, 6, 10)
                .verifyComplete();
    }

    // FlatMap не гарантирует порядок
    @Test
    public void transformingSequenceUsingFlatMapTest() {
        Flux<String> flatMap = Flux.just("otr", ".", "ru")
                .flatMap(s -> Flux.just(s.toUpperCase()
                        .split("")));

        StepVerifier.create(flatMap)
                .expectNext("O", "T", "R", ".", "R", "U")
                .expectComplete()
                .verify();
    }

    // ConcatMap гарантирует порядок
    @Test
    public void transformingSequenceUsingConcatMapTest() {
        Flux<String> flatMap = Flux.just("otr", ".", "ru")
                .concatMap(s -> Flux.just(s.toUpperCase()
                        .split("")));

        StepVerifier.create(flatMap)
                .expectNext("O", "T", "R", ".", "R", "U")
                .expectComplete()
                .verify();
    }


    @Test
    public void handleMonoTest() {
        Mono<String> mono = Mono.just("lol")
                .handle((string, sink) -> {
                    if (!string.equals("lol")) {
                        sink.error(new RuntimeException("not lol!"));
                    } else {
                        sink.next(string);
                    }
                });

        StepVerifier.create(mono)
                .expectNext("lol")
                .verifyComplete();
    }
}
