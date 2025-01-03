package jpa.board.controller;

import jpa.board.dto.*;
import jpa.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<PostIdDto> createPost(@RequestBody CreatePostRequest createPostRequest) {
        Long postId = postService.savePost(createPostRequest);
        return ResponseEntity.ok(new PostIdDto(postId));
    }

    @GetMapping("")
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        List<PostResponseDto> list = postService.findAllPost();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable("postId") Long postId) {
        PostResponseDto postResponseDto = postService.findOnePost(postId);
        return ResponseEntity.ok(postResponseDto);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostIdDto> updatePost(@PathVariable("postId") Long postId, @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        PostIdDto postIdDto = postService.updateOnePost(postId, updatePostRequestDto);
        return ResponseEntity.ok(postIdDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
