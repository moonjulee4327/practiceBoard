package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.RoleType;
import jpa.board.dto.CreateMemberDto;
import jpa.board.dto.MemberDto;
import jpa.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public Long saveMember(CreateMemberDto createMemberDto) {
        Member member = Member.builder()
                .name(createMemberDto.getName())
                .password(createMemberDto.getPassword())
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

    @Transactional
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
}
