package leesh.devcom.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * [4XX :: CLIENT ERROR]
 * [5XX :: INTERNAL SERVER ERROR]
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     *
     */
    INVALID_INPUT_VALUE(BAD_REQUEST, 4000001, "invalid input value")
    ;

    private final HttpStatus status;
    private final Integer code;
    private final String message;
}
