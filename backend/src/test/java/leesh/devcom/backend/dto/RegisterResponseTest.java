package leesh.devcom.backend.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RegisterResponseTest {

    @Test
    void builder_getter() {
        RegisterResponse build = RegisterResponse.builder()
                .id(1L)
                .build();
        Assertions.assertThat(build.getId()).isEqualTo(1L);
    }

}