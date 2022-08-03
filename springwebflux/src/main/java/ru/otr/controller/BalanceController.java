package ru.otr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otr.dto.CreateBalanceRequest;
import ru.otr.entity.Balance;
import ru.otr.service.BalanceService;

@RestController
@RequestMapping(value = "/api/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Balance> createBalance(@RequestBody final CreateBalanceRequest request) {
        final Balance balance = Balance.builder()
                .balance(request.getBalance())
                .memberId(request.getMemberId())
                .build();
        return balanceService.createBalance(balance);
    }
}