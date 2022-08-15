package ru.otr.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otr.kafka.dto.ProducerDto;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
public class ReactiveProducerService {

    private final ReactiveKafkaProducerTemplate<String, ProducerDto> reactiveKafkaProducerTemplate;
    private final String topic;

    public ReactiveProducerService(ReactiveKafkaProducerTemplate<String, ProducerDto> reactiveKafkaProducerTemplate,
                                   @Value(value = "${app.topic}") String topic) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.topic = topic;
    }

    public void send(ProducerDto producerDto) {
        log.info("send to topic={}, {}={},", topic, ProducerDto.class.getSimpleName(), producerDto);
        reactiveKafkaProducerTemplate.send(new ProducerRecord<>(topic, producerDto))
                .doOnError(error -> log.error(error.getMessage()))
                .doOnSuccess(r -> {
                    RecordMetadata metadata = r.recordMetadata();
                    Instant timestamp = Instant.ofEpochMilli(metadata.timestamp());
                    log.info("Message sent successfully, topic-partition={}-{} offset={} timestamp={}",
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            timestamp);
                })
                .subscribe();
    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        Flux.interval(Duration.ofSeconds(1))
                .flatMap(integer -> {
                    send(new ProducerDto(1L));
                    return Flux.never();
                })
//                .onBackpressureBuffer()
                .subscribe();
    }
}