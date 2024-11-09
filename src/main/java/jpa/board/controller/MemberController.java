package jpa.board.controller;

import jpa.board.domain.Member;
import jpa.board.domain.RoleType;
import jpa.board.dto.*;
import jpa.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<CreateMemberResponseDto> saveMember(@RequestBody CreateMemberDto createMemberDto) {
        Member member = Member.builder()
                .name(createMemberDto.getName())
                .password(createMemberDto.getPassword())
                .nickname(createMemberDto.getNickname())
                .roleType(RoleType.USER)
                .build();

        Long saveMemberNo = memberService.join(member);
        return ResponseEntity.ok(new CreateMemberResponseDto(saveMemberNo));
    }

    @GetMapping("/members")
    public ResponseEntity<Object> listMember() {
        List<MemberDto> list = memberService.findAll();
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/members/{memberId}")
    public ResponseEntity<UpdateMemberResponseDto> updateNickname(@PathVariable("memberId") Long memberId,
                                                                @RequestBody UpdateNicknameDto updateNicknameDto) {
        Long memberNo = memberService.updateNickname(memberId, updateNicknameDto.getNickname());
        return ResponseEntity.ok(new UpdateMemberResponseDto(memberNo));
    }
}
