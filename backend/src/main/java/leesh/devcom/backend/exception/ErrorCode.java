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
    INVALID_INPUT_VALUE(BAD_REQUEST, 4000001, "invalid input value"),

    NOT_FOUND_ACCESS_TOKEN(UNAUTHORIZED, 4010001, "access token not found in header field"),
    INVALID_JWT(UNAUTHORIZED, 4010002, "The token was signed incorrectly"),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, 4010003, "The access token was expired"),

    NOT_EXIST_MEMBER(CONFLICT, 4040001, "already exist member"),

    ALREADY_EXIST_MEMBER(CONFLICT, 4090001, "already exist member"),


    /**
     *
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000001, "internal server error")

    ;

    private final HttpStatus status;
    private final Integer code;
    private final String message;
}
