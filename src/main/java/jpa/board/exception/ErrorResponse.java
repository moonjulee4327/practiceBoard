package jpa.board.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorResponse {
    private String message;
    private String code;
    private List<FieldErrorDetail> fieldErrors;

    public ErrorResponse(ErrorCode errorCode, List<FieldErrorDetail> fieldErrors) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
        this.fieldErrors = fieldErrors;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, null);
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        List<FieldErrorDetail> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(FieldErrorDetail::of)
                .toList();
        return new ErrorResponse(errorCode, fieldErrors);
    }
}
