package ru.otr;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@EmbeddedKafka(topics = {"${app.topic}"})
class ApplicationTest {

    @Test
    void main_contextLoads_DoesNotThrow() {
        assertDoesNotThrow(() -> Application.main(new String[]{"--server.port=0"}));
    }

}
