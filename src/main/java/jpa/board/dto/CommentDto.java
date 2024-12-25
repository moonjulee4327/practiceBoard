package jpa.board.dto;

import jpa.board.domain.Comment;
import jpa.board.domain.Member;
import jpa.board.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

public class CommentDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String comment;
        private Member member;
        private Post post;

        public Comment toEntity(Member member, Long postId) {
            return Comment.builder()
                    .comment(comment)
                    .createdDate(ZonedDateTime.now())
                    .modifiedDate(null)
                    .member(member)
                    .postId(postId)
                    .build();
        }
    }
}
