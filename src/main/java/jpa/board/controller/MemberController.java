package jpa.board.controller;

import jakarta.validation.Valid;
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
    public ResponseEntity<MemberResponseDto> createMember(@RequestBody @Valid MemberDto.Request request) {
        Long saveMemberNo = memberService.saveMember(request);
        return ResponseEntity.ok(new MemberResponseDto(saveMemberNo));
    }

    @GetMapping("")
    public ResponseEntity<List<MemberDto.Response>> getMembers() {
        List<MemberDto.Response> list = memberService.findAllMembers();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto1> getMember(@PathVariable("memberId") Long memberId) {
        MemberDto1 memberDto = memberService.findOneMember(memberId);
        return ResponseEntity.ok(memberDto);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable("memberId") Long memberId,
                                                          @RequestBody UpdateNicknameDto updateNicknameDto) {
        Long memberNo = memberService.updateNickname(memberId, updateNicknameDto.getNickname());
        return ResponseEntity.ok(new MemberResponseDto(memberNo));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Long> deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> signIn(@RequestBody SignInDto signInDto) {
        JwtTokenResponse jwtTokenResponse = memberService.signIn(signInDto);
        return ResponseEntity.ok(jwtTokenResponse);
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtTokenResponse> reissue(@RequestBody JwtTokenRequest jwtTokenRequest) {
        JwtTokenResponse jwtTokenResponse = memberService.reissue(jwtTokenRequest);
        return ResponseEntity.ok(jwtTokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody JwtTokenRequest jwtTokenRequest) {
        memberService.logout(jwtTokenRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
