package jpa.board.controller;

import jpa.board.dto.*;
import jpa.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<CreatePostResponceDto> createPost(@RequestBody CreatePostRequest createPostRequest) {
        Long postId = postService.savePost(createPostRequest);
        return ResponseEntity.ok(new CreatePostResponceDto(postId));
    }

    @GetMapping("")
    public ResponseEntity<List<PostResponceDto>> getPosts() {
        List<PostResponceDto> list = postService.findAllPost();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponceDto> getPost(@PathVariable("postId") Long postId) {
        PostResponceDto postResponceDto = postService.findOnePost(postId);
        return ResponseEntity.ok(postResponceDto);
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<UpdatePostDto> updatePost(@PathVariable("postId") Long postId, @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        UpdatePostDto updatePostDto = postService.updateOnePost(postId, updatePostRequestDto);
        return ResponseEntity.ok(updatePostDto);
    }
}
