package leesh.devcom.backend.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.validation.BindingResult;

import static leesh.devcom.backend.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ErrorResponseTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void of() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        ErrorResponse of = ErrorResponse.of(INVALID_INPUT_VALUE, mockBindingResult);
    }

    @Test
    void constructor() {

    }

    @Test
    void getter() {
        BindingResult mockBindingResult = mock(BindingResult.class);
        ErrorResponse of = ErrorResponse.of(INVALID_INPUT_VALUE, mockBindingResult);
        Assertions.assertThat(of.getStatus()).isEqualTo(INVALID_INPUT_VALUE.getStatus());
        Assertions.assertThat(of.getCode()).isEqualTo(INVALID_INPUT_VALUE.getCode());
        Assertions.assertThat(of.getMessage()).isEqualTo(INVALID_INPUT_VALUE.getMessage());
        Assertions.assertThat(of.getErrors()).isNotNull();
    }
}