package leesh.devcom.backend.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionTest {

    @Test
    void constructor() {
        CustomException e = new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
        assertThat(e).isNotNull();
    }

}