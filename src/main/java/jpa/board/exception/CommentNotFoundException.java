package jpa.board.exception;

public class CommentNotFoundException extends RuntimeException{
    private final Long commentId;

    public CommentNotFoundException(String message, Long commentId) {
        super(message);
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}
