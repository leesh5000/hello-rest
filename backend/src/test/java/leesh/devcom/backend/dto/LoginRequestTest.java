package leesh.devcom.backend.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void builder_test() {

        LoginRequest build = LoginRequest.builder()
                .email("lee@test.com")
                .password("1111")
                .build();

        Assertions.assertThat(build).isNotNull();
    }

    @Test
    void getter() {

        String email = "lee@test.com";
        String password = "1111";
        LoginRequest build = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        Assertions.assertThat(build.getEmail()).isEqualTo(email);
        Assertions.assertThat(build.getPassword()).isEqualTo(password);
    }
}