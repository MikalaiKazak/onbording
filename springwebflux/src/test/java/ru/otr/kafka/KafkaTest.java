package ru.otr.kafka;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.connect.json.JsonSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.test.condition.EmbeddedKafkaCondition;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import reactor.kafka.sender.SenderOptions;
import reactor.test.StepVerifier;

@EmbeddedKafka(topics = EmbeddedKafkareactiveTest.REACTIVE_INT_KEY_TOPIC,
        brokerProperties = { "transaction.state.log.replication.factor=1", "transaction.state.log.min.isr=1" })
public class EmbeddedKafkareactiveTest {

    public static final String REACTIVE_INT_KEY_TOPIC = "reactive_int_key_topic";

    private static final Integer DEFAULT_KEY = 1;

    private static final String DEFAULT_VERIFY_TIMEOUT = null;

    private ReactiveKafkaProducerTemplate<Integer, Employee> reactiveKafkaProducerTemplate;

    @BeforeEach
    public void setUp() {
        reactiveKafkaProducerTemplate = new ReactiveKafkaProducerTemplate<>(setupSenderOptionsWithDefaultTopic(),
                new MessagingMessageConverter());
    }

    private SenderOptions<Integer, Employee> setupSenderOptionsWithDefaultTopic() {
        Map<String, Object> senderProps = KafkaTestUtils
                .producerProps(EmbeddedKafkaCondition.getBroker().getBrokersAsString());
        SenderOptions<Integer, Employee> senderOptions = SenderOptions.create(senderProps);
        senderOptions = senderOptions.producerProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "reactive.transaction")
                .producerProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true)
                .producerProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName())
        ;
        return senderOptions;
    }

    @Test
    public void test_When_Publish() {


        Employee employee = new Employee();

        ProducerRecord<Integer, Employee> producerRecord = new ProducerRecord<Integer, Employee>(REACTIVE_INT_KEY_TOPIC, DEFAULT_KEY, employee);

        StepVerifier.create(reactiveKafkaProducerTemplate.send(producerRecord)
                        .then())
                .expectComplete()
                .verify();
    }

    @AfterEach
    public void tearDown() {
        reactiveKafkaProducerTemplate.close();
    }
}
