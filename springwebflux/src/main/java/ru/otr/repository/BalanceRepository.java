package ru.otr.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.otr.entity.Balance;

public interface BalanceRepository extends R2dbcRepository<Balance, Long> {
    Mono<Balance> findByMemberId(Long memberId);
}
