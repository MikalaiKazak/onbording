package ru.otr.router.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otr.dto.CreateMemberRequest;
import ru.otr.entity.Member;
import ru.otr.service.MemberService;

@Component
@RequiredArgsConstructor
public class MemberHandler {

    private final MemberService memberService;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.findAll(), Member.class);
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        String name = request.pathVariable("name");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.findByName(name), Member.class);
    }

    public Mono<ServerResponse> createMember(ServerRequest request) {
        Mono<Member> member = request.bodyToMono(CreateMemberRequest.class)
                .flatMap(memberRequest -> memberService.createMember(Member.builder()
                        .name(memberRequest.getName())
                        .build()));
        return  ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(member, Member.class);
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.deleteById(id), Void.class);
    }
}
