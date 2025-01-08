package jpa.board.controller;

import jpa.board.dto.CommentDto;
import jpa.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("{postId}/comments")
    public ResponseEntity<Long> addComment(@PathVariable(name = "postId") Long postId, @RequestBody CommentDto.Request request) {
        return ResponseEntity.ok(commentService.addCommentToPost(postId, request));
    }

    @GetMapping("{postId}/comments")
    public ResponseEntity<List<CommentDto.Response>> getComments(@PathVariable(name = "postId") Long postId) {
        List<CommentDto.Response> commentList = commentService.findCommentToPost(postId);
        return ResponseEntity.ok(commentList);
    }

    @PatchMapping("{postId}/comments")
    public ResponseEntity<CommentDto.Response> updateComment(@PathVariable(name = "postId") Long postId, @RequestBody CommentDto.Request request) {
        return ResponseEntity.ok(commentService.updateCommentToPost(postId, request));
    }

    @DeleteMapping("{postId}/comments")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "postId") Long postId, @RequestBody CommentDto.Request request) {
        commentService.deleteCommentById(postId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
