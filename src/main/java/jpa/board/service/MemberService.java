package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.dto.MemberDto;
import jpa.board.dto.UpdateNicknameDto;
import jpa.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public Long join(Member member) {
        validateDuplicateMember(member);
        Member savedMember = memberRepository.save(member);
        return savedMember.getNo();
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberNo) {
        return memberRepository.findById(memberNo).get();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("Exist Duplicate Member");
        }
    }

    public List<MemberDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(member -> new MemberDto(member.getName(), member.getNickname()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long updateNickname(Long memberNo, String updateNickname) {
        Member updateMember = memberRepository.findById(memberNo)
                                                    .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
        return updateMember.updateNickname(updateNickname);
    }
}
