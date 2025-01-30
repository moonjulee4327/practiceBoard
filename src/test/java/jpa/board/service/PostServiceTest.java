package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.domain.RoleType;
import jpa.board.dto.PostDto;
import jpa.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
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
    }

    @Test
    void findOnePost() {
    }

    @Test
    void updateOnePost() {
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