package leesh.devcom.backend.security;

import leesh.devcom.backend.dto.RegisterRequest;
import leesh.devcom.backend.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService.save(RegisterRequest.builder().email("test1@gmail.com").username("test1").password("1111").build());
        memberService.save(RegisterRequest.builder().email("test2@gmail.com").username("test2").password("1111").build());
        memberService.save(RegisterRequest.builder().email("test3@gmail.com").username("test3").password("1111").build());
    }

    @AfterEach
    void tearDown() {
        memberService.deleteAll();
    }

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