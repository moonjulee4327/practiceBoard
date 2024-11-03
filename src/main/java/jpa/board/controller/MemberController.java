package jpa.board.controller;

import jpa.board.domain.Member;
import jpa.board.domain.RoleType;
import jpa.board.dto.CreateMemberDto;
import jpa.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members")
    public Long saveMember(@RequestBody CreateMemberDto createMemberDto) {
        Member member = Member.builder()
                .name(createMemberDto.getName())
                .password(createMemberDto.getPassword())
                .nickname(createMemberDto.getNickname())
                .roleType(RoleType.USER)
                .build();

        return memberService.join(member);
    }
}
