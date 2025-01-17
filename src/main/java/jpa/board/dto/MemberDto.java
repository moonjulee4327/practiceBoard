package jpa.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jpa.board.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


public class MemberDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "이메일을 정상적으로 입력해주세요.")
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        private String email;
        @NotBlank
        private String name;
        @NotBlank
        private String password;
        @NotBlank
        private String nickname;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String nickname;
        private ZonedDateTime createdDate;

        public Response(Long id) {
            this.id = id;
        }

        public Response(Member member) {
            this.id = member.getId();
            this.name = member.getName();
            this.nickname = member.getNickname();
            this.createdDate = member.getCreatedDate();
        }
    }
}
