package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.RoleType;
import jpa.board.dto.MemberDto;
import jpa.board.exception.MemberNotFoundException;
import jpa.board.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입")
    void join() {
        Member member = createMember();
        MemberDto.Request request = new MemberDto.Request(member.getEmail(), member.getName(), member.getPassword(), member.getNickname());

        when(memberRepository.existsByEmail(member.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodePassword");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDto.Response response = memberService.saveMember(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());

        verify(memberRepository, times(1)).existsByEmail("mj1234@kakao.com");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("중복 회원 가입 시 예외 발생")
    void joinDuplication() {
        Member member = createMember();
        MemberDto.Request request = new MemberDto.Request(member.getEmail(), member.getName(), member.getPassword(), member.getNickname());

        when(memberRepository.existsByEmail(member.getEmail())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> memberService.saveMember(request));
        verify(memberRepository, times(0)).save(member);
    }

    @Test
    @DisplayName("모든 회원 조회-회원이 존재하는 경우")
    void findAll() {
        Member member = createMember();

        List<Member> members = List.of(member);
        when(memberRepository.findAll()).thenReturn(members);

        List<MemberDto.Response> memberDtos = memberService.findAllMembers();

        assertNotNull(memberDtos);
        assertEquals(1, memberDtos.size());
        assertEquals(member.getName(), memberDtos.get(0).getName());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모든 회원 조회-회원이 존재하지 않는 경우")
    void findAllMembersNotFound() {
        when(memberRepository.findAll()).thenReturn(List.of());

        List<MemberDto.Response> memberDtos = memberService.findAllMembers();

        assertNotNull(memberDtos);
        assertEquals(0, memberDtos.size());
        assertTrue(memberDtos.isEmpty());
    }

    @Test
    @DisplayName("특정 회원 조회")
    void findOne() {
        Member member = createMember();
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        MemberDto.Response memberDto = memberService.findOneMember(member.getId());

        assertNotNull(memberDto);
        assertEquals(member.getName(), memberDto.getName());
        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("특정 회원이 존재하지 않을 경우 예외 발생")
    void findOneMenberNotFound() {
        Long notFoundMemberId = 1000L;
        when(memberRepository.findById(notFoundMemberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class
                , () -> memberService.findOneMember(notFoundMemberId));
    }

    @Test
    @DisplayName("회원 닉네임 수정 성공")
    void updatedNickname() {
        Member member = createMember();
        String newNickname = "new";

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        MemberDto.Response response = memberService.updateNickname(member.getId(), newNickname);

        assertThat(response.getId()).isEqualTo(member.getId());
        assertThat(member.getNickname()).isEqualTo(newNickname);

        verify(memberRepository, times(1)).findById(member.getId());
    }

    @Test
    @DisplayName("회원 닉네임 수정 실패 시 예외 발생")
    void updateMemberNotFound() {
        Long notFoundMemberId = 1000L;
        String newNickname = "new";

        when(memberRepository.findById(notFoundMemberId)).thenReturn(Optional.empty());
        assertThrows(MemberNotFoundException.class, () -> memberService.updateNickname(notFoundMemberId, newNickname));

        verify(memberRepository, times(1)).findById(notFoundMemberId);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void deleteMember() {
        Member member = createMember();

        when(memberRepository.existsById(member.getId())).thenReturn(true);

        memberService.deleteMember(member.getId());

        verify(memberRepository, times(1)).deleteById(member.getId());
    }

    @Test
    @DisplayName("회원 삭제 실패 시 예외 발생")
    void deleteMemberNotFound() {
        Long notFoundMemberId = 1000L;

        when(memberRepository.existsById(notFoundMemberId)).thenReturn(false);

        assertThrows(MemberNotFoundException.class, () -> memberService.deleteMember(notFoundMemberId));

        verify(memberRepository, never()).deleteById(notFoundMemberId);
    }

    private Member createMember() {
        return Member.builder()
                .id(1L)
                .name("홍길동")
                .email("mj1234@kakao.com")
                .password(passwordEncoder.encode("1234"))
                .nickname("길동")
                .roleType(RoleType.USER)
                .createdDate(ZonedDateTime.now())
                .build();
    }
}
