package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.dto.PostDto;
import jpa.board.exception.PostNotFoundException;
import jpa.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;

    @Transactional
    public PostDto.Response savePost(PostDto.Request request) {
        Member member = memberService.findAuthenticatedMember();

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .build();
        Post savedPost = postRepository.save(post);
        log.info("Added Post ID : {}", savedPost.getId());

        return new PostDto.Response(savedPost);
    }

    @Transactional(readOnly = true)
    public List<PostDto.Response> findAllPost() {
        return postRepository.findAll()
                .stream()
                .map(PostDto.Response::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostDto.Response findOnePost(Long postId) {
        return  postRepository.findById(postId)
                .map(PostDto.Response::new)
                .orElseThrow(() -> new PostNotFoundException("Post ID : " + postId + " Not Found (Post One)", postId));
    }

    @Transactional
    public PostDto.Response updateOnePost(Long postId, PostDto.Request request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post ID : " + postId + " Not Found (Update Post)", postId));
        Long updatePostId = post.updatePost(request.getTitle(), request.getContent());
        log.info("Updated Post ID : {}", postId);
        return new PostDto.Response(updatePostId);
    }

    @Transactional
    public void deletePostById(Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post ID : " + postId + " Not Found (Delete Post)", postId);
        }
        postRepository.deleteById(postId);
        log.info("Deleted Post ID : {}", postId);
    }
}
