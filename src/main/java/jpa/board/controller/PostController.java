package jpa.board.controller;

import jpa.board.dto.CreatePostRequest;
import jpa.board.dto.CreatePostResponceDto;
import jpa.board.dto.PostResponceDto;
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
}
