package ru.otr.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FilteringSequenceTest {

    @Test
    public void filterOddNumberTest() {
        Flux<Integer> filteredFlux = Flux.range(0, 10)
                .filter(integer -> integer % 2 == 0);

        StepVerifier.create(filteredFlux)
                .expectNext(0, 2, 4, 6, 8)
                .verifyComplete();
    }

    @Test
    public void excludeElementsTest() {
        Flux<String> success = Flux.just("Orange", "Apple", "Banana", "Grape", "Strawberry");
        Flux<String> erroneous = Flux.just("Banana", "Grape");

        Flux<String> filtered = success.filterWhen(s -> erroneous.hasElement(s)
                        .map(x -> !x));

        StepVerifier.create(filtered)
                .expectNext("Orange", "Apple", "Strawberry")
                .verifyComplete();
    }
}
