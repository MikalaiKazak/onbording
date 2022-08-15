package ru.otr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otr.Consumer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoutersConfig {

    @Bean
    public RouterFunction<ServerResponse> apis(Consumer consumer) {
        return route()
                .nest(accept(APPLICATION_JSON), builder -> builder
                        .GET("/scenario1", consumer::handleMessageScenario1)
                        .GET("/scenario2", consumer::handleMessageScenario2)
                        .GET("/scenario3", consumer::handleMessageScenario3)
                ).build();
    }

}