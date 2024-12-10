package jpa.board.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorResponse {
    private String message;
    private String code;
    private List<FieldError> fieldErrors;

    // Constructor
    public ErrorResponse(String message, String code, List<FieldError> fieldErrors) {
        this.message = message;
        this.code = code;
        this.fieldErrors = fieldErrors;
    }

    // Static factory method
    public static ErrorResponse of(String message, String code, BindingResult bindingResult) {
        // Spring의 기본 FieldError 리스트를 그대로 사용
        return new ErrorResponse(message, code, bindingResult.getFieldErrors());
    }
}
