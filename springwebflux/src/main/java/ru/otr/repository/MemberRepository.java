package ru.otr.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.otr.entity.Member;

public interface MemberRepository extends R2dbcRepository<Member, Long> {
    Mono<Member> findByName(String name);
}