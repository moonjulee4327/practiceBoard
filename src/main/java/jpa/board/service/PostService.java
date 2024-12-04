package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.dto.CreatePostRequest;
import jpa.board.dto.PostResponseDto;
import jpa.board.dto.PostIdDto;
import jpa.board.dto.UpdatePostRequestDto;
import jpa.board.repository.MemberRepository;
import jpa.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Long savePost(CreatePostRequest createPostRequest) {
        Member member = memberRepository.findById(createPostRequest.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("No Exist Post"));

        Post post = Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .member(member)
                .build();
        postRepository.save(post);

        return post.getId();
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllPost() {
        return postRepository.findAll()
                .stream()
                .map(Post::toPostDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponseDto findOnePost(Long postId) {
        return  postRepository.findById(postId)
                .map(Post::toPostDto)
                .orElseThrow(() -> new IllegalStateException("No Exist Post"));
    }

    @Transactional
    public PostIdDto updateOnePost(Long postId, UpdatePostRequestDto updatePostRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No Exist Post"));
        Long updatePostId = post.updatePost(updatePostRequestDto.getTitle(), updatePostRequestDto.getContent());
        return new PostIdDto(updatePostId);
    }

    public void deletePostById(Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new IllegalStateException("No Exist Post");
        }
        postRepository.deleteById(postId);
    }
}
