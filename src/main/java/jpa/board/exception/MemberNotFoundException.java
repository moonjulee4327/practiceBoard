package jpa.board.exception;

public class MemberNotFoundException extends RuntimeException{
    private final String name;

    public MemberNotFoundException(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getMemberName() {
        return name;
    }
}
