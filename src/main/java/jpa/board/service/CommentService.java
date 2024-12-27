package jpa.board.service;

import jpa.board.domain.Comment;
import jpa.board.domain.Member;
import jpa.board.dto.CommentDto;
import jpa.board.repository.CommentRepository;
import jpa.board.repository.MemberRepository;
import jpa.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Long addCommentToPost(Long postId, CommentDto.Request commentDto) {
        Member member = memberRepository.findByName(commentDto.getMember().getName());
        Comment comment = commentDto.toEntity(member, postId);
        Comment savedComment = commentRepository.save(comment);

        return savedComment.getId();
    }
}
