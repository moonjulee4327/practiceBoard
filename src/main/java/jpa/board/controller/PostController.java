package jpa.board.controller;

import jpa.board.dto.*;
import jpa.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<CreatePostResponceDto> createPost(@RequestBody CreatePostRequest createPostRequest) {
        Long postId = postService.savePost(createPostRequest);
        return ResponseEntity.ok(new CreatePostResponceDto(postId));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponceDto>> listPost() {
        List<PostResponceDto> list = postService.findPostAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponceDto> findPostOne(@PathVariable("postId") Long postId) {
        PostResponceDto postResponceDto = postService.findPostOne(postId);
        return ResponseEntity.ok(postResponceDto);
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<UpdatePostDto> updatePostOne(@PathVariable("postId") Long postId, @RequestBody UpdatePostRequestDto updatePostRequestDto) {
        System.out.println(updatePostRequestDto.getTitle());
        System.out.println(updatePostRequestDto.getContent());
        UpdatePostDto updatePostDto = postService.updatePostOne(postId, updatePostRequestDto);
        return ResponseEntity.ok(updatePostDto);
    }
}
