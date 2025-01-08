package jpa.board.exception;

public class CommentPermissionException extends RuntimeException {
    private final Long commentId;

    public CommentPermissionException(String message, Long commentId) {
        super(message);
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}
