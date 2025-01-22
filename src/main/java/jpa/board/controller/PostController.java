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
    public ResponseEntity<PostDto.Response> createPost(@RequestBody PostDto.Request request) {
        return ResponseEntity.ok(postService.savePost(request));
    }

    @GetMapping("")
    public ResponseEntity<List<PostDto.Response>> getPosts() {
        return ResponseEntity.ok(postService.findAllPost());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto.Response> getPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.findOnePost(postId));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostDto.Response> updatePost(@PathVariable("postId") Long postId, @RequestBody PostDto.Request request) {
        return ResponseEntity.ok(postService.updateOnePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
