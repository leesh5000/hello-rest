package leesh.devcom.backend.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void isPojo() {
        RegisterRequest leesh = RegisterRequest.builder()
                .email("leesh@gmail.com")
                .username("leesh")
                .password("1111")
                .build();
    }

}