package ru.otr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.otr.entity.Balance;
import ru.otr.repository.BalanceRepository;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    @Override
    @Transactional
    public Mono<Balance> createBalance(final Balance balance) {
        return balanceRepository.save(balance);
    }
}
