package leesh.devcom.backend.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private HttpStatus status;
    private Integer code;
    private String message;
    private List<Error> errors = new ArrayList<>();

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    private static class Error {
        private String field;
        private String value;
        private String reason;
    }

    private ErrorResponse(final ErrorCode code) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    private ErrorResponse(final ErrorCode code, final BindingResult bindingResult) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.errors = bindingResult.getFieldErrors().stream()
                .map(e -> new Error(
                        ObjectUtils.nullSafeToString(e.getField()),
                        e.getRejectedValue() == null ? "" : e.getRejectedValue().toString(),
                        e.getDefaultMessage() == null ? "" : e.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        ErrorResponse errorResponse = new ErrorResponse(code, bindingResult);
        return errorResponse;
    }
}
