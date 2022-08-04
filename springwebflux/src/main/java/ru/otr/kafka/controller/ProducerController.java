package ru.otr.kafka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otr.kafka.dto.ProducerDto;
import ru.otr.kafka.service.ReactiveProducerService;

@RestController
@RequestMapping("/api/producer")
@RequiredArgsConstructor
public class ProducerController {

    private final ReactiveProducerService reactiveProducerService;

    @PostMapping("/")
    public Mono<Void> produce(@RequestBody final ProducerDto producerDto) {
        reactiveProducerService.send(producerDto);
        return Mono.empty();
    }
}
