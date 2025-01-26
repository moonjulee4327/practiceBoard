package jpa.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
        private Long id;
        private String comment;

        public Comment toEntity(Member member, Long postId) {
            return Comment.builder()
                    .comment(comment)
                    .createdDate(ZonedDateTime.now())
                    .modifiedDate(null)
                    .member(member)
                    .postId(postId)
                    .authorName(member.getNickname())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private String author;
        private String comment;
        private ZonedDateTime createdDate;

        public Response(Comment comment) {
            this.id = comment.getId();
            this.author = comment.getAuthorName();
            this.comment = comment.getComment();
            this.createdDate = comment.getCreatedDate();
        }
    }
}
