package leesh.devcom.backend.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    @Test
    void builderTest() {
        LoginResponse responseDto = LoginResponse.builder()
                .expirySec(0L)
                .accessToken("access_token")
                .build();

        String accessToken = responseDto.getAccessToken();
        Long expirySec = responseDto.getExpirySec();
    }

}