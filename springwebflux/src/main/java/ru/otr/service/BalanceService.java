package ru.otr.service;

import reactor.core.publisher.Mono;
import ru.otr.entity.Balance;

public interface BalanceService {

    Mono<Balance> createBalance(final Balance balance);
}
