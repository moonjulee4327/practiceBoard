package jpa.board.service;

import jpa.board.BoardApplication;
import jpa.board.domain.Member;
import jpa.board.domain.RoleType;
import jpa.board.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberServiceTest {
    private MemberService memberService;

    @BeforeEach
    void beforeEach() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(BoardApplication.class);
        memberService = ac.getBean("memberService", MemberService.class);
    }

    @Test
    @DisplayName("회원가입")
    void join() {
        Member member = createMember();

        Long saveNo = memberService.join(member);
        Member findMember = memberService.findMember(saveNo);

        assertThat(member.getNo()).isEqualTo(findMember.getNo());
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
