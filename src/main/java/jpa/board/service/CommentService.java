package jpa.board.service;

import jpa.board.domain.Comment;
import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.dto.CommentDto;
import jpa.board.exception.CommentPermissionException;
import jpa.board.exception.MemberNotFoundException;
import jpa.board.exception.PostNotFoundException;
import jpa.board.repository.CommentRepository;
import jpa.board.repository.MemberRepository;
import jpa.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final SecurityContextService securityContextService;

    @Transactional
    public CommentDto.Response addCommentToPost(Long postId, CommentDto.Request commentDto) {
        Member member = findAuthenticatedMember();
        Post post = findPostById(postId);

        Comment comment = commentDto.toEntity(member, post.getId());
        Comment savedComment = commentRepository.save(comment);

        log.info("Added comment with ID : {} to Post ID : {}", savedComment.getId(), postId);
        return new CommentDto.Response(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto.Response> findCommentToPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentDto.Response::new)
                .toList();
    }

    @Transactional
    public CommentDto.Response updateCommentToPost(Long postId, CommentDto.Request request) {
        Comment comment = validateCommentOwnership(postId, request.getId());

        comment.updateComment(request.getComment());
        log.info("Updated comment with ID : {} from Post ID : {}", comment.getId(), postId);
        return new CommentDto.Response(comment);
    }

    @Transactional
    public void deleteCommentById(Long postId, CommentDto.Request request) {
        Comment comment = validateCommentOwnership(postId, request.getId());
        commentRepository.delete(comment);
        log.info("Deleted comment with ID : {} from Post ID : {}", comment.getId(), postId);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post ID : " + postId + " Not Found", postId));
    }

    private Comment validateCommentOwnership(Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new CommentPermissionException("Comment does not belong to Post. Comment ID : " + commentId + ", Post ID : " + postId, commentId));
        validateCommentAuthor(comment);
        return comment;
    }

    private void validateCommentAuthor(Comment comment) {
        String currentMemberEmail = securityContextService.getCurrentMemberEmail();
        if (!comment.isAuthor(currentMemberEmail)) {
            throw new CommentPermissionException("Comment ID : " + comment.getId() + ", Email : " + currentMemberEmail + " Not Permission to Update this Comment.", comment.getId());
        }
    }

    private Member findAuthenticatedMember() {
        String email = securityContextService.getCurrentMemberEmail();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("Member Not Found With Email : " + email, email));
    }
}
