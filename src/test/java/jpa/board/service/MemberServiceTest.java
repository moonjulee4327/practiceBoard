package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.RoleType;
import jpa.board.dto.MemberDto;
import jpa.board.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입")
    void join() {
        Member member = createMember();

        when(memberRepository.save(member)).thenReturn(member);
        when(memberRepository.findById(member.getNo())).thenReturn(Optional.of(member));

        Long saveNo = memberService.join(member);
        Member findMember = memberService.findMember(saveNo);

        assertThat(member.getNo()).isEqualTo(findMember.getNo());
    }

    @Test
    @DisplayName("전체 회원 조회")
    void findAll() {
        Member member = createMember();
        List<Member> members = new ArrayList<>();
        members.add(member);
        when(memberRepository.findAll()).thenReturn(members);

        List<MemberDto> list = memberService.findAll()
                .stream()
                .map(memberDto -> new MemberDto(member.getName(), member.getNickname()))
                .collect(Collectors.toList());
        
        assertNotNull(list);
    }

    private Member createMember() {
        return Member.builder()
                .name("홍길동")
                .password("1234")
                .nickname("길동")
                .roleType(RoleType.USER)
                .build();
    }
}
