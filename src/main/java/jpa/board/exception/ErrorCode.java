package jpa.board.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {
    MEMBER_INVALID(HttpStatus.BAD_REQUEST, "MEMBER_001", "존재하지 않는 회원입니다."),
    MEMBER_AUTHORIZATION(HttpStatus.FORBIDDEN, "MEMBER_002", "로그인이 필요합니다."),
    MEMBER_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "MEMBER_003", "인증이 필요합니다."),

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_001", "게시글이 존재하지 않습니다."),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_001", "댓글이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
