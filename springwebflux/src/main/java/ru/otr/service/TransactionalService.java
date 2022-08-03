package ru.otr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.otr.dto.CreateTransactionRequest;
import ru.otr.entity.Balance;
import ru.otr.repository.BalanceRepository;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransactionalService {

    private final BalanceRepository balanceRepository;

    @Transactional
    public Mono<Balance> doTransaction(final CreateTransactionRequest request) {
        return Mono.zip(balanceRepository.findByMemberId(request.getFromId()),
                        balanceRepository.findByMemberId(request.getToId()))
                .flatMap(balanceTuple -> executeTransaction(balanceTuple, request.getAmount()));
    }

    private Mono<Balance> executeTransaction(Tuple2<Balance, Balance> balanceTuple, BigDecimal amount) {
        Balance fromBalance = balanceTuple.getT1();
        Balance toBalance = balanceTuple.getT2();
        return deductBalance(fromBalance, amount)
                .flatMap(balance -> increaseBalance(toBalance, amount));
    }

    private Mono<Balance> increaseBalance(final Balance toBalance, BigDecimal amount) {
        return Mono.fromSupplier(() -> new Random().nextDouble())
                .flatMap(randomValue -> increaseBalance(toBalance, amount, randomValue));
    }

    private Mono<Balance> increaseBalance(Balance toBalance, BigDecimal amount, double randomValue) {
        if (randomValue > 0.5) {
            toBalance.setBalance(toBalance.getBalance().add(amount));
            return balanceRepository.save(toBalance);
        }
        return Mono.error(new RuntimeException("randomized error"));
    }

    private Mono<Balance> deductBalance(final Balance fromBalance, BigDecimal amount) {
        fromBalance.setBalance(fromBalance.getBalance().subtract(amount));
        return balanceRepository.save(fromBalance);
    }
}