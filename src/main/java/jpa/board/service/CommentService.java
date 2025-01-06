package jpa.board.service;

import jpa.board.domain.Comment;
import jpa.board.domain.Member;
import jpa.board.dto.CommentDto;
import jpa.board.exception.CommentNotFoundException;
import jpa.board.exception.CommentPermissionException;
import jpa.board.exception.PostNotFoundException;
import jpa.board.repository.CommentRepository;
import jpa.board.repository.MemberRepository;
import jpa.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        validatePostExists(postId);
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
        validatePostExists(postId);
        Comment comment = findCommentById(request.getId());
        validateCommentAuthor(comment);
        Long updatedCommentId = comment.updateComment(request.getComment());
        return new CommentDto.Response(updatedCommentId, comment.getAuthorName(), comment.getComment(), ZonedDateTime.now());
    }


    public void deleteCommentById(Long postId, CommentDto.Request request) {
        Comment storedComment = findCommentById(request.getId());
        if (!storedComment.getPostId().equals(postId)) {
            throw new CommentPermissionException("Comment ID : " + request.getId() + " Not Permission to Delete this Comment", request.getId());
        }
        commentRepository.deleteById(request.getId());
    }

    private void validatePostExists(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post ID : " + postId + " Not Found", postId);
        }
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment ID : " + commentId + " Not Found", commentId));
    }

    private void validateCommentAuthor(Comment comment) {
        String currentMemberEmail = getCurrentMemberEmail();
        if (!comment.isAuthor(currentMemberEmail)) {
            throw new CommentPermissionException("Comment ID : " + comment.getId() + ", Email : " + currentMemberEmail + " Not Permission to Update this Comment", comment.getId());
        }
    }

    private String getCurrentMemberEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
        return authentication.getName();
    }
}
