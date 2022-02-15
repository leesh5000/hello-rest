package leesh.devcom.backend.service;

import leesh.devcom.backend.dto.RegisterRequest;
import leesh.devcom.backend.exception.CustomException;
import leesh.devcom.backend.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void saveTest() {

        // given
        String email = "leesh@gmail.com";
        String username = "leesh";
        String password = "1111";

        RegisterRequest build = RegisterRequest.builder()
                .email(email).username(username).password(password)
                .build();
        Long save = memberService.save(build);

        Assertions.assertThat(save).isGreaterThan(0L);
    }

    @Test
    void save_fail_test() {
        // given
        String email = "leesh@gmail.com";
        String username = "leesh";
        String password = "1111";

        RegisterRequest build = RegisterRequest.builder()
                .email(email).username(username).password(password)
                .build();

        CustomException e = assertThrows(CustomException.class, () -> memberService.save(build));
        Assertions.assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ALREADY_EXIST_MEMBER);
    }
}