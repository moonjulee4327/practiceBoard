package jpa.board.service;

import jpa.board.domain.Comment;
import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.domain.RoleType;
import jpa.board.repository.CommentRepository;
import jpa.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private SecurityContextService securityContextService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void addCommentToPost() {
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