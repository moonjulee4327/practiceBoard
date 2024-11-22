package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.dto.CreatePostRequest;
import jpa.board.dto.PostResponceDto;
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
                .orElseThrow(() -> new IllegalArgumentException("No Exist Member"));

        Post post = Post.builder().title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .member(member)
                .build();
        postRepository.save(post);

        return post.getId();
    }

    @Transactional(readOnly = true)
    public List<PostResponceDto> findPostAll() {
        return postRepository.findAll()
                .stream()
                .map(Post::toPostDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponceDto findPostOne(Long postId) {
        return  postRepository.findById(postId)
                .map(Post::toPostDto).get();
    }
}
