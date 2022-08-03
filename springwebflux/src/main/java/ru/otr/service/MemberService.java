package ru.otr.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otr.entity.Member;

public interface MemberService {
    Flux<Member> findAll();

    Mono<Member> findByName(final String name);

    Mono<Member> createMember(final Member member);

    Mono<Void> deleteById(final long id);
}
