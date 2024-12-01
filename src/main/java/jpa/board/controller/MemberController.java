package jpa.board.controller;

import jpa.board.dto.*;
import jpa.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity<CreateMemberResponseDto> createMember(@RequestBody CreateMemberDto createMemberDto) {
        Long saveMemberNo = memberService.saveMember(createMemberDto);
        return ResponseEntity.ok(new CreateMemberResponseDto(saveMemberNo));
    }

    @GetMapping("")
    public ResponseEntity<Object> getMembers() {
        List<MemberDto> list = memberService.findAllMembers();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto> getMember(@PathVariable("memberId") Long memberId) {
        MemberDto memberDto = memberService.findOneMember(memberId);
        return ResponseEntity.ok(memberDto);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<UpdateMemberResponseDto> updateMember(@PathVariable("memberId") Long memberId,
                                                                @RequestBody UpdateNicknameDto updateNicknameDto) {
        Long memberNo = memberService.updateNickname(memberId, updateNicknameDto.getNickname());
        return ResponseEntity.ok(new UpdateMemberResponseDto(memberNo));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Long> deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("login")
    public ResponseEntity<JwtTokenResponse> signIn(@RequestBody SignInDto signInDto) {
        JwtTokenResponse jwtTokenResponse = memberService.signIn(signInDto);
        return ResponseEntity.ok(jwtTokenResponse);
    }
}
