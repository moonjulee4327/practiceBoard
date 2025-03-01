package jpa.board.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FieldErrorDetail {
    private final String field;
    private final String rejectedValue;
    private final String message;

    public static FieldErrorDetail of(FieldError fieldError) {
        return new FieldErrorDetail(fieldError.getField(),
                fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : "null",
                fieldError.getDefaultMessage());
    }
}
