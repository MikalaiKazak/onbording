package ru.otr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public CommandLineRunner runner(final BroadcastClient broadcastClient) {
//        return args -> {
//            broadcastClient.receiveMessages();
//            broadcastClient.receiveMessages();
//        };
//    }
}
