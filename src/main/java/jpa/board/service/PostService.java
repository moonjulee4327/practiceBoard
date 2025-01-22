package jpa.board.service;

import jpa.board.domain.Member;
import jpa.board.domain.Post;
import jpa.board.dto.PostDto;
import jpa.board.exception.PostNotFoundException;
import jpa.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;

    public PostDto.Response savePost(PostDto.Request request) {
        Member member = memberService.findAuthenticatedMember();

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .build();
        postRepository.save(post);

        return new PostDto.Response(post);
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
                .orElseThrow(() -> new PostNotFoundException("Post ID : " + postId + " Not Found", postId));
    }

    @Transactional
    public PostDto.Response updateOnePost(Long postId, PostDto.Request request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post ID : " + postId + " Not Found", postId));
        Long updatePostId = post.updatePost(request.getTitle(), request.getContent());
        return new PostDto.Response(updatePostId);
    }

    public void deletePostById(Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post ID : " + postId + " Not Found", postId);
        }
        postRepository.deleteById(postId);
    }
}
