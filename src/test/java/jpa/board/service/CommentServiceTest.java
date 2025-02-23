package jpa.board.service;

import jpa.board.domain.Comment;
import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.domain.RoleType;
import jpa.board.dto.CommentDto;
import jpa.board.exception.CommentPermissionException;
import jpa.board.exception.MemberNotFoundException;
import jpa.board.exception.PostNotFoundException;
import jpa.board.repository.CommentRepository;
import jpa.board.repository.MemberRepository;
import jpa.board.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SecurityContextService securityContextService;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 작성 성공")
    void addCommentToPost() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        CommentDto.Request commentRequest = new CommentDto.Request(1L, "댓글 내용");
        Comment comment = createComment(1L, mockMember, post.getId(), "댓글 내용");

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
    @DisplayName("댓글 작성 실패-게시글이 존재하지 않는 경우")
    void addCommentToNotPost() {
        Long postId = 1000L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        CommentDto.Request commentRequest = new CommentDto.Request(1L, "댓글 내용");
        assertThrows(PostNotFoundException.class, () -> commentService.addCommentToPost(postId, commentRequest));

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 작성 실패-회원이 아닌 경우")
    void addCommentNotMember() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        CommentDto.Request request = new CommentDto.Request(1L, "댓글 내용");
        String notAuthor = "hacker@gmail.com";

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(securityContextService.getCurrentMemberEmail()).thenReturn(notAuthor);
//        when(memberRepository.findByEmail(notAuthor)).thenReturn(Optional.empty());
        when(memberRepository.findByEmail(notAuthor)).thenThrow(new MemberNotFoundException("Member Not Found With Email : " + notAuthor, notAuthor));

        assertThrows(MemberNotFoundException.class, () -> commentService.addCommentToPost(post.getId(), request));
    }

    @Test
    @DisplayName("댓글 조회 성공")
    void findCommentToPost() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        Comment comment1 = createComment(1L, mockMember, post.getId(), "댓글 1");
        Comment comment2 = createComment(2L, mockMember, post.getId(), "댓글 2");
        List<Comment> commentList = List.of(comment1, comment2);

        when(commentRepository.findByPostId(post.getId())).thenReturn(commentList);

        List<CommentDto.Response> commentToPost = commentService.findCommentToPost(post.getId());

        assertEquals(commentToPost.size(), 2);
        assertEquals(commentToPost.get(0).getComment(), "댓글 1");
        assertEquals(commentToPost.get(1).getComment(), "댓글 2");

        verify(commentRepository, times(1)).findByPostId(post.getId());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateCommentToPost() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        Comment comment = createComment(1L, mockMember, post.getId(), "댓글");
        String updateComment = "댓글 수정!";
        CommentDto.Request request = new CommentDto.Request(comment.getId(), updateComment);

        when(commentRepository.findByIdAndPostId(comment.getId(), post.getId())).thenReturn(Optional.of(comment));
        when(securityContextService.getCurrentMemberEmail()).thenReturn(mockMember.getEmail());

        CommentDto.Response response = commentService.updateCommentToPost(post.getId(), request);

        assertEquals(response.getComment(), updateComment);

        verify(commentRepository, times(1)).findByIdAndPostId(comment.getId(), post.getId());
    }

    @Test
    @DisplayName("댓글 수정 실패-postId 또는 commentId 불일치")
    void updateCommentToPost_notFindByIdAndPostId() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        Comment comment = createComment(1L, mockMember, post.getId(), "댓글");
        CommentDto.Request request = CommentDto.Request.builder()
                .id(comment.getId())
                .comment("댓글 수정!")
                .build();

        when(commentRepository.findByIdAndPostId(comment.getId(), post.getId())).thenReturn(Optional.empty());

        assertThrows(CommentPermissionException.class, () -> commentService.updateCommentToPost(post.getId(), request));

        verify(commentRepository, times(1)).findByIdAndPostId(comment.getId(), post.getId());
        verify(securityContextService, never()).getCurrentMemberEmail();
    }

    @Test
    @DisplayName("댓글 수정 실패-댓글 작성자가 아닐 경우")
    void updateCommentToPost_notAuthor() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        Comment comment = createComment(1L, mockMember, post.getId(), "댓글");
        CommentDto.Request request = CommentDto.Request.builder()
                                            .id(comment.getId())
                                            .comment("댓글 수정!")
                                            .build();
        String notAuthor = "hacker@gmail.com";

        when(commentRepository.findByIdAndPostId(comment.getId(), post.getId())).thenReturn(Optional.of(comment));
        when(securityContextService.getCurrentMemberEmail()).thenReturn(notAuthor);

        assertThrows(CommentPermissionException.class, () -> commentService.updateCommentToPost(post.getId(), request));

        verify(commentRepository, times(1)).findByIdAndPostId(comment.getId(), post.getId());
        verify(securityContextService, times(1)).getCurrentMemberEmail();
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteCommentById() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        Comment comment = createComment(1L, mockMember, post.getId(), "댓글");
        CommentDto.Request request = CommentDto.Request.builder()
                                                    .id(comment.getId())
                                                    .build();

        when(commentRepository.findByIdAndPostId(comment.getId(), post.getId())).thenReturn(Optional.of(comment));
        when(securityContextService.getCurrentMemberEmail()).thenReturn(mockMember.getEmail());

        commentService.deleteCommentById(post.getId(), request);

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    @DisplayName("댓글 삭제 실패-postId 또는 commentId 불일치")
    void deleteCommentById_notFindByIdAndPostId() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        Comment comment = createComment(1L, mockMember, post.getId(), "댓글");
        CommentDto.Request request = CommentDto.Request.builder()
                                                    .id(comment.getId())
                                                    .build();

        when(commentRepository.findByIdAndPostId(comment.getId(), post.getId())).thenReturn(Optional.empty());

        assertThrows(CommentPermissionException.class, () -> commentService.deleteCommentById(post.getId(), request));
    }

    @Test
    @DisplayName("댓글 삭제 실패-댓글 작성자가 아닐 경우")
    void deleteCommentById_notAuthor() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        Comment comment = createComment(1L, mockMember, post.getId(), "댓글");
        CommentDto.Request request = CommentDto.Request.builder()
                                                    .id(comment.getId())
                                                    .build();
        String notAuthor = "hacker@gmail.com";

        when(commentRepository.findByIdAndPostId(comment.getId(), post.getId())).thenReturn(Optional.of(comment));
        when(securityContextService.getCurrentMemberEmail()).thenReturn(notAuthor);

        assertThrows(CommentPermissionException.class, () -> commentService.deleteCommentById(post.getId(), request));
    }

    private Comment createComment(Long id, Member member, Long postId, String comment) {
        return Comment.builder()
                .id(id)
                .comment(comment)
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