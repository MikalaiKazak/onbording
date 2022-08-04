package ru.otr.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import ru.otr.kafka.dto.ProducerDto;

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
                .doOnSuccess(senderResult -> log.info(
                        "sent {} offset : {}",
                        producerDto,
                        senderResult.recordMetadata()
                                .offset()))
                .subscribe();
    }
}