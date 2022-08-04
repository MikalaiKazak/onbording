package ru.otr.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otr.kafka.dto.ConsumerDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReactiveConsumerService implements CommandLineRunner {

    private final ReactiveKafkaConsumerTemplate<String, ConsumerDto> reactiveKafkaConsumerTemplate;

    private Flux<ConsumerDto> consume() {
        return reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                // .delayElements(Duration.ofSeconds(2L)) // BACKPRESSURE
                .doOnNext(consumerRecord -> log.info("received key={}, value={} from topic={}, offset={}",
                        consumerRecord.key(),
                        consumerRecord.value(),
                        consumerRecord.topic(),
                        consumerRecord.offset())
                )
                .map(ConsumerRecord::value)
                .doOnNext(consumerDto -> log.info("successfully consumed {}={}", ConsumerDto.class.getSimpleName(), consumerDto))
                .doOnError(throwable -> log.error("something bad happened while consuming : {}", throwable.getMessage()));
    }

    @Override
    public void run(String... args) {
        consume().subscribe();
    }
}