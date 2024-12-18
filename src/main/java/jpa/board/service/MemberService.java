package jpa.board.service;

import io.jsonwebtoken.JwtException;
import jpa.board.config.JwtTokenProvider;
import jpa.board.domain.Member;
import jpa.board.domain.RoleType;
import jpa.board.dto.*;
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
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Long saveMember(CreateMemberDto createMemberDto) {
        Member member = Member.builder()
                .email(createMemberDto.getEmail())
                .name(createMemberDto.getName())
                .password(passwordEncoder.encode(createMemberDto.getPassword()))
                .nickname(createMemberDto.getNickname())
                .roleType(RoleType.USER)
                .createdDate(ZonedDateTime.now())
                .build();

        validateDuplicateMember(member);
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("Exist Duplicate Member");
        }
    }

    @Transactional(readOnly = true)
    public List<MemberDto> findAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(Member::toMemberDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberDto findOneMember(Long memberId) {
        return memberRepository.findById(memberId)
                .map(member -> new MemberDto(member.getId(), member.getName(), member.getNickname(), member.getCreatedDate()))
                .orElseThrow(() -> new IllegalStateException("No Exist Member"));
    }

    public Long updateNickname(Long memberNo, String updateNickname) {
        Member updateMember = memberRepository.findById(memberNo)
                                                    .orElseThrow(() -> new IllegalArgumentException("No Exist Member"));
        return updateMember.updateNickname(updateNickname);
    }

    public void deleteMember(Long memberId) {
        if(!memberRepository.existsById(memberId)) {
            throw new IllegalStateException("No Exist Member");
        }
        memberRepository.deleteById(memberId);
    }

    public JwtTokenResponse signIn(SignInDto signInDto) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword());

        Authentication authenticate
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtTokenResponse jwtTokenResponse = jwtTokenProvider.generateToken(authenticate);
        return jwtTokenResponse;
    }

    public JwtTokenResponse reissue(JwtTokenRequest jwtTokenRequest) {
        String memberEmail = jwtTokenProvider.getMemberEmail(jwtTokenRequest.getRefreshToken());

        if (!jwtTokenProvider.validateRefreshToken(memberEmail, jwtTokenRequest.getRefreshToken())) {
            throw new JwtException("Not Valid Refresh token");
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(memberEmail, null, authorities);
        JwtTokenResponse jwtTokenResponse = jwtTokenProvider.generateToken(authentication);
        return jwtTokenResponse;
    }
}
