package jpa.board.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException{
    private final String name;

    public MemberNotFoundException(String message, String name) {
        super(message);
        this.name = name;
    }
}
