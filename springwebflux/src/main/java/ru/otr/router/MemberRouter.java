package ru.otr.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otr.router.handler.MemberHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MemberRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(final MemberHandler handler) {
        return nest(path("/api/member"), route()
                .GET("/", accept(MediaType.APPLICATION_JSON), handler::findAll)
                .GET("/{name}", accept(MediaType.APPLICATION_JSON), handler::findByName)
                .POST("/", accept(MediaType.APPLICATION_JSON), handler::createMember)
                .DELETE("/{id}", accept(MediaType.APPLICATION_JSON), handler::deleteById)
                .build());
    }
}
