package jpa.board.dto;

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
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private MemberDto.Response memberDto;

        public Response(Long id) {
            this.id = id;
        }

        public Response(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
        }
    }
}
