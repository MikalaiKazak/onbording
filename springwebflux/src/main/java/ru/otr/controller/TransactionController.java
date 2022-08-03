package ru.otr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otr.dto.CreateTransactionRequest;
import ru.otr.entity.Balance;
import ru.otr.service.TransactionalService;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionalService transactionalService;

    @PostMapping
    public Mono<Balance> createTransaction(@RequestBody final CreateTransactionRequest request) {
        return transactionalService.doTransaction(request);
    }
}