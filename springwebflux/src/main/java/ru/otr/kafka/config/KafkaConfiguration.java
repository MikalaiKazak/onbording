package ru.otr.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;
import ru.otr.kafka.dto.ConsumerDto;
import ru.otr.kafka.dto.ProducerDto;

import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

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

    @Bean
    public ReactiveKafkaProducerTemplate<String, ProducerDto> reactiveKafkaProducerTemplate(KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }
}
