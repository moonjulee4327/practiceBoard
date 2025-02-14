package jpa.board.service;

import jpa.board.domain.Comment;
import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.domain.RoleType;
import jpa.board.dto.CommentDto;
import jpa.board.dto.MemberDto;
import jpa.board.repository.CommentRepository;
import jpa.board.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void addCommentToPost() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        CommentDto.Request commentRequest = new CommentDto.Request(1L, "댓글 내용");
        Comment comment = createComment(1L, mockMember, post.getId());

        when(memberService.findAuthenticatedMember()).thenReturn(mockMember);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto.Response response = commentService.addCommentToPost(post.getId(), commentRequest);

        assertEquals(comment.getId(), response.getId());
        assertEquals(commentRequest.getComment(), response.getComment());
        assertEquals(mockMember.getNickname(), response.getAuthor());
        assertEquals(post.getId(), comment.getPostId());

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void findCommentToPost() {
    }

    @Test
    void updateCommentToPost() {
    }

    @Test
    void deleteCommentById() {
    }

    private Comment createComment(Long id, Member member, Long postId) {
        return Comment.builder()
                .id(id)
                .comment("댓글 내용")
                .createdDate(ZonedDateTime.now())
                .modifiedDate(ZonedDateTime.now())
                .member(member)
                .postId(postId)
                .authorName(member.getNickname())
                .build();
    }

    private Post createPost(Long id, Member member) {
        return Post.builder()
                .id(id)
                .title("제목")
                .content("내용")
                .member(member)
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .id(1L)
                .name("홍길동")
                .email("mj1234@kakao.com")
                .password("1234")
                .nickname("길동")
                .roleType(RoleType.USER)
                .createdDate(ZonedDateTime.now())
                .build();
    }
}