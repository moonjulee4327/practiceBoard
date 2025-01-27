package jpa.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jpa.board.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PostDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long postId;
        private String title;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long postId;
        private String title;
        private String content;
        private MemberDto.Response memberDto;

        public Response(Long postId) {
            this.postId = postId;
        }

        public Response(Post post) {
            this.postId = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
        }
    }
}
