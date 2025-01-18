package jpa.board.service;

import io.jsonwebtoken.JwtException;
import jpa.board.config.JwtTokenProvider;
import jpa.board.domain.Member;
import jpa.board.domain.RoleType;
import jpa.board.dto.*;
import jpa.board.exception.MemberNotFoundException;
import jpa.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SecurityContextService securityContextService;

    public MemberDto.Response saveMember(MemberDto.Request request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Exist Duplicate Member");
        }

        return new MemberDto.Response(memberRepository.save(Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .roleType(RoleType.USER)
                .createdDate(ZonedDateTime.now())
                .build()).getId());
    }

    @Transactional(readOnly = true)
    public List<MemberDto.Response> findAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberDto.Response::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberDto.Response findOneMember(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberDto.Response::new)
                .orElseThrow(() -> new MemberNotFoundException("Member ID : %d Not Found".formatted(memberId), memberId + ""));
    }

    @Transactional
    public MemberDto.Response updateNickname(Long memberId, String updateNickname) {
        Member updateMember = memberRepository.findById(memberId)
                                                    .orElseThrow(() -> new MemberNotFoundException("Member ID : %d Not Found".formatted(memberId), memberId + ""));
        Long updatedMemberId = updateMember.updateNickname(updateNickname);
        return new MemberDto.Response(updatedMemberId);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        if(!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("No Exist Member", memberId + "");
        }
        memberRepository.deleteById(memberId);
    }

    public JwtTokenResponse signIn(SignInDto signInDto) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword());

        Authentication authenticate
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authenticate);
    }

    public JwtTokenResponse reissue(JwtTokenRequest jwtTokenRequest) {
        String memberEmail = jwtTokenProvider.getMemberEmail(jwtTokenRequest.getRefreshToken());

        if (!jwtTokenProvider.validateRefreshToken(memberEmail, jwtTokenRequest.getRefreshToken())) {
            jwtTokenProvider.invalidRefreshToken(memberEmail);
            throw new JwtException("Invalid Refresh token");
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(RoleType.USER.name()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(memberEmail, null, authorities);
        return jwtTokenProvider.generateToken(authentication);
    }

    public void logout(JwtTokenRequest jwtTokenRequest) {
        String memberEmail = jwtTokenProvider.getMemberEmail(jwtTokenRequest.getRefreshToken());
        jwtTokenProvider.invalidRefreshToken(memberEmail);
    }

    public Member findAuthenticatedMember() {
        String email = securityContextService.getCurrentMemberEmail();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("Member Not Found With Email : " + email, email));
    }
}
