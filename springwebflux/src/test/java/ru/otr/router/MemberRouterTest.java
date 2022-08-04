package ru.otr.router;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.otr.entity.Member;
import ru.otr.router.handler.MemberHandler;
import ru.otr.service.MemberService;

import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MemberRouter.class, MemberHandler.class})
@WebFluxTest
public class MemberRouterTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void findByName() {
        Member member = Member.builder()
                .id(1L)
                .name("name")
                .build();

        when(memberService.findByName("name"))
                .thenReturn(Mono.just(member));

        webTestClient.get()
                .uri("/api/member/name")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(1)
                .jsonPath("$.name")
                .isEqualTo("name");
    }
}