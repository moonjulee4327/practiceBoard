package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.dto.CreatePostRequest;
import jpa.board.repository.MemberRepository;
import jpa.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
