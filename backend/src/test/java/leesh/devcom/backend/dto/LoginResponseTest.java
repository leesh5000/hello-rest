package leesh.devcom.backend.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoginResponseTest {

    @Test
    void builder_getter() {

        LoginResponse responseDto = LoginResponse.builder()
                .expirySec(0L)
                .accessToken("access_token")
                .build();
        Assertions.assertThat(responseDto).isNotNull();
        String accessToken = responseDto.getAccessToken();
        Long expirySec = responseDto.getExpirySec();
    }
}