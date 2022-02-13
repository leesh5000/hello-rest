package leesh.devcom.backend.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Test
    void afterPropertiesSet() {
    }

    @Test
    void createAccessTokenForLogin_ok_test() {

        // given
        String email = "test1@gmail.com";
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // when & then
        String accessToken = jwtUtil.createAccessToken(userDetails);
        Assertions.assertThat(accessToken).isNotNull();
    }

    @Test
    void createRefreshToken_ok_test() {

        // given
        String email = "test1@gmail.com";
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        String accessToken = jwtUtil.createAccessToken(userDetails);

        // when
        String refreshToken = jwtUtil.createRefreshToken(accessToken);

        // then
        Assertions.assertThat(refreshToken).isNotNull();

    }
}