package ru.otr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otr.entity.Member;
import ru.otr.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Flux<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Member> findByName(final String name) {
        return memberRepository.findByName(name);
    }

    @Override
    @Transactional
    public Mono<Member> createMember(final Member member) {
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(long memberId) {
        return memberRepository.deleteById(memberId);
    }
}
