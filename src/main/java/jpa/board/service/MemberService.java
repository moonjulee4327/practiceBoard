package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long join(Member member) {
        Member savedMember = memberRepository.save(member);
        return savedMember.getNo();
    }

    public Member findByMember(Long memberNo) {
        return memberRepository.findById(memberNo).get();
    }
}
