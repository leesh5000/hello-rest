package leesh.devcom.backend.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(@NotNull ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
