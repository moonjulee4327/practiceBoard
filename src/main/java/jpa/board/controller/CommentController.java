package jpa.board.controller;

import jpa.board.dto.CommentDto;
import jpa.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("{postId}/comments")
    public ResponseEntity<Long> addComment(@PathVariable(name = "postId") Long postId, @RequestBody CommentDto.Request request) {
        return ResponseEntity.ok(commentService.addCommentToPost(postId, request));
    }
}
