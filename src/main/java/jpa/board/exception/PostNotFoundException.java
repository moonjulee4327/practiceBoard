package jpa.board.exception;

public class PostNotFoundException extends RuntimeException{
    private final Long postId;

    public PostNotFoundException(String message, Long postId) {
        super(message);
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}
