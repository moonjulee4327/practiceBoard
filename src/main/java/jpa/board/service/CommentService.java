package jpa.board.service;

import jpa.board.domain.Comment;
import jpa.board.domain.Member;
import jpa.board.dto.CommentDto;
import jpa.board.exception.CommentNotFoundException;
import jpa.board.exception.PostNotFoundException;
import jpa.board.repository.CommentRepository;
import jpa.board.repository.MemberRepository;
import jpa.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post ID : " + postId + " Not Found", postId));
        Comment comment = commentDto.toEntity(member, postId);
        Comment savedComment = commentRepository.save(comment);

        return savedComment.getId();
    }

    public List<CommentDto.Response> findCommentToPost(Long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        return commentList.stream()
                .map(Comment::toCommentDto)
                .collect(Collectors.toList());
    }

    public CommentDto.Response updateCommentToPost(Long postId, CommentDto.Request request) {
        postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post ID : " + postId + " Not Found", postId));
        Comment savedComment = commentRepository.findById(request.getId()).orElseThrow(() -> new CommentNotFoundException("Comment ID : " + request.getId() + " Not Found", request.getId()));
        Long updatedCommentId = savedComment.updateComment(request.getComment());
        return new CommentDto.Response(updatedCommentId, savedComment.getAuthorName(), savedComment.getComment(), ZonedDateTime.now());
    }
}
