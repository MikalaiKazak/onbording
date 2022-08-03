package ru.otr.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.otr.dto.CreateBalanceRequest;
import ru.otr.entity.Balance;
import ru.otr.service.BalanceService;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BalanceController.class)
public class BalanceControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BalanceService balanceService;

    @Test
    public void createBalanceTest() {
        CreateBalanceRequest request = new CreateBalanceRequest();
        request.setBalance(BigDecimal.ONE);
        request.setMemberId(1L);

        Balance balance = Balance.builder()
                .id(2L)
                .balance(BigDecimal.ONE)
                .memberId(1L)
                .build();

        when(balanceService.createBalance(any(Balance.class))).thenReturn(Mono.just(balance));

        webClient.post()
                .uri("/api/balance")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(Balance.class);
    }
}
