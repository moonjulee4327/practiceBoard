package jpa.board.controller;

import jpa.board.dto.CommentDto;
import jpa.board.service.CommentService;
import lombok.RequiredArgsConstructor;
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
        List<CommentDto.Response> commentList = commentService.findPostComment(postId);
        return ResponseEntity.ok(commentList);
    }
}
