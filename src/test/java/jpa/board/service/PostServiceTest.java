package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.domain.RoleType;
import jpa.board.dto.PostDto;
import jpa.board.exception.PostNotFoundException;
import jpa.board.repository.PostRepository;
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
    void findOnePostNotFoundException() {
        Long notExistPostId = 1L;
        when(postRepository.findById(notExistPostId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.findOnePost(notExistPostId));
    }

    @Test
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
        assertEquals("수정된 제목", savedPost.getTitle());
        assertEquals("수정된 내용", savedPost.getContent());

        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void deletePostById() {
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