package jpa.board.exception;

import lombok.Getter;

@Getter
public class CommentPermissionException extends RuntimeException {
    private final Long commentId;

    public CommentPermissionException(String message, Long commentId) {
        super(message);
        this.commentId = commentId;
    }
}
