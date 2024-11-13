package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.RoleType;
import jpa.board.dto.MemberDto;
import jpa.board.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    @DisplayName("중복 회원 가입 시 예외 발생")
    void joinDuplication() {
        Member member = createMember();

        when(memberRepository.findByName(member.getName())).thenReturn(List.of(member));

        assertThrows(IllegalStateException.class, () -> memberService.join(member));
        verify(memberRepository, times(0)).save(member);
    }

    @Test
    @DisplayName("모든 회원 조회-회원이 존재하는 경우")
    void findAll() {
        Member member = createMember();

        List<Member> members = List.of(member);
        when(memberRepository.findAll()).thenReturn(members);

        List<MemberDto> memberDtos = memberService.findAll();

        assertNotNull(memberDtos);
        assertEquals(1, memberDtos.size());
        assertEquals(member.getName(), memberDtos.get(0).getName());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모든 회원 조회-회원이 존재하지 않는 경우")
    void findAllMembersNotFound() {
        when(memberRepository.findAll()).thenReturn(List.of());

        List<MemberDto> memberDtos = memberService.findAll();

        assertNotNull(memberDtos);
        assertEquals(0, memberDtos.size());
        assertTrue(memberDtos.isEmpty());
    }

    @Test
    @DisplayName("특정 회원 조회")
    void findOne() {
        Member member = createMember();
        when(memberRepository.findById(member.getNo())).thenReturn(Optional.of(member));

        MemberDto memberDto = memberService.findOneMember(member.getNo());

        assertNotNull(memberDto);
        assertEquals(member.getName(), memberDto.getName());
        verify(memberRepository, times(1)).findById(member.getNo());
    }

    @Test
    @DisplayName("특정 회원이 존재하지 않을 경우 예외 발생")
    void findOneMenberNotFound() {
        Long notFoundMemberId = 1000L;
        when(memberRepository.findById(notFoundMemberId)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class
                , () -> memberService.findOneMember(notFoundMemberId));
    }

    private Member createMember() {
        return Member.builder()
                .name("홍길동")
                .password("1234")
                .nickname("길동")
                .roleType(RoleType.USER)
                .createdDate(ZonedDateTime.now())
                .build();
    }
}
