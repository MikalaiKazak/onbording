package ru.otr.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import ru.otr.kafka.dto.ConsumerDto;

import java.util.List;

@Configuration
public class ReactiveKafkaConsumerConfig {

    @Bean
    public ReceiverOptions<String, ConsumerDto> kafkaReceiverOptions(
            @Value(value = "${app.topic}") String topic,
            KafkaProperties kafkaProperties) {
        ReceiverOptions<String, ConsumerDto> basicReceiverOptions =
                ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return basicReceiverOptions.subscription(List.of(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, ConsumerDto> reactiveKafkaConsumerTemplate(
            ReceiverOptions<String, ConsumerDto> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }
}
