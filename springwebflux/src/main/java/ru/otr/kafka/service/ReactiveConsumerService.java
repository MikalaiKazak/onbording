package ru.otr.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import ru.otr.kafka.dto.ConsumerDto;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReactiveConsumerService {

    private final ReactiveKafkaConsumerTemplate<String, ConsumerDto> reactiveKafkaConsumerTemplate;

    @EventListener(ApplicationStartedEvent.class)
    public Disposable consume() {
        return reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                 .delayElements(Duration.ofSeconds(3L)) // BACKPRESSURE
                .doOnNext(consumerRecord -> log.info("received key={}, value={} from topic={}, offset={}",
                        consumerRecord.key(),
                        consumerRecord.value(),
                        consumerRecord.topic(),
                        consumerRecord.offset())
                )
                .map(ConsumerRecord::value)
                .doOnNext(consumerDto -> log.info("successfully consumed {}={}", ConsumerDto.class.getSimpleName(), consumerDto))
                .doOnError(throwable -> log.error("something bad happened while consuming : {}", throwable.getMessage()))
                .subscribe();
    }
}