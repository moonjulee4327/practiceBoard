package jpa.board.exception;

import lombok.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class ErrorResponse {
    private String message;
    private String code;
    private List<FieldError> fieldErrors;

    public static ErrorResponse of(String message, String code) {
        return new ErrorResponse(message, code, null);
    }

    public static ErrorResponse of(String message, String code, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> new FieldError(fieldError.getField()
                        , String.valueOf(fieldError.getRejectedValue())
                        , fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorResponse(message, code, fieldErrors);
    }
}
