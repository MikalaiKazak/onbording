package ru.otr.reactor;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

@Log
public class CombinePublishersTest {

    @Test
    public void mergeMonosWithZipTest() {
        Mono<Integer> combineMono = Mono.zip(
                Mono.just(1),
                Mono.just(2)
        ).map(result -> result.getT1() + result.getT2());

        StepVerifier.create(combineMono)
                .expectNext(3)
                .verifyComplete();
    }

    // Пересечения исключены
    @Test
    public void concatTwoFLuxTest() {
        Flux<String> flux1 = Flux.fromArray(new String[]{"House", "Mention"});
        Flux<String> flux2 = Flux.fromStream(Stream.of("Street", "Area", "Mountain"));

        Flux<String> concatFlux = flux1.concatWith(flux2).doOnNext(log::info);

        StepVerifier.create(concatFlux)
                .expectNextCount(5)
                .verifyComplete();
    }

    // Возможны пересечения
    @Test
    public void mergeTwoFluxTest() {
        Flux<String> flux1 = Flux.fromIterable(List.of("House", "Mention"));
        Flux<String> flux2 = Flux.fromStream(Stream.of("Street", "Area", "Mountain"));

        Flux<String> concatFlux = flux1.mergeWith(flux2)
                .doOnNext(log::info);

        StepVerifier.create(concatFlux)
                .expectNextCount(5)
                .verifyComplete();
    }
}
