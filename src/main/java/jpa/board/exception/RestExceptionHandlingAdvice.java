package jpa.board.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandlingAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("handleMethodArgumentNotValidException", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of("invalid field request.", "INVALID FIELD", exception.getBindingResult()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exception) {
        log.error("handleBadCredentialsException", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of("Invalid email or password", "AUTHENTICATION_FAILED"));
    }
}
