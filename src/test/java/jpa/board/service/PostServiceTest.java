package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.domain.RoleType;
import jpa.board.dto.PostDto;
import jpa.board.exception.PostNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글 작성 성공")
    void savePost() {
        Member mockMember = createMember();
        when(memberService.findAuthenticatedMember()).thenReturn(mockMember);

        PostDto.Request request = new PostDto.Request(1L, "제목", "내용");
        Post savedPost = createPost(1L, mockMember);

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostDto.Response response = postService.savePost(request);

        assertNotNull(response);
        assertEquals(1L, response.getPostId());
        assertEquals("제목", response.getTitle());
        assertEquals("내용", response.getContent());

        verify(memberService, times(1)).findAuthenticatedMember();
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("모든 게시글 조회-게시글이 존재하는 경우")
    void findAllPost() {
        Member mockMember = createMember();
        Post post1 = createPost(1L, mockMember);
        Post post2 = createPost(2L, mockMember);
        when(postRepository.findAll()).thenReturn(List.of(post1, post2));

        List<PostDto.Response> postList = postService.findAllPost();

        assertEquals(2, postList.size());
        assertEquals(1L, postList.get(0).getPostId());
        assertEquals(2L, postList.get(1).getPostId());

        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("특정 게시물 조회-게시글이 존재하는 경우")
    void findOnePost() {
        Member mockMember = createMember();
        Post post = createPost(1L, mockMember);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostDto.Response response = postService.findOnePost(1L);

        assertNull(response);
        assertEquals(post.getId(), response.getPostId());
        assertEquals("제목", response.getTitle());
        assertEquals("내용", response.getContent());

        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("특정 게시물 조회-게시글이 존재하지 않는 경우")
    void findOnePostNotFoundException() {
        Long notExistPostId = 1L;
        when(postRepository.findById(notExistPostId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.findOnePost(notExistPostId));
    }

    @Test
    @DisplayName("특정 게시물 수정 성공")
    void updateOnePost() {
        Member mockMember = createMember();
        when(memberService.findAuthenticatedMember()).thenReturn(mockMember);

        PostDto.Request request = new PostDto.Request(1L, "제목", "내용");
        Post savedPost = createPost(1L, mockMember);

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostDto.Response response = postService.savePost(request);

        PostDto.Request updateRequest = new PostDto.Request(savedPost.getId(), "수정된 제목", "수정된 내용");
        when(postRepository.findById(1L)).thenReturn(Optional.of(savedPost));
        postService.updateOnePost(savedPost.getId(), updateRequest);

        assertEquals(savedPost.getTitle(), updateRequest.getTitle());
        assertEquals(response.getTitle(), savedPost.getTitle());
        assertEquals(response.getContent(), savedPost.getContent());

        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("특정 게시물 삭제 성공")
    void deletePostById() {
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);

        postService.deletePostById(postId);

        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    @DisplayName("특정 게시물 삭제 실패")
    void deletePostNotFound() {
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(false);

        assertThrows(PostNotFoundException.class, () -> postService.deletePostById(postId));

        verify(postRepository, never()).deleteById(anyLong());
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